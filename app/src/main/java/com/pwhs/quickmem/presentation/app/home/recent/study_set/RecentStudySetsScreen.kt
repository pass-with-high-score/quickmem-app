package com.pwhs.quickmem.presentation.app.home.recent.study_set

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.domain.model.study_set.GetStudySetResponseModel
import com.pwhs.quickmem.presentation.ads.BannerAds
import com.pwhs.quickmem.presentation.app.home.recent.component.RecentAccessTopAppBar
import com.pwhs.quickmem.presentation.app.library.study_set.component.StudySetItem
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.CreateStudySetScreenDestination
import com.ramcosta.composedestinations.generated.destinations.StudySetDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination<RootGraph>
@Composable
fun RecentStudySetsScreen(
    modifier: Modifier = Modifier,
    viewModel: RecentStudySetsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
    resultBackNavigator: ResultBackNavigator<Boolean>,
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is RecentStudySetsUiEvent.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    AllRecentAccessStudySets(
        modifier = modifier,
        onStudySetClick = { studySet ->
            navigator.navigate(
                StudySetDetailScreenDestination(
                    id = studySet?.id ?: ""
                )
            )
        },
        isLoading = uiState.isLoading,
        studySets = uiState.studySets,
        onNavigateBack = {
            resultBackNavigator.navigateBack(true)
        },
        onAddStudySet = {
            navigator.navigate(CreateStudySetScreenDestination(
                subjectId = 1
            ))
        }
    )
}

@Composable
fun AllRecentAccessStudySets(
    modifier: Modifier = Modifier,
    studySets: List<GetStudySetResponseModel> = emptyList(),
    onStudySetClick: (GetStudySetResponseModel?) -> Unit = {},
    isLoading: Boolean = false,
    onNavigateBack: () -> Unit = {},
    onAddStudySet: () -> Unit = {},
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredStudySets = studySets.filter { it.title.contains(searchQuery, ignoreCase = true) }

    Scaffold(
        containerColor = colorScheme.background,
        modifier = modifier,
        topBar = {
            RecentAccessTopAppBar(
                title = stringResource(R.string.txt_all_recent_access_study_sets),
                description = stringResource(R.string.txt_study_sets_you_ve_recently_opened_to_view_their_details),
                color = colorScheme.primary,
                onNavigateBack = onNavigateBack,
                onAddNew = onAddStudySet,
                searchQuery = searchQuery,
                placeHolder = stringResource(R.string.txt_search_study_sets),
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
                items(filteredStudySets.size, key = { it }) { index ->
                    val studySet = filteredStudySets.getOrNull(index)
                    if (studySet != null) {
                        StudySetItem(
                            studySet = studySet,
                            onStudySetClick = { onStudySetClick(studySet) }
                        )
                    }
                }
                item {
                    if (!isLoading && filteredStudySets.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
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
private fun AllRecentAccessStudySetsScreenPreview() {
    QuickMemTheme {
        AllRecentAccessStudySets(
            isLoading = false,
            onNavigateBack = {},
            onAddStudySet = {},
            onStudySetClick = {}
        )
    }
}