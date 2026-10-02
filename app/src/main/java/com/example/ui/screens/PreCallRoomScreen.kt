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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CallEngineState
import com.example.data.model.CoachingStrictness
import com.example.data.model.PracticeScenario
import com.example.ui.components.PersonaAvatar
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
import com.example.voice.VoiceEngine
import kotlinx.coroutines.delay

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PreCallRoomScreen(
    scenario: PracticeScenario,
    strictness: CoachingStrictness,
    selectedKeywords: Set<String>,
    voiceEngine: VoiceEngine,
    onBack: () -> Unit,
    onStrictnessChange: (CoachingStrictness) -> Unit,
    onToggleKeyword: (String) -> Unit,
    onAddKeyword: (String) -> Unit,
    onStartCall: () -> Unit
) {
    var showAddKeywordDialog by remember { mutableStateOf(false) }
    var customKeywordText by remember { mutableStateOf("") }
    var isTestingAudio by remember { mutableStateOf(false) }

    val rmsLevel by voiceEngine.currentRmsLevel.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(isTestingAudio) {
        if (isTestingAudio) {
            voiceEngine.startListening()
            voiceEngine.speak("Audio test active. Say something and watch the level meter.", 1.0f)
            delay(5000L)
            voiceEngine.stopListening()
            isTestingAudio = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNightBlue)
    ) {
        // Topbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, BorderLine, RoundedCornerShape(12.dp))
                    .testTag("precall_back_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to Dashboard",
                    tint = WhiteText
                )
            }

            Text(
                text = "Pre-Call Room",
                color = WhiteText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = { /* options */ },
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, BorderLine, RoundedCornerShape(12.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    tint = CoolGray
                )
            }
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // Persona Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                VibrantPurple.copy(alpha = 0.35f),
                                ElevatedSurface
                            )
                        )
                    )
                    .border(1.dp, LilacAccent.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PersonaAvatar(
                        engineState = CallEngineState.WAITING,
                        size = 72.dp
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(NeonMint.copy(alpha = 0.15f))
                                    .border(1.dp, NeonMint.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "ONLINE",
                                    color = NeonMint,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = scenario.persona.accent,
                                color = CoolGray,
                                fontSize = 12.sp
                            )
                        }

                        Text(
                            text = scenario.persona.name,
                            color = WhiteText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = scenario.persona.role,
                            color = LilacAccent,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Coaching Strictness Selector
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Coaching strictness",
                        color = WhiteText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Selected: ${strictness.title}",
                        color = CoolGray,
                        fontSize = 12.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CoachingStrictness.values().forEach { option ->
                        val isSelected = strictness == option
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .background(if (isSelected) NeonMint.copy(alpha = 0.12f) else ElevatedSurface)
                                .border(
                                    1.5.dp,
                                    if (isSelected) NeonMint else BorderLine,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onStrictnessChange(option) }
                                .padding(12.dp)
                                .testTag("strictness_${option.name.lowercase()}")
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = option.title,
                                    color = if (isSelected) NeonMint else WhiteText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = option.subtitle,
                                    color = CoolGray,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }

            // Target Keywords Chips
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Target keywords",
                        color = WhiteText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${selectedKeywords.size} selected",
                        color = NeonMint,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                val allKeywords = remember(scenario, selectedKeywords) {
                    (scenario.targetKeywords + scenario.extraKeywords + selectedKeywords).distinct()
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    allKeywords.forEach { kw ->
                        val isSelected = selectedKeywords.contains(kw)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isSelected) VibrantPurple.copy(alpha = 0.35f) else ElevatedSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) LilacAccent.copy(alpha = 0.7f) else BorderLine,
                                    RoundedCornerShape(18.dp)
                                )
                                .clickable { onToggleKeyword(kw) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                                .testTag("keyword_chip_$kw")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = if (isSelected) LilacAccent else CoolGray,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "#$kw",
                                    color = if (isSelected) WhiteText else CoolGray,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        }
                    }

                    // Add Custom Keyword Chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(Surface2)
                            .border(1.dp, NeonMint.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
                            .clickable { showAddKeywordDialog = true }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .testTag("add_keyword_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add custom keyword",
                                tint = NeonMint,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Add keyword",
                                color = NeonMint,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            // Adaptive Voice Engine Note
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, NeonMint.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(NeonMint.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = NeonMint,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = "Adaptive voice engine on",
                            color = NeonMint,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Starts at 0.95× speed, then dynamically adjusts pace and vocabulary complexity based on your fluency.",
                            color = CoolGray,
                            fontSize = 12.sp,
                            lineHeight = 17.sp
                        )
                    }
                }
            }

            // Microphone Readiness & Audio Test
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, BorderLine, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            tint = NeonMint,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = "Microphone ready",
                                color = WhiteText,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = if (isTestingAudio) "Listening to audio level..." else "Zero latency engine configured",
                                color = if (isTestingAudio) NeonMint else CoolGray,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Button(
                        onClick = { isTestingAudio = !isTestingAudio },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isTestingAudio) NeonMint else Surface2,
                            contentColor = if (isTestingAudio) DeepNightBlue else WhiteText
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("test_audio_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isTestingAudio) "TESTING..." else "TEST AUDIO",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Bottom CTA
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            DeepNightBlue.copy(alpha = 0.5f),
                            DeepNightBlue
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onStartCall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .testTag("start_call_with_persona_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonMint,
                        contentColor = DeepNightBlue
                    ),
                    shape = RoundedCornerShape(27.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Start call with ${scenario.persona.firstName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Conversation audio is processed privately on your device",
                    color = CoolGray,
                    fontSize = 11.sp
                )
            }
        }
    }

    // Add Custom Keyword Dialog
    if (showAddKeywordDialog) {
        AlertDialog(
            onDismissRequest = { showAddKeywordDialog = false },
            containerColor = ElevatedSurface,
            title = {
                Text(
                    text = "Add Target Keyword",
                    color = WhiteText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                OutlinedTextField(
                    value = customKeywordText,
                    onValueChange = { customKeywordText = it },
                    placeholder = { Text("e.g. leverage, metric, synergy", color = CoolGray) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonMint,
                        unfocusedBorderColor = BorderLine,
                        focusedTextColor = WhiteText,
                        unfocusedTextColor = WhiteText
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (customKeywordText.isNotBlank()) {
                            onAddKeyword(customKeywordText.trim())
                            customKeywordText = ""
                        }
                        showAddKeywordDialog = false
                    }
                ) {
                    Text("Add", color = NeonMint, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddKeywordDialog = false }) {
                    Text("Cancel", color = CoolGray)
                }
            }
        )
    }
}
