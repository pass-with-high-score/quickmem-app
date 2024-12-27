package com.pwhs.quickmem.presentation.app.home.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.outlined.Groups
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
import com.pwhs.quickmem.domain.model.classes.GetClassByOwnerResponseModel
import com.pwhs.quickmem.domain.model.users.UserResponseModel
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun ClassHomeItem(
    modifier: Modifier = Modifier,
    classItem: GetClassByOwnerResponseModel?,
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
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Outlined.Groups,
                    contentDescription = stringResource(R.string.txt_class_icon)
                )
                Text(
                    text = "${classItem?.title}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = when (classItem?.memberCount) {
                        0 -> stringResource(R.string.txt_no_members)
                        1 -> stringResource(R.string.txt_one_member)
                        else -> stringResource(
                            R.string.txt_nums_members,
                            classItem?.memberCount ?: 0
                        )
                    },
                    style = typography.bodyMedium
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(16.dp),
                    thickness = 1.dp,
                    color = colorScheme.onSurface.copy(alpha = 0.12f)
                )
                AsyncImage(
                    model = classItem?.owner?.avatarUrl,
                    contentDescription = stringResource(R.string.txt_user_avatar),
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = classItem?.owner?.username ?: "",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ClassItemPreview() {
    QuickMemTheme {
        Scaffold {
            LazyRow(
                modifier = Modifier
                    .padding(it)
            ) {
                item {
                    repeat(3) {
                        ClassHomeItem(
                            classItem = GetClassByOwnerResponseModel(
                                title = "Class Title",
                                allowSetManagement = true,
                                allowMemberManagement = true,
                                studySetCount = 10,
                                owner = UserResponseModel(
                                    username = "User",
                                    avatarUrl = "https://example.com/avatar.jpg"
                                ),
                                createdAt = "2021-01-01T00:00:00Z",
                                updatedAt = "2021-01-01T00:00:00Z",
                                description = "Class Description",
                                id = "1",
                                folderCount = 3,
                                memberCount = 5,
                                joinToken = "123456",
                            )
                        )
                    }
                }
            }
        }
    }
}