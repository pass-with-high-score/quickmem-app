package com.pwhs.quickmem.presentation.app.profile.change_avatar.component

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pwhs.quickmem.R
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun AvatarItem(
    avatarUrl: String? = null,
    avatarUri: Uri? = null,
    @DrawableRes imageId: Int? = null,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onSelected() }
            .border(
                width = if (isSelected) 4.dp else 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .fillMaxWidth()
            .height(110.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    when {
                        avatarUrl != null -> avatarUrl
                        avatarUri != null -> avatarUri
                        else -> imageId
                    }
                )
                .crossfade(true)
                .memoryCacheKey(imageId?.toString() ?: avatarUrl)
                .diskCacheKey(imageId?.toString() ?: avatarUrl)
                .build(),
            colorFilter = imageId?.let { ColorFilter.tint(Color.Gray.copy(alpha = 0.5f)) },
            contentDescription = stringResource(R.string.txt_avatar),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxSize()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewItem() {
    QuickMemTheme {
        AvatarItem(
            avatarUrl = "https://api.quickmem.app/public/images/avatar/17.jpg",
            isSelected = true,
            onSelected = {}
        )
    }
}

