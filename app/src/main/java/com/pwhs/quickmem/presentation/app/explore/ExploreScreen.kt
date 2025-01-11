package com.pwhs.quickmem.presentation.app.explore

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.DifficultyLevel
import com.pwhs.quickmem.core.data.enums.QuestionType
import com.pwhs.quickmem.presentation.app.explore.create_study_set_ai.CreateStudySetAITab
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.ads.AdsUtil
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.StudySetDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.revenuecat.purchases.CustomerInfo

@Composable
@Destination<RootGraph>
fun ExploreScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ExploreUiEvent.CreatedStudySet -> {
                    navigator.navigate(
                        StudySetDetailScreenDestination(
                            id = event.studySetId,
                        )
                    )
                }

                is ExploreUiEvent.EarnedCoins -> {
                    Toast.makeText(
                        context,
                        context.getString(event.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is ExploreUiEvent.Error -> {
                    Toast.makeText(
                        context,
                        context.getString(event.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Explore(
        modifier = modifier,
        isLoading = uiState.isLoading,
        title = uiState.title,
        description = uiState.description,
        numberOfFlashcards = uiState.numberOfFlashcards,
        language = uiState.language,
        questionType = uiState.questionType,
        difficultyLevel = uiState.difficulty,
        onTitleChange = { viewModel.onEvent(ExploreUiAction.OnTitleChanged(it)) },
        onDescriptionChange = { viewModel.onEvent(ExploreUiAction.OnDescriptionChanged(it)) },
        onNumberOfFlashcardsChange = {
            viewModel.onEvent(
                ExploreUiAction.OnNumberOfFlashcardsChange(
                    it
                )
            )
        },
        onLanguageChange = { viewModel.onEvent(ExploreUiAction.OnLanguageChanged(it)) },
        onQuestionTypeChange = { viewModel.onEvent(ExploreUiAction.OnQuestionTypeChanged(it)) },
        onDifficultyLevelChange = { viewModel.onEvent(ExploreUiAction.OnDifficultyLevelChanged(it)) },
        onCreateStudySet = {
            viewModel.onEvent(ExploreUiAction.OnCreateStudySet)
        },
        errorMessage = uiState.errorMessage,
        coins = uiState.coins,
        onEarnCoins = { viewModel.onEvent(ExploreUiAction.OnEarnCoins) },
        customerInfo = uiState.customerInfo
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Explore(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    title: String = "",
    description: String = "",
    numberOfFlashcards: Int = 0,
    language: String = "",
    questionType: QuestionType = QuestionType.MULTIPLE_CHOICE,
    difficultyLevel: DifficultyLevel = DifficultyLevel.EASY,
    @StringRes errorMessage: Int? = null,
    onTitleChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
    onNumberOfFlashcardsChange: (Int) -> Unit = {},
    onLanguageChange: (String) -> Unit = {},
    onQuestionTypeChange: (QuestionType) -> Unit = {},
    onDifficultyLevelChange: (DifficultyLevel) -> Unit = {},
    onCreateStudySet: () -> Unit = {},
    coins: Int = 0,
    onEarnCoins: () -> Unit = {},
    customerInfo: CustomerInfo? = null,
) {
    val context = LocalContext.current
    var isGettingAds by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.txt_explore),
                        style = typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Text(
                            text = when (customerInfo?.activeSubscriptions?.isNotEmpty()) {
                                true -> stringResource(R.string.txt_unlimited)
                                false -> coins.toString()
                                else -> "0"
                            },
                            style = typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_coin),
                            contentDescription = stringResource(R.string.txt_coin),
                            modifier = Modifier.size(24.dp),
                            contentScale = ContentScale.Crop
                        )
                        if (customerInfo?.activeSubscriptions?.isNotEmpty() == false) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.txt_add),
                                tint = colorScheme.primary,
                                modifier = Modifier
                                    .size(24.dp)
                                    .clickable {
                                        isGettingAds = true
                                        AdsUtil.rewardedInterstitialAd(
                                            context,
                                            onAdLoadFailedToLoad = {
                                                isGettingAds = false
                                            },
                                            onAdWatched = {
                                                onEarnCoins()
                                                isGettingAds = false
                                            }
                                        )
                                    },
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            CreateStudySetAITab(
                modifier = Modifier.padding(horizontal = 16.dp),
                title = title,
                description = description,
                numberOfFlashcards = numberOfFlashcards,
                language = language,
                questionType = questionType,
                difficultyLevel = difficultyLevel,
                onTitleChange = onTitleChange,
                onDescriptionChange = onDescriptionChange,
                onNumberOfFlashcardsChange = onNumberOfFlashcardsChange,
                onLanguageChange = onLanguageChange,
                onQuestionTypeChange = onQuestionTypeChange,
                onDifficultyLevelChange = onDifficultyLevelChange,
                errorMessage = errorMessage,
                onCreateStudySet = {
                    if (coins > 0 || customerInfo?.activeSubscriptions?.isNotEmpty() == true) {
                        onCreateStudySet()
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.txt_you_need_at_least_1_coin_to_create_a_study_set),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                isPlus = customerInfo?.activeSubscriptions?.isNotEmpty() == true
            )

        }
        LoadingOverlay(isLoading = isLoading || isGettingAds)
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun ExploreScreenPreview() {
    QuickMemTheme {
        Explore()
    }
}