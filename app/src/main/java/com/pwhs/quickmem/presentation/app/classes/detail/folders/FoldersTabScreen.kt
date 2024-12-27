package com.pwhs.quickmem.presentation.app.classes.detail.folders

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.presentation.app.classes.detail.component.ClassDetailEmptyItems
import com.pwhs.quickmem.presentation.app.library.component.SearchTextField
import com.pwhs.quickmem.presentation.app.library.folder.component.FolderItem
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@Composable
fun FoldersTabScreen(
    modifier: Modifier = Modifier,
    isOwner: Boolean,
    folders: List<GetFolderResponseModel> = emptyList(),
    onAddFoldersClicked: () -> Unit = {},
    onFolderItemClicked: (GetFolderResponseModel) -> Unit = {},
    onDeleteFolderClicked: (String) -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    val filterFolders = folders.filter {
        searchQuery.trim().takeIf { query -> query.isNotEmpty() }?.let { query ->
            it.title.contains(query, ignoreCase = true)
        } != false
    }
    when {
        folders.isEmpty() -> {
            ClassDetailEmptyItems(
                modifier = modifier,
                title = stringResource(R.string.txt_this_class_has_no_folders),
                subtitle = stringResource(R.string.txt_add_folders_to_share_them_with_your_class),
                buttonTitle = stringResource(R.string.txt_add_folders),
                onAddClick = onAddFoldersClicked,
                isOwner = isOwner
            )
        }

        else -> {
            LazyColumn(
                modifier = modifier,
            ) {
                item {
                    SearchTextField(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        searchQuery = searchQuery,
                        onSearchQueryChange = { searchQuery = it },
                        placeholder = stringResource(R.string.txt_search_folders),
                    )
                }
                items(items = filterFolders, key = { it.id }) { folders ->
                    FolderItem(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        title = folders.title,
                        onClick = { onFolderItemClicked(folders) },
                        numOfStudySets = folders.studySetCount,
                        userResponseModel = folders.owner,
                        onDeleteClick = { folderId ->
                            onDeleteFolderClicked(folderId)
                        },
                        folder = folders,
                        isOwner = isOwner
                    )
                }
                item {
                    if (filterFolders.isEmpty() && searchQuery.trim().isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = CenterHorizontally
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

@Preview(showBackground = true)
@Composable
private fun FoldersTabScreenPreview() {
    QuickMemTheme {
        FoldersTabScreen(isOwner = false)
    }
}