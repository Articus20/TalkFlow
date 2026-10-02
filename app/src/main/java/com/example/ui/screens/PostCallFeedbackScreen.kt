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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.SavedVocabEntity
import com.example.data.model.CallFeedback
import com.example.data.model.PhonemeScore
import com.example.data.model.VocabWord
import com.example.ui.components.RadarChart
import com.example.ui.components.RadarDimension
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
import com.example.voice.VoiceEngine

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PostCallFeedbackScreen(
    feedback: CallFeedback,
    savedVocabulary: List<SavedVocabEntity>,
    voiceEngine: VoiceEngine,
    onToggleSaveVocab: (VocabWord) -> Unit,
    onClose: () -> Unit
) {
    var showDrillDialog by remember { mutableStateOf(false) }
    var selectedDrillPhoneme by remember { mutableStateOf<PhonemeScore?>(feedback.phonemes.firstOrNull()) }

    val scrollState = rememberScrollState()

    val radarDimensions = listOf(
        RadarDimension("Pace", feedback.paceScore),
        RadarDimension("Clarity", feedback.clarityScore),
        RadarDimension("Vocab", feedback.vocabScore),
        RadarDimension("Confidence", feedback.confidenceScore),
        RadarDimension("Keywords", feedback.keywordScore)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNightBlue)
    ) {
        // Topbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ElevatedSurface)
                    .testTag("feedback_close_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close feedback",
                    tint = CoolGray,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = "Call Feedback",
                color = WhiteText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(NeonMint.copy(alpha = 0.15f))
                    .border(1.dp, NeonMint.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "SAVED",
                    color = NeonMint,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Scrollable Body
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Fluency Score + Radar Chart Hero Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                NeonMint.copy(alpha = 0.1f),
                                ElevatedSurface
                            )
                        )
                    )
                    .border(1.dp, NeonMint.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
                    .padding(18.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Text(
                                text = "FLUENCY SCORE",
                                color = NeonMint,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "${feedback.fluencyScore}",
                                    color = WhiteText,
                                    fontSize = 52.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 56.sp
                                )
                                Text(
                                    text = " / 100",
                                    color = CoolGray,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }
                            Text(
                                text = "↑ ${feedback.deltaFromLast} from last call",
                                color = NeonMint,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Embedded Radar Chart
                        RadarChart(
                            dimensions = radarDimensions,
                            modifier = Modifier.width(160.dp)
                        )
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = feedback.headline,
                            color = WhiteText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = feedback.subtitle,
                            color = CoolGray,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    }
                }
            }

            // Streak advancement banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Surface2)
                    .border(1.dp, AmberAlert.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = AmberAlert,
                        modifier = Modifier.size(28.dp)
                    )
                    Column {
                        Text(
                            text = if (feedback.isFirstCallStreakStarter) "Streak Started: 1 Day!" else "Streak Continued!",
                            color = AmberAlert,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "You completed your speaking session. Practice again tomorrow in Costa Rica timezone to keep the fire going!",
                            color = CoolGray,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                }
            }

            // Phoneme Pronunciation Map Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, BorderLine, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Phoneme map",
                            color = WhiteText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "PRACTICE SOUNDS",
                            color = NeonMint,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    selectedDrillPhoneme = feedback.phonemes.firstOrNull()
                                    showDrillDialog = true
                                }
                                .padding(4.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        feedback.phonemes.forEach { phoneme ->
                            val isLow = phoneme.percentage < 75
                            val borderColor = if (isLow) CoralRed else NeonMint.copy(alpha = 0.5f)
                            val textColor = if (isLow) CoralRed else NeonMint

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Surface2)
                                    .border(1.dp, borderColor, RoundedCornerShape(12.dp))
                                    .clickable {
                                        selectedDrillPhoneme = phoneme
                                        showDrillDialog = true
                                    }
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = phoneme.symbol,
                                            color = textColor,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "${phoneme.percentage}%",
                                            color = textColor,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = phoneme.exampleWord,
                                        color = CoolGray,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Sound More Native (Native Idioms)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, BorderLine, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Sound more native",
                            color = WhiteText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(VibrantPurple.copy(alpha = 0.25f))
                                .border(1.dp, LilacAccent.copy(alpha = 0.35f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 7.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${feedback.nativeIdioms.size} IDIOMS",
                                color = LilacAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    feedback.nativeIdioms.forEach { idiom ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(VibrantPurple.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatQuote,
                                    contentDescription = null,
                                    tint = LilacAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "\"${idiom.phrase}\"",
                                    color = WhiteText,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = idiom.usage,
                                    color = CoolGray,
                                    fontSize = 11.sp,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            }

            // Saved Vocabulary Bank
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, BorderLine, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Saved vocabulary",
                            color = WhiteText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        val savedCount = feedback.targetWords.count { word ->
                            savedVocabulary.any { it.word == word.word }
                        }
                        Text(
                            text = "$savedCount SAVED",
                            color = NeonMint,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        feedback.targetWords.forEach { wordItem ->
                            val isSaved = savedVocabulary.any { it.word == wordItem.word }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSaved) NeonMint.copy(alpha = 0.15f) else Surface2)
                                    .border(
                                        1.dp,
                                        if (isSaved) NeonMint.copy(alpha = 0.6f) else BorderLine,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { onToggleSaveVocab(wordItem) }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = null,
                                        tint = if (isSaved) NeonMint else CoolGray,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = wordItem.word,
                                        color = if (isSaved) NeonMint else WhiteText,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Recommended Next Drill Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, NeonMint.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .clickable {
                        selectedDrillPhoneme = feedback.phonemes.firstOrNull()
                        showDrillDialog = true
                    }
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeonMint.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = NeonMint,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "RECOMMENDED NEXT",
                            color = NeonMint,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Practice /θ/ for 3 minutes",
                            color = WhiteText,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Focused drill · think, thorough, growth",
                            color = CoolGray,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = CoolGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Done / Return to Dashboard Button
            Button(
                onClick = onClose,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("done_feedback_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonMint,
                    contentColor = DeepNightBlue
                ),
                shape = RoundedCornerShape(26.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Done · Back to Dashboard",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Pronunciation Sound Drill Dialog
    if (showDrillDialog && selectedDrillPhoneme != null) {
        val sound = selectedDrillPhoneme!!
        val words = when (sound.symbol) {
            "/θ/" -> listOf("think", "thorough", "growth", "three", "method", "authority")
            "/r/" -> listOf("role", "really", "result", "remember", "reply", "priority")
            "/v/" -> listOf("value", "very", "improve", "overview", "involve", "leverage")
            else -> listOf("think", "thorough", "growth")
        }

        AlertDialog(
            onDismissRequest = { showDrillDialog = false },
            containerColor = ElevatedSurface,
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Practice Sound ${sound.symbol}",
                        color = WhiteText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(${sound.percentage}%)",
                        color = NeonMint,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Tap any word to listen to its native pronunciation, then repeat it out loud:",
                        color = CoolGray,
                        fontSize = 12.sp
                    )

                    words.forEach { word ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Surface2)
                                .clickable { voiceEngine.speak(word, 0.85f) }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = word,
                                color = WhiteText,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Listen to $word",
                                tint = NeonMint,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDrillDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonMint,
                        contentColor = DeepNightBlue
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Complete Drill", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
