package com.pwhs.quickmem.presentation.app.flashcard.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.pwhs.quickmem.R

@Composable
fun CardSelectImage(
    modifier: Modifier = Modifier,
    onUploadImage: (Uri) -> Unit,
    definitionImageUri: Uri?,
    definitionImageUrl: String?,
    onDeleteImage: () -> Unit,
    onChooseImage: () -> Unit,
) {
    // State for showing the image viewer dialog
    var isImageViewerOpen by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            focusedElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = {
            if (definitionImageUri != null || !definitionImageUrl.isNullOrEmpty()) {
                isImageViewerOpen = true // Open image viewer when clicked
            } else {
                onChooseImage()
            }
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (definitionImageUri == null && !definitionImageUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = definitionImageUrl,
                        contentDescription = stringResource(R.string.txt_image_for_definition),
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Crop
                    )
                } else if (definitionImageUri != null) {
                    AsyncImage(
                        model = definitionImageUri,
                        contentDescription = stringResource(R.string.txt_image_for_definition),
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Crop,
                        onSuccess = { onUploadImage(definitionImageUri) }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_add_image),
                        contentDescription = stringResource(R.string.txt_add_image_to_definition),
                        modifier = Modifier.size(120.dp),
                        colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.5f
                            )
                        ),
                        contentScale = ContentScale.Crop
                    )
                }
                Text(
                    stringResource(R.string.txt_image_for_definition),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }

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
                            model = definitionImageUri ?: definitionImageUrl,
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