package com.orbit.recovery.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.orbit.recovery.ui.components.*
import com.orbit.recovery.ui.theme.*

@Composable
fun WelcomeScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToQuiz: () -> Unit
) {
    var currentStep by remember { mutableStateOf(1) }
    
    val title = when(currentStep) {
        1 -> "You're not alone"
        2 -> "Take back control, instantly"
        3 -> "See progress that feels real"
        else -> "Your coach, always by your side"
    }
    
    val subtitle = when(currentStep) {
        1 -> "A calm space that reminds you: relapse doesn't mean failure. Support is part of the plan."
        2 -> "One tap opens a fast reset: breathing, grounding, and a short plan to ride the urge wave."
        3 -> "Track your streak, collect orbs, and notice the small wins that add up to momentum."
        else -> "A friendly, non-judgmental guide that helps you choose your next right action — not perfection."
    }
    
    val icon = when(currentStep) {
        1 -> Icons.Default.Group
        2 -> Icons.Default.Shield
        3 -> Icons.Default.LocalFireDepartment
        else -> Icons.Default.AutoAwesome
    }
    
    val buttonText = if (currentStep == 4) "Start your plan >" else "Continue >"

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = OrbitBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Top Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ORBIT / RECOVERY COMPANION PROTOTYPE",
                    style = Typography.labelSmall,
                    color = OrbitTextMuted,
                    modifier = Modifier.weight(1f)
                )
                
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .border(1.dp, OrbitBorder, RoundedCornerShape(50))
                        .clickable { onNavigateToHome() }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Preview Home",
                        style = Typography.labelSmall,
                        color = OrbitTextPrimary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Main Card
            OrbitCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = title,
                            style = Typography.headlineLarge,
                            color = OrbitTextPrimary,
                            modifier = Modifier.weight(1f).padding(end = 16.dp)
                        )
                        
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(OrbitSurfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = OrbitPrimary
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = subtitle,
                        style = Typography.bodyLarge,
                        color = OrbitTextSecondary
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Center Content
                    when (currentStep) {
                        1 -> CommunityMomentumCard()
                        2 -> AnimatedBreathingCircle(ringColor = Color(0xFFE8A09A))
                        3 -> AnimatedBreathingCircle(ringColor = OrbitPrimary)
                        4 -> AnimatedBreathingCircle(ringColor = Color(0xFFE8A09A))
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    OrbitProgressBar(step = currentStep, totalSteps = 4)
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    OrbitPrimaryButton(
                        text = buttonText,
                        onClick = {
                            if (currentStep < 4) {
                                currentStep++
                            } else {
                                onNavigateToQuiz()
                            }
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            PremiumTeaserCard()
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "This prototype is for support and self-improvement, not medical advice.",
                style = Typography.bodyMedium,
                color = OrbitTextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CommunityMomentumCard() {
    Surface(
        color = OrbitSurfaceVariant,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "COMMUNITY / THIS WEEK'S MOMENTUM",
                style = Typography.labelSmall,
                color = OrbitTextSecondary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "Folded (reset)", style = Typography.bodyMedium, color = OrbitTextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "38%", style = Typography.headlineMedium, color = OrbitTextPrimary)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Still going strong", style = Typography.bodyMedium, color = OrbitTextSecondary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "62%", style = Typography.headlineMedium, color = OrbitPrimary)
                }
            }
        }
    }
}

@Composable
fun AnimatedBreathingCircle(ringColor: Color, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer circle
        Box(
            modifier = Modifier
                .size(200.dp)
                .scale(scale)
                .border(1.5.dp, ringColor, CircleShape)
        )
        // Inner circle
        Box(
            modifier = Modifier
                .size(130.dp)
                .border(1.5.dp, ringColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "INHALE", style = Typography.labelSmall.copy(color = OrbitTextPrimary))
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .size(4.dp)
                        .background(OrbitTextPrimary, CircleShape)
                )
                Text(text = "EXHALE", style = Typography.labelSmall.copy(color = OrbitTextPrimary))
            }
        }
    }
}

@Composable
fun PremiumTeaserCard() {
    Surface(
        color = OrbitSurfaceVariant,
        shape = RoundedCornerShape(18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = "PREMIUM TEASER", style = Typography.labelSmall, color = OrbitTextMuted)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Loved by thousands", style = Typography.titleLarge, color = OrbitTextPrimary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Unlock deeper insights, guided resets, and personalized routines.", style = Typography.bodyMedium, color = OrbitTextSecondary)
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(4) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = OrbitAmber, modifier = Modifier.size(16.dp))
                }
                Icon(imageVector = Icons.Outlined.Star, contentDescription = null, tint = OrbitTextMuted, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "4.8 Rated highly", style = Typography.labelSmall, color = OrbitTextSecondary)
            }
        }
    }
}
