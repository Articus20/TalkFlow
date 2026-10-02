package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PanicSuggestion
import com.example.ui.theme.AmberAlert
import com.example.ui.theme.BorderLine
import com.example.ui.theme.CoolGray
import com.example.ui.theme.CoralRed
import com.example.ui.theme.DeepNightBlue
import com.example.ui.theme.ElevatedSurface
import com.example.ui.theme.LilacAccent
import com.example.ui.theme.NeonMint
import com.example.ui.theme.Surface2
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.WhiteText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanicModeSheet(
    isOpen: Boolean,
    currentSuggestion: PanicSuggestion,
    spanishInput: String,
    translatedEnglish: String,
    personaFirstName: String,
    onSpanishInputChange: (String) -> Unit,
    onPreviewAudio: (String) -> Unit,
    onUseResponse: (String) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = DeepNightBlue,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(42.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CoolGray.copy(alpha = 0.4f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sheet Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CrisisAlert,
                        contentDescription = null,
                        tint = CoralRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "Panic Mode",
                            color = WhiteText,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Breathe — the call remains live.",
                            color = CoolGray,
                            fontSize = 12.sp
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(ElevatedSurface)
                        .testTag("panic_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Panic Mode",
                        tint = CoolGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Live Spanish -> English Translation Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(ElevatedSurface)
                    .border(1.dp, NeonMint.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LIVE · SPANISH → ENGLISH",
                            color = NeonMint,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Real-time translator",
                            color = CoolGray,
                            fontSize = 11.sp
                        )
                    }

                    // User editable Spanish phrase
                    OutlinedTextField(
                        value = spanishInput,
                        onValueChange = onSpanishInputChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Escribe o di tu idea en español...", color = CoolGray, fontSize = 13.sp) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonMint,
                            unfocusedBorderColor = BorderLine,
                            focusedTextColor = LilacAccent,
                            unfocusedTextColor = LilacAccent,
                            focusedContainerColor = Surface2,
                            unfocusedContainerColor = Surface2
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // English Translation Result
                    Text(
                        text = "\"${translatedEnglish.ifBlank { currentSuggestion.translatedEnglish }}\"",
                        color = WhiteText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 22.sp
                    )
                }
            }

            // Suggestions Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Choose your response",
                    color = WhiteText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Tap speaker to preview",
                    color = CoolGray,
                    fontSize = 11.sp
                )
            }

            // 1. Conservative Tier
            ResponseTierCard(
                tierLabel = "CONSERVATIVE",
                tierColor = NeonMint,
                badgeBg = NeonMint.copy(alpha = 0.12f),
                text = currentSuggestion.conservative,
                onPreview = { onPreviewAudio(currentSuggestion.conservative) },
                onUse = { onUseResponse(currentSuggestion.conservative) }
            )

            // 2. Natural Tier
            ResponseTierCard(
                tierLabel = "NATURAL",
                tierColor = LilacAccent,
                badgeBg = VibrantPurple.copy(alpha = 0.25f),
                text = currentSuggestion.natural,
                onPreview = { onPreviewAudio(currentSuggestion.natural) },
                onUse = { onUseResponse(currentSuggestion.natural) }
            )

            // 3. Advanced Tier
            ResponseTierCard(
                tierLabel = "ADVANCED",
                tierColor = AmberAlert,
                badgeBg = AmberAlert.copy(alpha = 0.15f),
                text = currentSuggestion.advanced,
                onPreview = { onPreviewAudio(currentSuggestion.advanced) },
                onUse = { onUseResponse(currentSuggestion.advanced) }
            )

            // Privacy Guarantee Notice
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(CoralRed.copy(alpha = 0.08f))
                    .border(1.dp, CoralRed.copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = CoralRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Suggestions stay private. $personaFirstName only hears what you speak.",
                        color = CoolGray,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun ResponseTierCard(
    tierLabel: String,
    tierColor: Color,
    badgeBg: Color,
    text: String,
    onPreview: () -> Unit,
    onUse: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(ElevatedSurface)
            .border(1.dp, tierColor.copy(alpha = 0.45f), RoundedCornerShape(14.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBg)
                        .border(1.dp, tierColor.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = tierLabel,
                        color = tierColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onPreview,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Listen to preview",
                            tint = CoolGray,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Surface2)
                            .clickable { onUse() }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "USE THIS",
                            color = WhiteText,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = NeonMint,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Text(
                text = text,
                color = WhiteText,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}
