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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PracticeScenario
import com.example.data.model.ScenarioData
import com.example.data.model.ScenarioLevel
import com.example.ui.theme.AmberAlert
import com.example.ui.theme.BorderLine
import com.example.ui.theme.CoolGray
import com.example.ui.theme.DeepNightBlue
import com.example.ui.theme.ElevatedSurface
import com.example.ui.theme.LilacAccent
import com.example.ui.theme.NeonMint
import com.example.ui.theme.Surface2
import com.example.ui.theme.WhiteText

@Composable
fun PracticeScreen(
    onSelectScenario: (PracticeScenario) -> Unit
) {
    var selectedFilter by remember { mutableStateOf<ScenarioLevel?>(null) }
    val scrollState = rememberScrollState()

    val filteredList = remember(selectedFilter) {
        if (selectedFilter == null) ScenarioData.allScenarios
        else ScenarioData.allScenarios.filter { it.level == selectedFilter }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNightBlue)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Practice Scenarios",
            color = WhiteText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        // Filter chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterTabChip(
                label = "All",
                isSelected = selectedFilter == null,
                onClick = { selectedFilter = null }
            )
            FilterTabChip(
                label = "Basic",
                isSelected = selectedFilter == ScenarioLevel.BASIC,
                onClick = { selectedFilter = ScenarioLevel.BASIC }
            )
            FilterTabChip(
                label = "Intermediate",
                isSelected = selectedFilter == ScenarioLevel.INTERMEDIATE,
                onClick = { selectedFilter = ScenarioLevel.INTERMEDIATE }
            )
            FilterTabChip(
                label = "Advanced",
                isSelected = selectedFilter == ScenarioLevel.ADVANCED,
                onClick = { selectedFilter = ScenarioLevel.ADVANCED }
            )
        }

        // Scenario cards
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            filteredList.forEach { scenario ->
                PracticeScenarioListItem(
                    scenario = scenario,
                    onClick = { onSelectScenario(scenario) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun FilterTabChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) NeonMint.copy(alpha = 0.15f) else ElevatedSurface)
            .border(
                1.dp,
                if (isSelected) NeonMint else BorderLine,
                RoundedCornerShape(20.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) NeonMint else CoolGray,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun PracticeScenarioListItem(
    scenario: PracticeScenario,
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

    val levelColor = when (scenario.level) {
        ScenarioLevel.BASIC -> NeonMint
        ScenarioLevel.INTERMEDIATE -> LilacAccent
        ScenarioLevel.ADVANCED -> AmberAlert
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(ElevatedSurface)
            .border(1.dp, BorderLine, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
            .testTag("practice_item_${scenario.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Surface2),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = levelColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = scenario.title,
                    color = WhiteText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "with ${scenario.persona.name} · ${scenario.durationMinutes} min",
                    color = CoolGray,
                    fontSize = 12.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(levelColor.copy(alpha = 0.15f))
                    .border(1.dp, levelColor.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 7.dp, vertical = 3.dp)
            ) {
                Text(
                    text = scenario.level.label,
                    color = levelColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = CoolGray,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
