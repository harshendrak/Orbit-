package com.orbit.recovery.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orbit.recovery.ui.components.*
import com.orbit.recovery.ui.theme.*

@Composable
fun PersonalizationScreen(
    onNavigateBack: () -> Unit,
    onNavigateToResults: () -> Unit,
    onSkip: () -> Unit,
    viewModel: PersonalizationViewModel = viewModel()
) {
    var currentStep by remember { mutableStateOf(1) }
    
    // States for answers
    var selectedReasons by remember { mutableStateOf(setOf<String>()) }
    var targetHabit by remember { mutableStateOf("") }
    var ageGroup by remember { mutableStateOf("") }
    var routineDuration by remember { mutableStateOf("") }
    var usageIncreased by remember { mutableStateOf("") }
    var habitChanges by remember { mutableStateOf("") }
    var financialImpact by remember { mutableStateOf("") }
    var religion by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    
    val totalSteps = 9
    val percentage = (currentStep.toFloat() / totalSteps * 100).toInt()
    
    Surface(modifier = Modifier.fillMaxSize(), color = OrbitBackground) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, OrbitBorder),
                    color = Color.Transparent,
                    onClick = {
                        if (currentStep > 1) {
                            currentStep--
                        } else {
                            onNavigateBack()
                        }
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(16.dp),
                            tint = OrbitTextPrimary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Back", style = Typography.labelSmall, color = OrbitTextPrimary)
                    }
                }
                
                Text(
                    text = "Skip",
                    style = Typography.labelSmall,
                    color = OrbitTextSecondary,
                    modifier = Modifier.clickable { onSkip() }.padding(8.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Scrollable Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                OrbitCard(modifier = Modifier.fillMaxWidth()) {
                    Column {
                        // Question Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Question $currentStep of $totalSteps",
                                style = Typography.labelSmall,
                                color = OrbitTextMuted
                            )
                            Text(
                                text = "$percentage%",
                                style = Typography.labelSmall,
                                color = OrbitPrimary
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        LinearProgressIndicator(
                            progress = { currentStep.toFloat() / totalSteps },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = OrbitPrimary,
                            trackColor = OrbitBorder
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Dynamic Question Content
                        AnimatedContent(targetState = currentStep, label = "QuestionContent") { step ->
                            Column {
                                val questionTitle = getQuestionTitle(step)
                                val subtitle = getQuestionSubtitle(step)
                                
                                Text(
                                    text = questionTitle,
                                    style = Typography.headlineLarge,
                                    color = OrbitTextPrimary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = subtitle,
                                    style = Typography.bodyLarge,
                                    color = OrbitTextSecondary
                                )
                                
                                Spacer(modifier = Modifier.height(24.dp))
                                
                                // Options
                                when (step) {
                                    1 -> {
                                        val options = listOf("Regain control over my time", "Improve focus and energy", "Feel more confident", "Strengthen relationships", "Reduce shame and spirals", "Build better habits")
                                        options.forEach { option ->
                                            OrbitOptionRow(
                                                text = option,
                                                isSelected = selectedReasons.contains(option),
                                                onClick = {
                                                    selectedReasons = if (selectedReasons.contains(option)) {
                                                        selectedReasons - option
                                                    } else {
                                                        selectedReasons + option
                                                    }
                                                }
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                    2 -> {
                                        val options = listOf("Pornography / adult content", "Social media & scrolling", "Gaming", "Gambling", "Alcohol or substances", "Other compulsive behavior")
                                        options.forEach { option ->
                                            OrbitOptionRow(
                                                text = option,
                                                isSelected = targetHabit == option,
                                                onClick = { targetHabit = option }
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                    3 -> {
                                        val options = listOf("Under 18", "18–24", "25–34", "35–44", "45+")
                                        options.forEach { option ->
                                            OrbitOptionRow(
                                                text = option,
                                                isSelected = ageGroup == option,
                                                onClick = { ageGroup = option }
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                        
                                        if (ageGroup == "Under 18") {
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Surface(
                                                color = OrbitPanicBg,
                                                shape = RoundedCornerShape(12.dp)
                                            ) {
                                                Text(
                                                    text = "Orbit is designed for adults 18+. Please speak to a trusted adult or counselor.",
                                                    style = Typography.bodyMedium,
                                                    color = OrbitPanicButton,
                                                    modifier = Modifier.padding(16.dp)
                                                )
                                            }
                                        }
                                    }
                                    4 -> {
                                        val options = listOf("Recently", "1–2 years ago", "3–5 years ago", "6–10 years ago", "10+ years ago")
                                        options.forEach { option ->
                                            OrbitOptionRow(
                                                text = option,
                                                isSelected = routineDuration == option,
                                                onClick = { routineDuration = option }
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                    5 -> {
                                        val options = listOf("Yes", "No", "Not sure")
                                        options.forEach { option ->
                                            OrbitOptionRow(
                                                text = option,
                                                isSelected = usageIncreased == option,
                                                onClick = { usageIncreased = option }
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                    6 -> {
                                        val options = listOf("Yes", "No", "Not sure")
                                        options.forEach { option ->
                                            OrbitOptionRow(
                                                text = option,
                                                isSelected = habitChanges == option,
                                                onClick = { habitChanges = option }
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                    7 -> {
                                        val options = listOf("Yes", "No", "Prefer not to say")
                                        options.forEach { option ->
                                            OrbitOptionRow(
                                                text = option,
                                                isSelected = financialImpact == option,
                                                onClick = { financialImpact = option }
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                    8 -> {
                                        val options = listOf("Yes", "No", "Prefer not to say")
                                        options.forEach { option ->
                                            OrbitOptionRow(
                                                text = option,
                                                isSelected = religion == option,
                                                onClick = { religion = option }
                                            )
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                    9 -> {
                                        OrbitTextField(
                                            value = name,
                                            onValueChange = { name = it },
                                            hint = "Alex",
                                            leadingIcon = {
                                                Icon(
                                                    imageVector = Icons.Default.Person,
                                                    contentDescription = null,
                                                    tint = OrbitTextMuted,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        val isContinueEnabled = when (currentStep) {
                            1 -> selectedReasons.isNotEmpty()
                            2 -> targetHabit.isNotEmpty()
                            3 -> ageGroup.isNotEmpty() && ageGroup != "Under 18"
                            4 -> routineDuration.isNotEmpty()
                            5 -> usageIncreased.isNotEmpty()
                            6 -> habitChanges.isNotEmpty()
                            7 -> financialImpact.isNotEmpty()
                            8 -> religion.isNotEmpty()
                            9 -> name.isNotBlank()
                            else -> true
                        }
                        
                        OrbitPrimaryButton(
                            text = "Continue",
                            enabled = isContinueEnabled,
                            onClick = {
                                if (currentStep < totalSteps) {
                                    currentStep++
                                } else {
                                    viewModel.saveQuizResults(
                                        name, selectedReasons, targetHabit, ageGroup,
                                        routineDuration, usageIncreased, habitChanges, financialImpact, religion
                                    )
                                    onNavigateToResults()
                                }
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Your answers are stored on this device (local storage) for this prototype. You can reset from Profile anytime.",
                    style = Typography.bodyMedium,
                    color = OrbitTextMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

fun getQuestionTitle(step: Int) = when(step) {
    1 -> "What brings you here?"
    2 -> "What are you trying to change?"
    3 -> "How old are you?"
    4 -> "When did this habit first become part of your routine?"
    5 -> "Has your usage increased over time?"
    6 -> "Have you noticed any changes in this habit over time?"
    7 -> "Has this habit had any financial impact on you?"
    8 -> "Are you religious?"
    9 -> "What should we call you?"
    else -> ""
}

fun getQuestionSubtitle(step: Int) = when(step) {
    1 -> "Choose as many as you like. We'll tailor your plan around what matters most."
    2 -> "No judgment here. This is just for context."
    3 -> "You must be 18+ to use Orbit."
    4 -> "Pick what feels closest. You can always update later."
    5 -> "No judgment — this is common, and awareness is progress."
    6 -> "Some people notice they seek more novelty or intensity. Answer what feels true for you."
    7 -> "This can help us understand patterns around friction and access."
    8 -> "Optional. This helps us frame support in a way that feels aligned for you."
    9 -> "A first name or nickname is perfect."
    else -> ""
}
