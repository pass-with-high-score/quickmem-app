package com.pwhs.quickmem.presentation.app.study_set.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.users.UserResponseModel
import com.pwhs.quickmem.utils.gradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySetDetailTopAppBar(
    modifier: Modifier = Modifier,
    isOwner: Boolean,
    title: String,
    isAIGenerated: Boolean,
    color: Color,
    userResponse: UserResponseModel,
    flashCardCount: Int,
    onNavigateBack: () -> Unit,
    onAddFlashcard: () -> Unit,
    onShareClicked: () -> Unit,
    onMoreClicked: () -> Unit,
    onNavigateToUserDetail: () -> Unit = {},
) {
    LargeTopAppBar(
        title = {
            Column {
                Text(
                    title, style = typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = colorScheme.onSurface
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (isAIGenerated) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(
                            stringResource(R.string.txt_ai_generated),
                            style = typography.bodyMedium.copy(
                                color = colorScheme.secondary
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
                    modifier = Modifier.clickable { onNavigateToUserDetail() }
                ) {
                    AsyncImage(
                        model = userResponse.avatarUrl,
                        contentDescription = stringResource(R.string.txt_user_avatar),
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                    )
                    Text(
                        userResponse.username,
                        style = typography.bodyMedium.copy(
                            color = colorScheme.secondary
                        ),
                    )
                    VerticalDivider(
                        modifier = Modifier
                            .height(16.dp)
                    )
                    Text(
                        when (flashCardCount) {
                            0 -> stringResource(R.string.txt_no_flashcards)
                            1 -> stringResource(R.string.txt_one_flashcard)
                            else -> stringResource(R.string.txt_num_flashcards, flashCardCount)
                        },
                        style = typography.bodyMedium.copy(
                            color = colorScheme.secondary
                        )
                    )
                }
            }
        },
        modifier = modifier.background(color.gradientBackground()),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        expandedHeight = if (isAIGenerated) 165.dp else 120.dp,
        collapsedHeight = 56.dp,
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.txt_back),
                )
            }
        },
        actions = {
            IconButton(
                onClick = onShareClicked
            ) {
                Icon(
                    imageVector = Default.IosShare,
                    contentDescription = stringResource(R.string.txt_share)
                )
            }
            if (isOwner) {
                IconButton(
                    onClick = onAddFlashcard
                ) {
                    Icon(
                        imageVector = Default.Add,
                        contentDescription = stringResource(R.string.txt_add_flashcard)
                    )
                }
            }
            IconButton(
                onClick = onMoreClicked
            ) {
                Icon(
                    imageVector = Default.MoreVert,
                    contentDescription = stringResource(R.string.txt_more)
                )
            }
        }
    )
}