package com.pwhs.quickmem.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun ShowImageDialog(
    modifier: Modifier = Modifier,
    definitionImageUri: String,
    onDismissRequest: () -> Unit,
    title: String = stringResource(id = R.string.txt_close),
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Dialog(onDismissRequest = {
        onDismissRequest()
    }) {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = Color.Transparent
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        onDismissRequest()
                    }
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = definitionImageUri,
                        contentDescription = stringResource(id = R.string.image_description),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = color
                        )
                    ) {
                        Text(text = title)
                    }
                }
            }
        }
    }
}