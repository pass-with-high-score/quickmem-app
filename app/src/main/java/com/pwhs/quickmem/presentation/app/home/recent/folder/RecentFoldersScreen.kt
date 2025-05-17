package com.pwhs.quickmem.presentation.app.home.recent.folder

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.folder.GetFolderResponseModel
import com.pwhs.quickmem.presentation.ads.BannerAds
import com.pwhs.quickmem.presentation.app.home.recent.component.RecentAccessTopAppBar
import com.pwhs.quickmem.presentation.app.library.folder.component.FolderItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateFolderScreenDestination
import com.ramcosta.composedestinations.generated.destinations.FolderDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination<RootGraph>
@Composable
fun RecentFoldersScreen(
    modifier: Modifier = Modifier,
    viewModel: RecentFoldersViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultBackNavigator: ResultBackNavigator<Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is RecentFoldersUiEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    AllRecentAccessFolders(
        modifier = modifier,
        onFolderClick = {
            navigator.navigate(
                FolderDetailScreenDestination(
                    id = it.id,
                )
            )
        },
        isLoading = uiState.isLoading,
        folders = uiState.folders,
        onNavigateBack = {
            resultBackNavigator.navigateBack(true)
        },
        onAddFolder = {
            navigator.navigate(CreateFolderScreenDestination())
        }
    )
}

@Composable
fun AllRecentAccessFolders(
    modifier: Modifier,
    onFolderClick: (GetFolderResponseModel) -> Unit = {},
    isLoading: Boolean = false,
    folders: List<GetFolderResponseModel> = emptyList(),
    onNavigateBack: () -> Unit = {},
    onAddFolder: () -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        containerColor = colorScheme.background,
        modifier = modifier,
        topBar = {
            RecentAccessTopAppBar(
                title = stringResource(R.string.txt_all_recent_access_folders),
                description = stringResource(R.string.txt_folders_you_ve_recently_opened_to_view_their_details),
                color = colorScheme.primary,
                onNavigateBack = onNavigateBack,
                onAddNew = onAddFolder,
                searchQuery = searchQuery,
                onSearchQueryChange = {
                    searchQuery = it
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(folders.size, key = { it }) { index ->
                    val folder = folders.getOrNull(index)
                    if (folder != null && folder.title.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    ) {
                        FolderItem(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            title = folder.title,
                            numOfStudySets = folder.studySetCount,
                            onClick = { onFolderClick(folder) },
                            userResponseModel = folder.owner,
                            folder = folder
                        )
                    }
                }
                item {
                    if (!isLoading && folders
                        .isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(innerPadding)
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
            BannerAds(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            )
        }
    }
}