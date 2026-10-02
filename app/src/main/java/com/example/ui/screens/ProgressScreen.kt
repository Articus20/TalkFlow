package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.CallHistoryEntity
import com.example.data.local.SavedVocabEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.VocabWord
import com.example.ui.theme.AmberAlert
import com.example.ui.theme.BorderLine
import com.example.ui.theme.CoolGray
import com.example.ui.theme.CoralRed
import com.example.ui.theme.DeepNightBlue
import com.example.ui.theme.ElevatedSurface
import com.example.ui.theme.LilacAccent
import com.example.ui.theme.NeonMint
import com.example.ui.theme.Surface2
import com.example.ui.theme.WhiteText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProgressScreen(
    userProfile: UserProfileEntity?,
    callHistory: List<CallHistoryEntity>,
    savedVocabulary: List<SavedVocabEntity>,
    costaRicaTime: String,
    onEditProfile: () -> Unit,
    onRemoveVocabWord: (VocabWord) -> Unit,
    onClearAllData: () -> Unit
) {
    var showClearConfirm by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    val totalCalls = callHistory.size
    val totalMinutes = callHistory.sumOf { (it.durationSeconds / 60).toInt() }
    val avgScore = if (totalCalls > 0) callHistory.sumOf { it.fluencyScore } / totalCalls else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNightBlue)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Your Progress",
            color = WhiteText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        // User Profile & Goals Card
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(NeonMint.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = NeonMint,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Column {
                            Text(
                                text = userProfile?.username ?: "Maya",
                                color = WhiteText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Goal: ${userProfile?.dailyTargetMinutes ?: 10} min/day · Streak: ${userProfile?.currentStreak ?: 0} days",
                                color = CoolGray,
                                fontSize = 11.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = onEditProfile,
                        modifier = Modifier.testTag("edit_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Goals",
                            tint = NeonMint
                        )
                    }
                }

                if (!userProfile?.goalsDescription.isNullOrBlank()) {
                    Text(
                        text = "\"${userProfile?.goalsDescription}\"",
                        color = LilacAccent,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Summary Statistics (Calls, Minutes, Avg Score)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StatBox(
                label = "Calls",
                value = "$totalCalls",
                modifier = Modifier.weight(1f)
            )
            StatBox(
                label = "Minutes",
                value = "$totalMinutes",
                modifier = Modifier.weight(1f)
            )
            StatBox(
                label = "Avg Score",
                value = if (totalCalls > 0) "$avgScore" else "—",
                modifier = Modifier.weight(1f)
            )
        }

        // Call History Section
        Text(
            text = "Session History",
            color = WhiteText,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        if (callHistory.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, BorderLine, RoundedCornerShape(12.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No completed calls yet. Finish your first call to start your streak and track fluency scores here.",
                    color = CoolGray,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                callHistory.forEach { call ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(ElevatedSurface)
                            .border(1.dp, BorderLine, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = call.scenarioTitle,
                                    color = WhiteText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${call.dateFormatted} · ${(call.durationSeconds / 60).coerceAtLeast(1)} min · ${call.turnsCount} turns",
                                    color = CoolGray,
                                    fontSize = 11.sp
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NeonMint.copy(alpha = 0.15f))
                                    .border(1.dp, NeonMint.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${call.fluencyScore}",
                                    color = NeonMint,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Saved Vocabulary Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Saved Vocabulary Bank",
                color = WhiteText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${savedVocabulary.size} saved",
                color = NeonMint,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (savedVocabulary.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, BorderLine, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Bookmark words during feedback after any call to build your personal vocabulary list.",
                    color = CoolGray,
                    fontSize = 12.sp
                )
            }
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                savedVocabulary.forEach { item ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Surface2)
                            .border(1.dp, BorderLine, RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = NeonMint,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = item.word,
                                color = WhiteText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove word",
                                tint = CoolGray,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        onRemoveVocabWord(VocabWord(item.word, item.translationSpanish))
                                    }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Reset Data Button
        Button(
            onClick = { showClearConfirm = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Surface2,
                contentColor = CoralRed
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = CoralRed,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Reset All Profile & History Data",
                fontSize = 13.sp,
                color = CoralRed
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            containerColor = ElevatedSurface,
            title = {
                Text(
                    text = "Reset all TalkFlow data?",
                    color = WhiteText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "This will erase your streak, call history, profile goals, and saved vocabulary from this device.",
                    color = CoolGray,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showClearConfirm = false
                        onClearAllData()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CoralRed)
                ) {
                    Text("Delete Everything", color = WhiteText)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text("Cancel", color = CoolGray)
                }
            }
        )
    }
}

@Composable
private fun StatBox(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(ElevatedSurface)
            .border(1.dp, BorderLine, RoundedCornerShape(14.dp))
            .padding(14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                color = WhiteText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                color = CoolGray,
                fontSize = 12.sp
            )
        }
    }
}
