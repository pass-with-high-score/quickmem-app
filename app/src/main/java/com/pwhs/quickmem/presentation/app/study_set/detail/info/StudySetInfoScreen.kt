package com.pwhs.quickmem.presentation.app.study_set.detail.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.AutoMirrored.Filled
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pwhs.quickmem.R
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Destination<RootGraph>(
    navArgs = StudySetInfoScreenArgs::class
)
@Composable
fun StudySetInfoScreen(
    modifier: Modifier = Modifier,
    viewModel: StudySetViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val uiState by viewModel.uiState.collectAsState()

    StudySetInfo(
        modifier = modifier,
        authorUsername = uiState.authorUsername,
        authorAvatarUrl = uiState.authorAvatarUrl,
        creationDate = uiState.creationDate,
        description = uiState.description,
        isPublic = uiState.isPublic,
        isAIGenerated = uiState.isAIGenerated,
        onNavigateUp = { navigator.navigateUp() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySetInfo(
    modifier: Modifier = Modifier,
    authorUsername: String,
    authorAvatarUrl: String,
    creationDate: String,
    description: String,
    isPublic: Boolean,
    onNavigateUp: () -> Unit = {},
    isAIGenerated: Boolean = false
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.txt_study_set_info),
                        style = typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateUp
                    ) {
                        Icon(
                            imageVector = Filled.ArrowBack,
                            contentDescription = stringResource(R.string.txt_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Text(
                    text = stringResource(R.string.txt_created_by),
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(10.dp)
                    ) {
                        AsyncImage(
                            model = authorAvatarUrl,
                            contentDescription = stringResource(R.string.txt_user_avatar),
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                        Text(
                            text = authorUsername,
                            style = typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.weight(1f),
                            maxLines = 1
                        )
                    }
                }
            }

            Column {
                Text(
                    text = stringResource(R.string.txt_created_on),
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                val parsedDate = ZonedDateTime.parse(creationDate)
                val formattedDate =
                    parsedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                ) {
                    Text(
                        text = formattedDate,
                        style = typography.bodyLarge,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }

            if (description.isNotEmpty()) {
                Column {
                    Text(
                        text = stringResource(R.string.txt_description),
                        style = typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                    ) {
                        Text(
                            text = description,
                            style = typography.bodyLarge,
                            modifier = Modifier.padding(10.dp),
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Column {
                Text(
                    text = stringResource(R.string.txt_visibility),
                    style = typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                ) {
                    Text(
                        text = if (isPublic) stringResource(R.string.txt_public) else stringResource(
                            R.string.txt_private
                        ), style = typography.bodyLarge, modifier = Modifier.padding(10.dp)
                    )
                }
            }

            if (isAIGenerated) {
                Column {
                    Text(
                        text = stringResource(R.string.txt_ai_generated),
                        style = typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                    ) {
                        Text(
                            text = stringResource(R.string.txt_yes),
                            style = typography.bodyLarge,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
private fun StudySetInfoPreview() {
    QuickMemTheme {
        StudySetInfo(
            authorUsername = "Author",
            authorAvatarUrl = "",
            creationDate = "2021-09-01",
            description = "Description",
            isPublic = true
        )
    }
}