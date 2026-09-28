package com.orbit.recovery

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orbit.recovery.data.UserPreferencesRepository
import com.orbit.recovery.data.dataStore
import com.orbit.recovery.ui.theme.OrbitBackground
import com.orbit.recovery.ui.theme.OrbitTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            val repo = UserPreferencesRepository(applicationContext.dataStore)
            val prefs = repo.preferencesFlow.first()
            
            val vpnEnabled = prefs[UserPreferencesRepository.VPN_ENABLED_KEY] ?: false
            if (vpnEnabled) {
                val intent = Intent(this@MainActivity, OrbitVpnService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
            }

            val appLimitsJson = prefs[UserPreferencesRepository.APP_LIMITS_KEY] ?: "{}"
            val type = object : TypeToken<Map<String, Int>>() {}.type
            val limitsMap: Map<String, Int> = Gson().fromJson(appLimitsJson, type) ?: emptyMap()
            if (limitsMap.isNotEmpty()) {
                val intent = Intent(this@MainActivity, AppBlockerService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
            }
        }

        setContent {
            val onboardingComplete by applicationContext.dataStore.data
                .map { it[UserPreferencesRepository.ONBOARDING_COMPLETE_KEY] ?: false }
                .collectAsState(initial = false)
                
            OrbitTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = OrbitBackground) {
                    val navController = rememberNavController()
                    val startDest = if (onboardingComplete) "home" else "welcome"
                    
                    AppNavigation(
                        navController = navController,
                        startDestination = startDest
                    )
                }
            }
        }
    }
}
