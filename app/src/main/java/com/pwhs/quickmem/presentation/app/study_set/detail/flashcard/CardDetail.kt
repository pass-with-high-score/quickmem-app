package com.pwhs.quickmem.presentation.app.study_set.detail.flashcard

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.components.ShowImageDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun CardDetail(
    modifier: Modifier = Modifier,
    front: String = "",
    back: String = "",
    isStarred: Boolean = true,
    onToggleStarClick: (Boolean) -> Unit = { },
    onMenuClick: () -> Unit = {},
    imageURL: String? = null,
    isOwner: Boolean = false,
    isAIGenerated: Boolean = false,
    color: Color = colorScheme.primary
) {
    // TextToSpeech state
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isImageViewerOpen by remember { mutableStateOf(false) }
    var definitionImageUri by remember { mutableStateOf("") }
    var isSpeaking by remember { mutableStateOf(false) }
    var startTTS by remember { mutableStateOf(false) }
    val tts = remember {
        TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                startTTS = true
            }
        }
    }

    if (startTTS) {
        tts.language = Locale.US
        startTTS = false
    }

    // Function to start/stop TTS
    fun toggleSpeech() {
        if (isSpeaking) {
            scope.launch {
                tts.stop()
            }
        } else {
            scope.launch {
                tts.speak(front, TextToSpeech.QUEUE_FLUSH, null, null)
                delay(1000)
                tts.speak(back, TextToSpeech.QUEUE_FLUSH, null, null)
                delay((front.length + back.length).toLong() * 60)
                isSpeaking = false
            }
        }
        isSpeaking = !isSpeaking
    }

    Card(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 3.dp,
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = CenterVertically
            ) {
                Text(text = front, modifier = Modifier.weight(1f))
                Row(
                    verticalAlignment = CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            toggleSpeech()
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_sound),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = when {
                                isSpeaking -> colorScheme.primary
                                else -> Color.Gray
                            }
                        )
                    }
                    if (isOwner) {
                        AnimatedContent(
                            targetState = isStarred,
                        ) { targetState ->
                            IconButton(
                                onClick = {
                                    onToggleStarClick(!targetState)
                                }
                            ) {
                                Icon(
                                    imageVector = if (targetState) Default.Star else Default.StarBorder,
                                    contentDescription = stringResource(R.string.txt_star),
                                    tint = Color(0xFFE0A800),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            Text(text = back, modifier = Modifier.padding(vertical = 8.dp))
            if (!imageURL.isNullOrEmpty()) {
                AsyncImage(
                    model = imageURL,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clickable {
                            isImageViewerOpen = true
                            definitionImageUri = imageURL
                        },
                    contentScale = ContentScale.Crop
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row {
                if (isAIGenerated) {
                    Icon(
                        painter = painterResource(R.drawable.ic_generative_ai),
                        contentDescription = stringResource(R.string.txt_ai_generated),
                        modifier = Modifier.size(30.dp),
                        tint = color
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(
                    onClick = {}
                ) {
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Default.MoreHoriz,
                            contentDescription = stringResource(R.string.txt_more_options)
                        )
                    }
                }
            }
        }
    }

    // Image Viewer Dialog
    if (isImageViewerOpen) {
        ShowImageDialog(
            definitionImageUri = definitionImageUri,
            onDismissRequest = {
                isImageViewerOpen = false
                definitionImageUri = ""
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }
}