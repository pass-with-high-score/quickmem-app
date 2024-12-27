package com.pwhs.quickmem.presentation.app.library.folder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.presentation.app.library.component.SearchTextField
import com.pwhs.quickmem.presentation.app.library.folder.component.FolderItem
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListFolderScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isOwner: Boolean = false,
    folders: List<GetFolderResponseModel> = emptyList(),
    onFolderClick: (GetFolderResponseModel) -> Unit = {},
    onAddFolderClick: () -> Unit = {},
    onFolderRefresh: () -> Unit = {}
) {
    val refreshState = rememberPullToRefreshState()
    var searchQuery by remember { mutableStateOf("") }

    val filterFolders = folders.filter {
        searchQuery.trim().takeIf { query -> query.isNotEmpty() }?.let { query ->
            it.title.contains(query, ignoreCase = true)
        } ?: true
    }
    Scaffold(
        modifier = modifier
    ) { innerPadding ->
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
            state = refreshState,
            isRefreshing = isLoading,
            onRefresh = {
                onFolderRefresh()
            }
        ) {
            when (folders.isEmpty()) {
                true -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(top = 40.dp)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (isOwner) {
                            Icon(
                                imageVector = Icons.Outlined.Folder,
                                contentDescription = stringResource(R.string.txt_folder),
                                modifier = Modifier.size(60.dp),
                                tint = colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.txt_organize_your_study_sets_in_folders_by_subject_teacher_course_or_any_other_way_you_like),
                                style = typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center
                                ),
                            )
                            Button(
                                shape = MaterialTheme.shapes.medium,
                                onClick = onAddFolderClick,
                                modifier = Modifier
                            ) {
                                Text(
                                    text = stringResource(R.string.txt_create_a_folder),
                                    style = typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.ic_folder),
                                contentDescription = stringResource(R.string.txt_empty_folder),
                            )
                            Text(
                                text = stringResource(R.string.txt_no_folders_found),
                                style = typography.titleLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                false -> {
                    LazyColumn {
                        item {
                            if (folders.isNotEmpty()) {
                                SearchTextField(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    searchQuery = searchQuery,
                                    onSearchQueryChange = { searchQuery = it },
                                    placeholder = stringResource(R.string.txt_search_folders),
                                )
                            }
                        }
                        items(items = filterFolders, key = {it.id}) { folder ->
                            FolderItem(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                title = folder.title,
                                numOfStudySets = folder.studySetCount,
                                onClick = { onFolderClick(folder) },
                                userResponseModel = folder.owner,
                                folder = folder
                            )
                        }
                        item {
                            if (filterFolders.isEmpty() && searchQuery.trim().isNotEmpty()) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = stringResource(R.string.txt_no_folders_found),
                                        style = typography.bodyLarge,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.padding(60.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ListFolderScreenPreview() {
    QuickMemTheme {
        ListFolderScreen()
    }

}