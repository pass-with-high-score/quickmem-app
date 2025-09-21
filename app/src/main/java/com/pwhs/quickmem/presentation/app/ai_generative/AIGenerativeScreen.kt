package com.pwhs.quickmem.presentation.app.ai_generative

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults.iconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.DifficultyLevel
import com.pwhs.quickmem.core.data.enums.QuestionType
import com.pwhs.quickmem.presentation.app.ai_generative.components.FABAI
import com.pwhs.quickmem.presentation.app.ai_generative.components.NumberOfFlashcard
import com.pwhs.quickmem.presentation.app.ai_generative.components.SelectDifficultLevel
import com.pwhs.quickmem.presentation.app.ai_generative.components.SelectLanguage
import com.pwhs.quickmem.presentation.app.ai_generative.components.SelectQuestionType
import com.pwhs.quickmem.presentation.components.LanguageBottomSheet
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.ads.AdsUtil
import com.pwhs.quickmem.utils.rememberImeState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.StudySetDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.revenuecat.purchases.CustomerInfo

@Composable
@Destination<RootGraph>
fun AIGenerativeScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    viewModel: AIGenerativeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AIGenerativeUiEvent.CreatedStudySet -> {
                    navigator.navigate(
                        StudySetDetailScreenDestination(
                            id = event.studySetId,
                        )
                    )
                }

                is AIGenerativeUiEvent.EarnedCoins -> {
                    Toast.makeText(
                        context,
                        context.getString(event.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is AIGenerativeUiEvent.Error -> {
                    Toast.makeText(
                        context,
                        context.getString(event.message),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    AIGenerative(
        modifier = modifier,
        isLoading = uiState.isLoading,
        title = uiState.title,
        description = uiState.description,
        numberOfFlashcards = uiState.numberOfFlashcards,
        language = uiState.language,
        questionType = uiState.questionType,
        difficultyLevel = uiState.difficulty,
        onTitleChange = { viewModel.onEvent(AIGenerativeUiAction.OnTitleChanged(it)) },
        onDescriptionChange = { viewModel.onEvent(AIGenerativeUiAction.OnDescriptionChanged(it)) },
        onNumberOfFlashcardsChange = {
            viewModel.onEvent(
                AIGenerativeUiAction.OnNumberOfFlashcardsChange(
                    it
                )
            )
        },
        onLanguageChange = { viewModel.onEvent(AIGenerativeUiAction.OnLanguageChanged(it)) },
        onQuestionTypeChange = { viewModel.onEvent(AIGenerativeUiAction.OnQuestionTypeChanged(it)) },
        onDifficultyLevelChange = {
            viewModel.onEvent(
                AIGenerativeUiAction.OnDifficultyLevelChanged(
                    it
                )
            )
        },
        onCreateStudySet = {
            viewModel.onEvent(AIGenerativeUiAction.OnCreateStudySet)
        },
        errorMessage = uiState.errorMessage,
        coins = uiState.coins,
        onEarnCoins = { viewModel.onEvent(AIGenerativeUiAction.OnEarnCoins) },
        customerInfo = uiState.customerInfo,
        onNavigateBack = { navigator.navigateUp() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIGenerative(
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
    onNavigateBack: () -> Unit = {},
) {
    val context = LocalContext.current
    var isGettingAds by rememberSaveable { mutableStateOf(false) }

    val sheetLanguageState = rememberModalBottomSheetState()
    var showBottomSheetLanguage by remember {
        mutableStateOf(false)
    }
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.value, tween(300))
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
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
                title = {
                    Text(
                        text = stringResource(R.string.txt_ai_generative),
                        style = typography.titleLarge.copy(
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
                            style = typography.titleLarge.copy(
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
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .background(color = Color.White, shape = CircleShape)
                                    .border(
                                        width = 2.dp,
                                        color = colorScheme.primary,
                                        shape = CircleShape
                                    )
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
                                    }
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = stringResource(R.string.txt_add),
                                    tint = colorScheme.primary,
                                    modifier = Modifier
                                        .size(30.dp),
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (title.isNotEmpty()) {
                FABAI(
                    onCreateStudySet = onCreateStudySet,
                    isPlus = customerInfo?.activeSubscriptions?.isNotEmpty() == true,
                    numberOfFlashcards = numberOfFlashcards,
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = modifier
                    .verticalScroll(scrollState)
                    .padding(16.dp)
                    .imePadding(),
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.txt_warning_ai_not_gen),
                        color = Color.Red,
                        style = typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Start,
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    value = title,
                    maxLines = 2,
                    isError = errorMessage != null,
                    supportingText = {
                        errorMessage?.let {
                            Text(
                                text = stringResource(it),
                                style = typography.bodyMedium.copy(
                                    color = colorScheme.error,
                                )
                            )
                        }
                    },
                    colors = colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = colorScheme.primary,
                        focusedSupportingTextColor = colorScheme.error,
                        unfocusedSupportingTextColor = colorScheme.error,
                        errorContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    onValueChange = onTitleChange,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.txt_title_required),
                            style = typography.bodyMedium,
                        )
                    }
                )
                OutlinedTextField(
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(vertical = 10.dp),
                    value = description,
                    maxLines = 5,
                    onValueChange = onDescriptionChange,
                    placeholder = {
                        Text(
                            text = stringResource(R.string.txt_description_optional),
                            style = typography.bodyMedium,
                        )
                    },
                    colors = colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = colorScheme.primary,
                        focusedSupportingTextColor = colorScheme.error,
                        unfocusedSupportingTextColor = colorScheme.error,
                        errorContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )

                NumberOfFlashcard(
                    numberOfFlashcards = numberOfFlashcards,
                    onNumberOfFlashcardsChange = onNumberOfFlashcardsChange
                )
                SelectLanguage(
                    language = language,
                    onChooseLanguage = onLanguageChange,
                    onShowBottomSheetLanguage = { showBottomSheetLanguage = it },
                )
                SelectQuestionType(
                    questionType = questionType,
                    onQuestionTypeChange = onQuestionTypeChange,
                )
                SelectDifficultLevel(
                    difficultyLevel = difficultyLevel,
                    onDifficultyLevelChange = onDifficultyLevelChange,
                )
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
        if (showBottomSheetLanguage) {
            LanguageBottomSheet(
                sheetState = sheetLanguageState,
                onDismissRequest = { showBottomSheetLanguage = false },
                onLanguageChange = onLanguageChange,
                language = language,
            )
        }
        LoadingOverlay(isLoading = isLoading || isGettingAds)
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun AIGenerativeScreenPreview() {
    QuickMemTheme {
        AIGenerative()
    }
}