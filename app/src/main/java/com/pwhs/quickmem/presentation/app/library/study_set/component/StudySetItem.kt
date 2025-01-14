package com.pwhs.quickmem.presentation.app.library.study_set.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.CardDefaults.elevatedCardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.color.ColorModel
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.domain.model.subject.SubjectModel
import com.pwhs.quickmem.domain.model.users.UserResponseModel
import com.pwhs.quickmem.presentation.components.RoleUserText
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.toColor

@Composable
fun StudySetItem(
    modifier: Modifier = Modifier,
    isOwner: Boolean = false,
    studySet: GetStudySetResponseModel?,
    onStudySetClick: (String) -> Unit = {},
    onDeleteClick: ((String) -> Unit)? = null
) {
    val borderColor = studySet?.color?.hexValue?.toColor()
        ?: ColorModel.defaultColors[0].hexValue.toColor()

    val titleColor = borderColor.copy(alpha = 0.8f)

    Card(
        onClick = { onStudySetClick(studySet?.id ?: "") },
        colors = cardColors(containerColor = Color.White),
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = elevatedCardElevation(defaultElevation = 4.dp),
        border = BorderStroke(width = 1.dp, color = borderColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = studySet?.title.orEmpty(),
                    style = typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = titleColor
                )
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = typography.bodySmall.toSpanStyle()
                                .copy(fontWeight = FontWeight.Bold)
                        ) {
                            append(studySet?.flashcardCount.toString())
                            withStyle(
                                style = typography.bodySmall.toSpanStyle()
                                    .copy(fontWeight = FontWeight.Normal)
                            ) {
                                append(" ${stringResource(R.string.txt_flashcards)}")
                            }
                        }
                    }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text =  stringResource(studySet?.subject?.subjectName ?: SubjectModel.defaultSubjects[0].subjectName),
                        style = typography.bodySmall.copy(
                            color = colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                    if(studySet?.isAIGenerated == true) {
                        VerticalDivider(
                            modifier = Modifier.height(12.dp)
                        )
                        Text(
                            stringResource(R.string.txt_ai_generated),
                            style = typography.bodySmall.copy(
                                color = colorScheme.onSurface.copy(alpha = 0.6f)
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_generative_ai),
                            contentDescription = stringResource(R.string.txt_ai_generated),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    AsyncImage(
                        model = studySet?.owner?.avatarUrl,
                        contentDescription = stringResource(R.string.txt_user_avatar),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                    )

                    if (studySet != null) {
                        RoleUserText(
                            username = studySet.owner.username,
                            role = studySet.owner.role,
                        )
                    }
                }
            }
            if (isOwner && onDeleteClick != null) {
                IconButton(
                    onClick = { onDeleteClick(studySet?.id.orEmpty()) }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = stringResource(R.string.txt_delete),
                        modifier = Modifier
                            .size(24.dp)
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun StudySetItemPreview() {
    QuickMemTheme {
        Scaffold {
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .padding(horizontal = 16.dp)
            ) {
                items(10) {
                    StudySetItem(
                        modifier = Modifier.fillMaxWidth(),
                        studySet = GetStudySetResponseModel(
                            id = "1",
                            title = "Study Set Title",
                            flashcardCount = 10,
                            color = ColorModel.defaultColors[0],
                            subject = SubjectModel.defaultSubjects[0],
                            owner = UserResponseModel(
                                id = "1",
                                username = "User",
                                avatarUrl = "https://www.example.com/avatar.jpg"
                            ),
                            description = "Study Set Description",
                            isPublic = true,
                            createdAt = "2021-01-01T00:00:00Z",
                            updatedAt = "2021-01-01T00:00:00Z",
                            flashcards = emptyList(),
                            isAIGenerated = false
                        ),
                        isOwner = true,
                        onDeleteClick = {}
                    )
                }
            }
        }
    }
}
