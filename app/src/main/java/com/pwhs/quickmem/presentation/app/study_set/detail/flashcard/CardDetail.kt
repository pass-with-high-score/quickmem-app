package com.pwhs.quickmem.presentation.app.study_set.detail.flashcard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.components.ShowImageDialog

@Composable
fun CardDetail(
    modifier: Modifier = Modifier,
    index: Int = 0,
    flashcardId: String = "",
    front: String = "",
    back: String = "",
    termVoiceCode: String = "",
    definitionVoiceCode: String = "",
    onMenuClick: () -> Unit = {},
    definitionImageURL: String? = null,
    termImageURL: String? = null,
    isAIGenerated: Boolean = false,
    color: Color = colorScheme.primary,
    onGetSpeech: (
        flashcardId: String,
        term: String,
        definition: String,
        termVoiceCode: String,
        definitionVoiceCode: String,
        onTermSpeakStart: () -> Unit,
        onTermSpeakEnd: () -> Unit,
        onDefinitionSpeakStart: () -> Unit,
        onDefinitionSpeakEnd: () -> Unit
    ) -> Unit = { _, _, _, _, _, _, _, _, _ -> },
    isSpeaking: Boolean = false,
) {
    // TextToSpeech state
    var isImageViewerOpen by remember { mutableStateOf(false) }
    var imageSelectedUri by remember { mutableStateOf("") }
    var isTermSpeaking by remember { mutableStateOf(false) }
    var isDefinitionSpeaking by remember { mutableStateOf(false) }
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
                verticalAlignment = CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box {
                    Text(
                        text = "#${index + 1}",
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                IconButton(
                    onClick = {
                        onGetSpeech(
                            flashcardId,
                            front,
                            back,
                            termVoiceCode,
                            definitionVoiceCode,
                            {
                                isTermSpeaking = true
                            },
                            {
                                isTermSpeaking = false
                            },
                            {
                                isDefinitionSpeaking = true
                            },
                            {
                                isDefinitionSpeaking = false
                            }
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_sound),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = when {
                            (isTermSpeaking || isDefinitionSpeaking) && isSpeaking -> colorScheme.primary
                            else -> Color.Gray
                        }
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (!termImageURL.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(termImageURL)
                            .placeholder(R.drawable.ic_image_default)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clickable {
                                isImageViewerOpen = true
                                imageSelectedUri = termImageURL
                            },
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = front, modifier = Modifier.weight(1f),
                    color = when {
                        isTermSpeaking && isSpeaking -> colorScheme.primary
                        else -> Color.Black
                    }
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (!definitionImageURL.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(definitionImageURL)
                            .placeholder(R.drawable.ic_image_default)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clickable {
                                isImageViewerOpen = true
                                imageSelectedUri = definitionImageURL
                            },
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    text = back, modifier = Modifier.padding(vertical = 8.dp),
                    color = when {
                        isDefinitionSpeaking && isSpeaking -> colorScheme.primary
                        else -> Color.Black
                    }
                )
            }
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
            definitionImageUri = imageSelectedUri,
            onDismissRequest = {
                isImageViewerOpen = false
                imageSelectedUri = ""
            }
        )
    }
}

@Preview
@Composable
private fun CardDetailPreview() {
    MaterialTheme {
        CardDetail()
    }
}