package com.pwhs.quickmem.presentation.app.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.users.UserResponseModel
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun FolderHomeItem(
    modifier: Modifier = Modifier,
    title: String = "",
    numOfStudySets: Int = 0,
    userResponseModel: UserResponseModel = UserResponseModel(),
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 16.dp)
            .padding(vertical = 8.dp)
            .width(width = 290.dp),
        onClick = onClick,
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.onSurface.copy(alpha = 0.12f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Outlined.Folder,
                        contentDescription = stringResource(R.string.txt_folder_icon),
                    )
                    Text(
                        text = title,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = colorScheme.onSurface
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = when (numOfStudySets) {
                            0 -> stringResource(R.string.txt_no_study_sets)
                            1 -> stringResource(R.string.txt_one_study_set)
                            else -> stringResource(R.string.txt_study_sets_library, numOfStudySets)
                        },
                        style = typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(16.dp),
                        color = colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                    AsyncImage(
                        model = userResponseModel.avatarUrl,
                        contentDescription = stringResource(R.string.txt_user_avatar),
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(colorScheme.primary.copy(alpha = 0.1f)),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = userResponseModel.username,
                        style = typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface,
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                }
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun FolderItemPreview() {
    QuickMemTheme {
        Scaffold {
            LazyRow(
                modifier = Modifier.padding(it),
            ) {
                items(10) {
                    FolderHomeItem(
                        modifier = Modifier.fillMaxWidth(),
                        title = "Folder Title",
                        numOfStudySets = 10,
                        userResponseModel = UserResponseModel(
                            username = "User Name",
                            avatarUrl = "https://www.example.com/avatar.jpg"
                        )
                    )
                }
            }
        }
    }
}
