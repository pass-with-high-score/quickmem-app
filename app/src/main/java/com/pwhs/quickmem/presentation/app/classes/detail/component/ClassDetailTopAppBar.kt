package com.pwhs.quickmem.presentation.app.classes.detail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.users.UserResponseModel
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.gradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailTopAppBar(
    modifier: Modifier = Modifier,
    title: String = "",
    studySetCount: Int = 0,
    userResponse: UserResponseModel = UserResponseModel(),
    onNavigateBack: () -> Unit = {},
    onMoreClicked: () -> Unit = {},
    onShareClicked: () -> Unit = {},
    onNavigateToUserDetail: (String) -> Unit = {},
) {
    LargeTopAppBar(
        modifier = modifier.background(colorScheme.primary.gradientBackground()),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .weight(1f),
                ) {
                    Text(
                        title, style = typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Row(
                            modifier = Modifier.clickable {
                                onNavigateToUserDetail(
                                    userResponse.id
                                )
                            },
                            verticalAlignment = Alignment.CenterVertically,
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
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        VerticalDivider(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .height(16.dp)
                        )
                        Text(
                            when (studySetCount) {
                                0 -> stringResource(R.string.txt_no_study_sets)
                                1 -> stringResource(R.string.txt_one_study_set)
                                else -> stringResource(
                                    R.string.txt_study_sets_count_class_detail,
                                    studySetCount
                                )
                            },
                            style = typography.bodyMedium.copy(
                                color = colorScheme.secondary
                            )
                        )
                    }
                }
            }
        },
        colors = topAppBarColors(
            containerColor = Color.Transparent,
        ),
        expandedHeight = 140.dp,
        collapsedHeight = 56.dp,
        navigationIcon = {
            IconButton(
                onClick = onNavigateBack,
                colors = iconButtonColors(
                    contentColor = colorScheme.onSurface
                )
            ) {
                Icon(
                    imageVector = AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.txt_back),
                )
            }
        },
        actions = {
            IconButton(
                onClick = onShareClicked
            ) {
                Icon(
                    imageVector = Default.Share,
                    contentDescription = stringResource(R.string.txt_share)
                )
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

@Preview
@Composable
fun ClassDetailScreen() {
    QuickMemTheme {
        Scaffold(
            topBar = {
                ClassDetailTopAppBar(
                    title = "Class Detail",
                )
            }
        ) {
            Column(
                modifier = Modifier.padding(it)
            ) {

            }
        }
    }
}