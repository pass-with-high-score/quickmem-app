package com.pwhs.quickmem.presentation.app.study_set.add_to_folder.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.domain.model.users.UserResponseModel
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun AddStudySetToFoldersItem(
    modifier: Modifier = Modifier,
    folder: GetFolderResponseModel,
    isAdded: Boolean = false,
    onAddStudySetToFolders: (String) -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.onSurface.copy(alpha = 0.12f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .background(Color.Transparent)
                    .weight(1f)
            ) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Outlined.Folder,
                        contentDescription = stringResource(R.string.txt_folder),
                    )
                    Text(
                        text = folder.title,
                        style = typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (folder.studySetCount) {
                            0 -> stringResource(R.string.txt_no_study_sets)
                            1 -> stringResource(R.string.txt_one_study_set)
                            else -> stringResource(
                                R.string.txt_study_sets_library,
                                folder.studySetCount
                            )
                        },
                        style = typography.bodyMedium
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(16.dp)
                            .padding(horizontal = 8.dp),
                        thickness = 1.dp,
                        color = colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AsyncImage(
                            model = folder.owner.avatarUrl,
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = folder.owner.username,
                            style = typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .clickable {
                        onAddStudySetToFolders(folder.id)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(if (isAdded) R.drawable.ic_check_circle else R.drawable.ic_add_circle),
                    contentDescription = if (isAdded) stringResource(R.string.txt_delete_folder) else stringResource(
                        R.string.txt_add_folder
                    ),
                    modifier = Modifier.size(26.dp),
                    tint = colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun AddStudySetToFoldersItemPreview() {
    QuickMemTheme {
        Scaffold {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    repeat(10) {
                        AddStudySetToFoldersItem(
                            modifier = Modifier
                                .fillMaxWidth(),
                            folder = GetFolderResponseModel(
                                id = "1",
                                title = "Study Set Title",
                                studySetCount = 10,
                                owner = UserResponseModel(
                                    id = "1",
                                    username = "User",
                                    avatarUrl = "https://www.example.com/avatar.jpg"
                                ),
                                description = "Study Set Description",
                                isPublic = true,
                                createdAt = "2021-01-01T00:00:00Z",
                                updatedAt = "2021-01-01T00:00:00Z",
                            )
                        )
                    }
                }
            }
        }
    }
}