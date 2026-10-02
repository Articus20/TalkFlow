package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CallEngineState
import com.example.data.model.PracticeScenario
import com.example.ui.components.PersonaAvatar
import com.example.ui.components.VoiceWaveVisualizer
import com.example.ui.theme.AmberAlert
import com.example.ui.theme.BorderLine
import com.example.ui.theme.CoolGray
import com.example.ui.theme.CoralRed
import com.example.ui.theme.DeepNightBlue
import com.example.ui.theme.ElevatedSurface
import com.example.ui.theme.LilacAccent
import com.example.ui.theme.NeonMint
import com.example.ui.theme.Surface2
import com.example.ui.theme.Surface3
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.WhiteText
import com.example.voice.CoachTip
import com.example.voice.VoiceEngine

@Composable
fun ActiveCallScreen(
    scenario: PracticeScenario,
    engineState: CallEngineState,
    botEnglishText: String,
    botSpanishText: String,
    isTranslationVisible: Boolean,
    callSeconds: Long,
    costaRicaTime: String,
    adaptiveSpeed: Float,
    adaptiveVocabulary: String,
    coachTip: CoachTip?,
    userTranscriptHistory: List<String>,
    isMuted: Boolean,
    isSpeakerOn: Boolean,
    voiceEngine: VoiceEngine,
    onToggleTranslation: () -> Unit,
    onToggleMute: () -> Unit,
    onToggleSpeaker: () -> Unit,
    onUserSubmitAnswer: (String) -> Unit,
    onOpenPanicMode: () -> Unit,
    onEndCall: () -> Unit
) {
    var manualInputText by remember { mutableStateOf("") }
    var showEndCallConfirm by remember { mutableStateOf(false) }

    val rmsLevel by voiceEngine.currentRmsLevel.collectAsState()
    val partialLiveTranscript by voiceEngine.partialTranscript.collectAsState()
    val isUserSpeakingNow by voiceEngine.userSpeaking.collectAsState()

    // Sync partial speech transcript into composer field so the user sees their words in real-time
    LaunchedEffect(partialLiveTranscript) {
        if (partialLiveTranscript.isNotBlank()) {
            manualInputText = partialLiveTranscript
        }
    }

    val minutes = callSeconds / 60
    val seconds = callSeconds % 60
    val timerString = String.format("%02d:%02d", minutes, seconds)

    val scrollState = rememberScrollState()

    val pulseTransition = rememberInfiniteTransition(label = "liveDot")
    val dotAlpha by pulseTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dotAlpha"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNightBlue)
    ) {
        // Top Call Status Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Live Status indicator
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(NeonMint.copy(alpha = dotAlpha))
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "LIVE · EXCELLENT",
                    color = NeonMint,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            // Duration Timer
            Text(
                text = timerString,
                color = WhiteText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            // Costa Rica Time Chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Surface2)
                    .border(1.dp, BorderLine, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "🇨🇷 $costaRicaTime",
                    color = CoolGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Main Scrollable Call Area
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // AI Persona Avatar
            PersonaAvatar(
                engineState = engineState,
                size = 104.dp
            )

            // Persona Name & Role
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = scenario.persona.name,
                    color = WhiteText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = scenario.persona.role,
                    color = CoolGray,
                    fontSize = 12.sp
                )
            }

            // State Pill
            val stateLabel = when (engineState) {
                CallEngineState.SPEAKING -> "${scenario.persona.firstName.uppercase()} IS SPEAKING"
                CallEngineState.LISTENING -> if (isUserSpeakingNow) "YOU ARE SPEAKING..." else "LISTENING NOW"
                CallEngineState.WAITING -> "TAKE YOUR TIME"
                CallEngineState.THINKING -> "AI IS PROCESSING..."
                CallEngineState.DONE -> "CALL COMPLETED"
            }
            val stateColor = when (engineState) {
                CallEngineState.SPEAKING -> LilacAccent
                CallEngineState.LISTENING -> NeonMint
                CallEngineState.WAITING -> CoolGray
                CallEngineState.THINKING -> VibrantPurple
                CallEngineState.DONE -> NeonMint
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(stateColor.copy(alpha = 0.12f))
                    .border(1.dp, stateColor.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(stateColor)
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = stateLabel,
                        color = stateColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Central Interactive Voice Wave
            VoiceWaveVisualizer(
                engineState = engineState,
                rmsLevel = rmsLevel,
                modifier = Modifier.testTag("voice_wave_visualizer")
            )

            // Voice Wave Help Text
            Text(
                text = when (engineState) {
                    CallEngineState.SPEAKING -> "Listen closely — then respond naturally in your own words."
                    CallEngineState.LISTENING -> if (isUserSpeakingNow) "I'm listening — take your time to finish..." else "Speak naturally — I'll match your pace"
                    CallEngineState.WAITING -> "No rush. When you finish speaking, the AI will respond."
                    CallEngineState.THINKING -> "Analyzing response and adapting pace..."
                    CallEngineState.DONE -> "Great job! Tap End to review your fluency feedback."
                },
                color = if (engineState == CallEngineState.LISTENING) NeonMint else CoolGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            // Speed & Vocabulary Adaptive Metrics
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, BorderLine, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = null,
                            tint = NeonMint,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Speed: ",
                            color = CoolGray,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${String.format("%.1f", adaptiveSpeed)}×",
                            color = WhiteText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = LilacAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Vocabulary: ",
                            color = CoolGray,
                            fontSize = 12.sp
                        )
                        Text(
                            text = adaptiveVocabulary,
                            color = WhiteText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Live Subtitles Card with Tap-to-Translate
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, BorderLine, RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FormatQuote,
                                contentDescription = null,
                                tint = NeonMint,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${scenario.persona.firstName.uppercase()} · LIVE SUBTITLES",
                                color = NeonMint,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Text(
                            text = if (isTranslationVisible) "Tap to hide" else "Tap to translate",
                            color = CoolGray,
                            fontSize = 11.sp,
                            modifier = Modifier
                                .clickable { onToggleTranslation() }
                                .padding(4.dp)
                        )
                    }

                    // Bot English Utterance
                    Text(
                        text = "\"$botEnglishText\"",
                        color = WhiteText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 21.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleTranslation() }
                    )

                    // Expandable Spanish Translation Box
                    AnimatedVisibility(
                        visible = isTranslationVisible,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(NeonMint.copy(alpha = 0.08f))
                                .border(1.dp, NeonMint.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Translate,
                                    contentDescription = null,
                                    tint = NeonMint,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = botSpanishText,
                                    color = NeonMint,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    // Coach Tip Banner if user previously responded
                    coachTip?.let { tip ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (tip.isPositive) NeonMint.copy(alpha = 0.1f) else AmberAlert.copy(alpha = 0.12f))
                                .border(
                                    1.dp,
                                    if (tip.isPositive) NeonMint.copy(alpha = 0.4f) else AmberAlert.copy(alpha = 0.5f),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = if (tip.isPositive) Icons.Default.CheckCircle else Icons.Default.Info,
                                    contentDescription = null,
                                    tint = if (tip.isPositive) NeonMint else AmberAlert,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = tip.tip,
                                    color = if (tip.isPositive) NeonMint else AmberAlert,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // Answer composer row (Type or speak with instant submit button)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = manualInputText,
                            onValueChange = { manualInputText = it },
                            placeholder = { Text("Say it, or type your answer...", color = CoolGray, fontSize = 13.sp) },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("reply_input_field"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = NeonMint,
                                unfocusedBorderColor = BorderLine,
                                focusedTextColor = WhiteText,
                                unfocusedTextColor = WhiteText,
                                focusedContainerColor = Surface2,
                                unfocusedContainerColor = Surface2
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        // Done / Send answer button
                        IconButton(
                            onClick = {
                                if (manualInputText.isNotBlank()) {
                                    val text = manualInputText
                                    manualInputText = ""
                                    onUserSubmitAnswer(text)
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(NeonMint)
                                .testTag("send_reply_button")
                        ) {
                            Icon(
                                imageVector = if (manualInputText.isNotBlank()) Icons.Default.Send else Icons.Default.Done,
                                contentDescription = "Send or Done Speaking",
                                tint = DeepNightBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // PANIC BUTTON
            Button(
                onClick = onOpenPanicMode,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("panic_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CoralRed.copy(alpha = 0.12f),
                    contentColor = CoralRed
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(CoralRed)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CrisisAlert,
                    contentDescription = null,
                    tint = CoralRed,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "PANIC · Get instant help",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoralRed
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Bottom Call Controls Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(ElevatedSurface)
                .border(1.dp, BorderLine)
                .padding(vertical = 12.dp, horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mute
                CallControlItem(
                    label = if (isMuted) "Unmute" else "Mute",
                    icon = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                    isActive = isMuted,
                    activeColor = AmberAlert,
                    testTag = "call_mute_button",
                    onClick = onToggleMute
                )

                // Speaker
                CallControlItem(
                    label = "Speaker",
                    icon = Icons.Default.VolumeUp,
                    isActive = isSpeakerOn,
                    activeColor = NeonMint,
                    testTag = "call_speaker_button",
                    onClick = onToggleSpeaker
                )

                // Video off (Disabled)
                CallControlItem(
                    label = "Video off",
                    icon = Icons.Default.VideocamOff,
                    isActive = false,
                    activeColor = CoolGray,
                    enabled = false,
                    testTag = "call_video_off_button",
                    onClick = {}
                )

                // End Call
                CallControlItem(
                    label = "End",
                    icon = Icons.Default.Phone,
                    isActive = true,
                    activeColor = CoralRed,
                    isDestructive = true,
                    testTag = "call_end_button",
                    onClick = { showEndCallConfirm = true }
                )
            }
        }
    }

    // End Call Confirmation Dialog
    if (showEndCallConfirm) {
        AlertDialog(
            onDismissRequest = { showEndCallConfirm = false },
            containerColor = ElevatedSurface,
            title = {
                Text(
                    text = "End Call & View Feedback?",
                    color = WhiteText,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "Your fluency score, phoneme pronunciation map, and idiom recommendations will be calculated.",
                    color = CoolGray,
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showEndCallConfirm = false
                        onEndCall()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoralRed,
                        contentColor = WhiteText
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("End Call", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndCallConfirm = false }) {
                    Text("Keep Practicing", color = CoolGray)
                }
            }
        )
    }
}

@Composable
private fun CallControlItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isActive: Boolean,
    activeColor: Color,
    isDestructive: Boolean = false,
    enabled: Boolean = true,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    if (isDestructive) CoralRed
                    else if (isActive) activeColor.copy(alpha = 0.2f)
                    else Surface2
                )
                .border(
                    1.dp,
                    if (isDestructive) CoralRed else if (isActive) activeColor else BorderLine,
                    CircleShape
                )
                .clickable(enabled = enabled) { onClick() }
                .testTag(testTag),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isDestructive) WhiteText else if (isActive) activeColor else CoolGray,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = label,
            color = if (enabled) CoolGray else CoolGray.copy(alpha = 0.5f),
            fontSize = 11.sp
        )
    }
}
