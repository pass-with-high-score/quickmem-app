package com.pwhs.quickmem.presentation.app.search

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.search.SearchQueryModel
import com.pwhs.quickmem.presentation.app.library.component.SearchTextField
import com.pwhs.quickmem.presentation.app.search.component.SearchRecentList
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.SearchResultScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import timber.log.Timber

@Destination<RootGraph>
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultSearchResult: ResultRecipient<SearchResultScreenDestination, Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    resultSearchResult.onNavResult { result ->
        when (result) {
            NavResult.Canceled -> {
                Timber.d("SearchScreen: NavResult.Canceled")
            }

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.onEvent(SearchUiAction.OnRefresh)
                }
            }
        }

    }

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SearchUiEvent.NavigateToResult -> {
                    navigator.navigate(
                        SearchResultScreenDestination(
                            query = event.query
                        )
                    )
                }

                is SearchUiEvent.ShowError -> {
                    Toast.makeText(context, context.getString(event.error), Toast.LENGTH_SHORT)
                        .show()
                }

                SearchUiEvent.ClearAllSearchRecent -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_deleted_successfully), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Search(
        modifier = modifier,
        query = uiState.query,
        listSearchQuery = uiState.listResult,
        errorMessage = uiState.error,
        onQueryChange = { viewModel.onEvent(SearchUiAction.OnQueryChanged(it)) },
        onNavigateBack = { navigator.navigateUp() },
        onSearch = { viewModel.onEvent(SearchUiAction.Search) },
        onClearAll = { viewModel.onEvent(SearchUiAction.DeleteAllSearch) },
        onSearchRecentClick = { query ->
            viewModel.onEvent(
                SearchUiAction.SearchWithQueryRecent(
                    query
                )
            )
        },
        onDeleteQuery = { query -> viewModel.onEvent(SearchUiAction.DeleteSearch(query.query)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Search(
    modifier: Modifier = Modifier,
    onNavigateBack: () -> Unit = {},
    listSearchQuery: List<SearchQueryModel> = emptyList(),
    query: String = "",
    @StringRes errorMessage: Int? = null,
    onClearAll: () -> Unit = {},
    onQueryChange: (String) -> Unit = {},
    onSearch: () -> Unit = {},
    onSearchRecentClick: (String) -> Unit = {},
    onDeleteQuery: (SearchQueryModel) -> Unit = {},
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            LargeTopAppBar(
                title = {
                    SearchTextField(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        searchQuery = query,
                        onSearchQueryChange = onQueryChange,
                        errorMessage = errorMessage?.let { stringResource(it) },
                        placeholder = stringResource(R.string.txt_study_sets_folders),
                        onSearch = onSearch,
                    )
                },
                expandedHeight = 160.dp,
                actions = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(R.string.txt_close)
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            if (listSearchQuery.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(
                        text = stringResource(R.string.txt_enter_a_topic_or_keywords),
                        style = typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                    Text(
                        text = stringResource(R.string.txt_tip_the_more_specific_the_better),
                        style = typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    )
                }
            } else {
                SearchRecentList(
                    listResult = listSearchQuery,
                    onSearchRecent = { query -> onSearchRecentClick(query) },
                    onDelete = { query -> onDeleteQuery(query) },
                    onClearAll = onClearAll
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun SearchScreenPreview() {
    QuickMemTheme {
        Search()
    }
}