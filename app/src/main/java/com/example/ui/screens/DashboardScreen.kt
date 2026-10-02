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
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserProfileEntity
import com.example.data.model.PracticeScenario
import com.example.data.model.ScenarioData
import com.example.data.model.ScenarioLevel
import com.example.ui.components.StreakProgressCard
import com.example.ui.theme.AmberAlert
import com.example.ui.theme.BorderLine
import com.example.ui.theme.CoolGray
import com.example.ui.theme.DeepNightBlue
import com.example.ui.theme.ElevatedSurface
import com.example.ui.theme.LilacAccent
import com.example.ui.theme.NeonMint
import com.example.ui.theme.Surface2
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.WhiteText

@Composable
fun DashboardScreen(
    userProfile: UserProfileEntity?,
    greeting: String,
    costaRicaTime: String,
    onSelectScenario: (PracticeScenario) -> Unit,
    onViewAllScenarios: () -> Unit,
    onNotificationClick: () -> Unit
) {
    val username = userProfile?.username ?: "Maya"
    val streak = userProfile?.currentStreak ?: 0
    val bestStreak = userProfile?.bestStreak ?: 0
    val todayMins = userProfile?.todayMinutesPracticed ?: 0
    val targetMins = userProfile?.dailyTargetMinutes ?: 10

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNightBlue)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Dashboard Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text(
                    text = "$greeting, ${username.uppercase()}",
                    color = NeonMint,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Ready to speak?",
                    color = WhiteText,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = onNotificationClick,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(ElevatedSurface)
                    .border(1.dp, BorderLine, CircleShape)
                    .testTag("notifications_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = CoolGray
                )
            }
        }

        // Streak Progress Card with 0-streak initiation and Costa Rica timezone
        StreakProgressCard(
            streakCount = streak,
            bestStreak = bestStreak,
            todayMinutes = todayMins,
            targetMinutes = targetMins,
            costaRicaTime = costaRicaTime
        )

        // Quick Call Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            NeonMint.copy(alpha = 0.12f),
                            ElevatedSurface
                        )
                    )
                )
                .border(1.dp, NeonMint.copy(alpha = 0.55f), RoundedCornerShape(18.dp))
                .padding(18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Quick Call",
                            color = WhiteText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Speed + vocabulary adapt live",
                            color = NeonMint,
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonMint.copy(alpha = 0.15f))
                            .border(1.dp, NeonMint.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "AI READY",
                            color = NeonMint,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Button(
                    onClick = { onSelectScenario(ScenarioData.interview) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("quick_call_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonMint,
                        contentColor = DeepNightBlue
                    ),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Start a 5-minute call",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Practice Scenarios Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Practice scenarios",
                color = WhiteText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View all",
                color = NeonMint,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clickable { onViewAllScenarios() }
                    .padding(vertical = 4.dp)
            )
        }

        // Scenario Matrix: Basic, Intermediate, Advanced
        ScenarioGroupSection(
            levelTitle = "BASIC",
            levelColor = NeonMint,
            description = "Clear, everyday exchanges",
            scenarios = listOf(ScenarioData.coffee, ScenarioData.tickets),
            onSelectScenario = onSelectScenario
        )

        ScenarioGroupSection(
            levelTitle = "INTERMEDIATE",
            levelColor = LilacAccent,
            description = "Nuance under pressure",
            scenarios = listOf(ScenarioData.interview, ScenarioData.hotel),
            onSelectScenario = onSelectScenario
        )

        ScenarioGroupSection(
            levelTitle = "ADVANCED",
            levelColor = AmberAlert,
            description = "Persuade with precision",
            scenarios = listOf(ScenarioData.negotiation, ScenarioData.debate),
            onSelectScenario = onSelectScenario
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ScenarioGroupSection(
    levelTitle: String,
    levelColor: androidx.compose.ui.graphics.Color,
    description: String,
    scenarios: List<PracticeScenario>,
    onSelectScenario: (PracticeScenario) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ElevatedSurface)
            .border(1.dp, BorderLine, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(levelColor.copy(alpha = 0.15f))
                        .border(1.dp, levelColor.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 7.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = levelTitle,
                        color = levelColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = description,
                    color = CoolGray,
                    fontSize = 12.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                scenarios.forEach { scenario ->
                    ScenarioCardTile(
                        scenario = scenario,
                        modifier = Modifier.weight(1f),
                        onClick = { onSelectScenario(scenario) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ScenarioCardTile(
    scenario: PracticeScenario,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val icon: ImageVector = when (scenario.id) {
        "coffee" -> Icons.Default.EmojiFoodBeverage
        "tickets" -> Icons.Default.ConfirmationNumber
        "interview" -> Icons.Default.BusinessCenter
        "hotel" -> Icons.Default.Hotel
        "negotiation" -> Icons.Default.Gavel
        "debate" -> Icons.Default.Chat
        else -> Icons.Default.Chat
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Surface2)
            .border(1.dp, BorderLine, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp)
            .testTag("scenario_tile_${scenario.id}")
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(NeonMint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = NeonMint,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column {
                Text(
                    text = scenario.title,
                    color = WhiteText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 17.sp
                )
                Text(
                    text = "${scenario.durationMinutes} min",
                    color = CoolGray,
                    fontSize = 11.sp
                )
            }
        }
    }
}
