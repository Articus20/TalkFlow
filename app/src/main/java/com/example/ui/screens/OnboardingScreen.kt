package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberAlert
import com.example.ui.theme.BorderLine
import com.example.ui.theme.CoolGray
import com.example.ui.theme.DeepNightBlue
import com.example.ui.theme.ElevatedSurface
import com.example.ui.theme.LilacAccent
import com.example.ui.theme.NeonMint
import com.example.ui.theme.Surface2
import com.example.ui.theme.Surface3
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.WhiteText

@Composable
fun OnboardingScreen(
    currentName: String,
    currentGoals: String,
    currentTargetMins: Int,
    costaRicaTime: String,
    onSaveProfile: (name: String, goals: String, targetMins: Int) -> Unit
) {
    var name by remember { mutableStateOf(currentName.ifBlank { "Maya" }) }
    var goals by remember { mutableStateOf(currentGoals.ifBlank { "Eliminate speaking anxiety, practice for interviews, and sound natural in conversations." }) }
    var targetMins by remember { mutableIntStateOf(if (currentTargetMins > 0) currentTargetMins else 10) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNightBlue)
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 28.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // App Logo & Bot Avatar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(VibrantPurple, DeepNightBlue)
                        )
                    )
                    .border(2.dp, NeonMint, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "TalkFlow Logo",
                    tint = NeonMint,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = "TalkFlow",
                    color = WhiteText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Zero-latency AI Voice Coach",
                    color = NeonMint,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Title & Description
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Set Up Your Profile & Daily Goals",
                color = WhiteText,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp
            )
            Text(
                text = "Eliminate language anxiety through real-time adaptive speaking. Tell us your name and objectives to personalize your coaching.",
                color = CoolGray,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }

        // Costa Rica Time Zone Info Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Surface2)
                .border(1.dp, NeonMint.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Flag,
                    contentDescription = "Costa Rica Flag",
                    tint = AmberAlert,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Time Zone: Costa Rica (UTC-6)",
                        color = WhiteText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Current local time: $costaRicaTime · Daily streak advances in this zone.",
                        color = CoolGray,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Username Input
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Your Name / Username",
                color = WhiteText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("username_input"),
                placeholder = { Text("e.g. Maya", color = CoolGray) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonMint,
                    unfocusedBorderColor = BorderLine,
                    focusedTextColor = WhiteText,
                    unfocusedTextColor = WhiteText,
                    focusedContainerColor = ElevatedSurface,
                    unfocusedContainerColor = ElevatedSurface
                ),
                shape = RoundedCornerShape(14.dp)
            )
        }

        // Goals Description Input
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "Brief Description of Your Goals",
                color = WhiteText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = goals,
                onValueChange = { goals = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .testTag("goals_input"),
                placeholder = { Text("Describe what you want to achieve (e.g. job interviews, daily fluency, negotiation)...", color = CoolGray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NeonMint,
                    unfocusedBorderColor = BorderLine,
                    focusedTextColor = WhiteText,
                    unfocusedTextColor = WhiteText,
                    focusedContainerColor = ElevatedSurface,
                    unfocusedContainerColor = ElevatedSurface
                ),
                shape = RoundedCornerShape(14.dp)
            )
        }

        // Daily Practice Target Selection
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(
                text = "Daily Practice Target",
                color = WhiteText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                listOf(5, 10, 15, 20).forEach { mins ->
                    val isSelected = targetMins == mins
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NeonMint.copy(alpha = 0.15f) else Surface2)
                            .border(
                                1.5.dp,
                                if (isSelected) NeonMint else BorderLine,
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { targetMins = mins }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$mins min",
                            color = if (isSelected) NeonMint else CoolGray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Visual Progress Indicator Preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(ElevatedSurface)
                .border(1.dp, BorderLine, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daily Progress Tracker Preview",
                        color = LilacAccent,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Goal: $targetMins min/day",
                        color = NeonMint,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                LinearProgressIndicator(
                    progress = { 0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = NeonMint,
                    trackColor = Surface2
                )

                Text(
                    text = "⚡ Your streak begins at 0. Finish your first call with our AI persona to start your 1-day streak!",
                    color = CoolGray,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Start Button
        Button(
            onClick = {
                onSaveProfile(name, goals, targetMins)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("save_profile_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonMint,
                contentColor = DeepNightBlue
            ),
            shape = RoundedCornerShape(27.dp)
        ) {
            Icon(imageVector = Icons.Default.RocketLaunch, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Enter TalkFlow",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}
