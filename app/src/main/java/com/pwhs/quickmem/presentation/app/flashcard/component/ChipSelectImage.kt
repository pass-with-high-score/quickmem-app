package com.pwhs.quickmem.presentation.app.flashcard.component

import android.net.Uri
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.pwhs.quickmem.R

@Composable
fun ChipSelectImage(
    modifier: Modifier = Modifier,
    onUploadImage: (Uri) -> Unit,
    imageUri: Uri?,
    imageUrl: String?,
    onDeleteImage: () -> Unit,
    onChooseImage: () -> Unit,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    var isImageViewerOpen by remember { mutableStateOf(false) }

    InputChip(
        modifier = modifier,
        onClick = {
            if (imageUri != null || !imageUrl.isNullOrEmpty()) {
                isImageViewerOpen = true // Open image viewer when clicked
            } else {
                onChooseImage()
            }
        },
        selected = imageUri != null || !imageUrl.isNullOrEmpty(),
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (imageUri == null && !imageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Crop
                    )
                } else if (imageUri != null) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Crop,
                        onSuccess = { onUploadImage(imageUri) }
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = null,
                    )
                }

                Text(
                    text = stringResource(R.string.txt_term_image),
                    color = color,
                )
            }
        }
    )

    // Image Viewer Dialog
    if (isImageViewerOpen) {
        Dialog(onDismissRequest = { isImageViewerOpen = false }) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { isImageViewerOpen = false }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        AsyncImage(
                            model = imageUri ?: imageUrl,
                            contentDescription = stringResource(R.string.txt_full_image),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentScale = ContentScale.Fit
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                onDeleteImage() // Delete image when button is pressed
                                isImageViewerOpen = false // Close dialog
                            },
                            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                        ) {
                            Text(stringResource(R.string.txt_delete_image))
                        }
                    }
                }
            }
        }
    }
}