package com.pwhs.quickmem.presentation.app.home.subject

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.presentation.ads.BannerAds
import com.pwhs.quickmem.presentation.app.home.subject.component.SearchStudySetBySubjectTopAppBar
import com.pwhs.quickmem.presentation.app.library.study_set.component.StudySetItem
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateStudySetScreenDestination
import com.ramcosta.composedestinations.generated.destinations.StudySetDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination<RootGraph>(
    navArgs = SearchStudySetBySubjectArgs::class
)
@Composable
fun SearchStudySetBySubjectScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchStudySetBySubjectViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultBackNavigator: ResultBackNavigator<Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val studySetItems: LazyPagingItems<GetStudySetResponseModel> =
        viewModel.studySetState.collectAsLazyPagingItems()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SearchStudySetBySubjectUiEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    SearchStudySetBySubject(
        modifier = modifier,
        onStudySetClick = { studySet ->
            navigator.navigate(
                StudySetDetailScreenDestination(
                    id = studySet?.id ?: ""
                )
            )
        },
        icon = uiState.icon,
        studySetCount = uiState.studySetCount,
        isLoading = uiState.isLoading,
        nameSubject = uiState.subject?.subjectName,
        colorSubject = uiState.subject?.color ?: Color.Blue,
        descriptionSubject = uiState.subject?.subjectDescription,
        studySets = studySetItems,
        onNavigateBack = {
            resultBackNavigator.navigateBack(true)
        },
        onStudySetRefresh = {
            viewModel.onEvent(SearchStudySetBySubjectUiAction.RefreshStudySets)
        },
        onAddStudySet = {
            navigator.navigate(
                CreateStudySetScreenDestination(
                    subjectId = uiState.subject?.id ?: 1
                )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchStudySetBySubject(
    modifier: Modifier = Modifier,
    studySets: LazyPagingItems<GetStudySetResponseModel>? = null,
    onStudySetClick: (GetStudySetResponseModel?) -> Unit = {},
    @StringRes nameSubject: Int? = null,
    colorSubject: Color = colorScheme.primary,
    @DrawableRes icon: Int? = null,
    studySetCount: Int = 0,
    @StringRes descriptionSubject: Int? = null,
    isLoading: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onStudySetRefresh: () -> Unit = {},
    onAddStudySet: () -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        containerColor = colorScheme.background,
        modifier = modifier,
        topBar = {
            SearchStudySetBySubjectTopAppBar(
                onNavigateBack = onNavigateBack,
                name = nameSubject?.let { stringResource(it) },
                color = colorSubject,
                icon = icon,
                studySetCount = studySetCount,
                description = descriptionSubject?.let { stringResource(it) },
                onAddStudySet = onAddStudySet,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
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
                items(studySets?.itemCount ?: 0, key = { it }) { index ->
                    val studySet = studySets?.get(index)
                    if (studySet != null && studySet.title.contains(
                            searchQuery,
                            ignoreCase = true
                        )
                    ) {
                        StudySetItem(
                            studySet = studySet,
                            onStudySetClick = { onStudySetClick(studySet) }
                        )
                    }
                }
                item {
                    if (studySets?.itemCount == 0 && !isLoading) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(innerPadding)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.txt_no_study_sets_found),
                                style = typography.bodyLarge,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                item {
                    studySets?.apply {
                        when {
                            loadState.refresh is LoadState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .padding(innerPadding),
                                    color = colorScheme.primary
                                )
                            }

                            loadState.refresh is LoadState.Error -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(innerPadding),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Image(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = stringResource(R.string.txt_error),
                                    )
                                    Text(
                                        text = stringResource(R.string.txt_error_occurred),
                                        style = typography.titleLarge,
                                        textAlign = TextAlign.Center
                                    )
                                    Button(
                                        onClick = onStudySetRefresh,
                                        modifier = Modifier.padding(top = 16.dp)
                                    ) {
                                        Text(text = stringResource(R.string.txt_retry))
                                    }
                                }
                            }

                            loadState.append is LoadState.Loading -> {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .padding(top = 16.dp),
                                    color = colorScheme.primary
                                )
                            }

                            loadState.append is LoadState.Error -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(innerPadding),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Image(
                                        imageVector = Icons.Default.Error,
                                        contentDescription = stringResource(R.string.txt_error),
                                    )
                                    Text(
                                        text = stringResource(R.string.txt_error_occurred),
                                        style = typography.titleLarge,
                                        textAlign = TextAlign.Center
                                    )
                                    Button(
                                        onClick = { retry() },
                                        modifier = Modifier.padding(top = 16.dp)
                                    ) {
                                        Text(text = stringResource(R.string.txt_retry))
                                    }
                                }
                            }
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

@Preview(showSystemUi = true)
@Composable
private fun SearchStudySetBySubjectScreenPreview() {
    QuickMemTheme {
        SearchStudySetBySubject()
    }
}