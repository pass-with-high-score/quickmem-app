package com.pwhs.quickmem.presentation.app.study_set.studies.flip

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.msusman.compose.cardstack.CardStack
import com.msusman.compose.cardstack.Direction
import com.msusman.compose.cardstack.Duration
import com.msusman.compose.cardstack.SwipeDirection
import com.msusman.compose.cardstack.SwipeMethod
import com.msusman.compose.cardstack.rememberStackState
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.LearnFrom
import com.pwhs.quickmem.domain.model.flashcard.FlashCardResponseModel
import com.pwhs.quickmem.presentation.StandardUiAction
import com.pwhs.quickmem.presentation.StandardViewModel
import com.pwhs.quickmem.presentation.app.study_set.component.StudyCardBottomSheet
import com.pwhs.quickmem.presentation.app.study_set.studies.flip.component.FlipFlashCardButton
import com.pwhs.quickmem.presentation.app.study_set.studies.flip.component.FlipFlashCardFinish
import com.pwhs.quickmem.presentation.app.study_set.studies.flip.component.FlipFlashCardIconButton
import com.pwhs.quickmem.presentation.app.study_set.studies.flip.component.FlipFlashCardStatusRow
import com.pwhs.quickmem.presentation.app.study_set.studies.flip.component.StudyFlipFlashCard
import com.pwhs.quickmem.presentation.app.study_set.studies.component.StudyTopAppBar
import com.pwhs.quickmem.presentation.app.study_set.studies.component.UnfinishedLearningBottomSheet
import com.pwhs.quickmem.presentation.component.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.util.toColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.delay

@Destination<RootGraph>(
    navArgs = FlipFlashCardArgs::class
)
@Composable
fun FlipFlashCardScreen(
    modifier: Modifier = Modifier,
    resultNavigator: ResultBackNavigator<Boolean>,
    viewModel: FlipFlashCardViewModel = hiltViewModel(),
    standardViewModel: StandardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                FlipFlashCardUiEvent.Finished -> {
                    standardViewModel.onEvent(StandardUiAction.UpdateStreak)
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_you_have_finished),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                FlipFlashCardUiEvent.Back -> {
                    resultNavigator.navigateBack(true)
                }
            }

        }
    }
    FlipFlashCard(
        modifier = modifier,
        flashCards = uiState.flashCardList,
        isLoading = uiState.isLoading,
        isSwipingLeft = uiState.isSwipingLeft,
        isSwipingRight = uiState.isSwipingRight,
        isEndOfList = uiState.isEndOfList,
        learningTime = uiState.learningTime,
        onEndSessionClick = {
            viewModel.onEvent(FlipFlashCardUiAction.OnBackClicked)
        },
        currentCardIndex = uiState.currentCardIndex,
        countKnown = uiState.countKnown,
        countStillLearning = uiState.countStillLearning,
        onUpdatedCardIndex = { index ->
            viewModel.onEvent(FlipFlashCardUiAction.OnUpdateCardIndex(index))
        },
        studySetColor = uiState.studySetColor.hexValue.toColor(),
        onSwipeRight = { isSwipingRight ->
            viewModel.onEvent(FlipFlashCardUiAction.OnSwipeRight(isSwipingRight))
        },
        onSwipeLeft = { isSwipingLeft ->
            viewModel.onEvent(FlipFlashCardUiAction.OnSwipeLeft(isSwipingLeft))
        },
        onUpdateCountKnown = { isIncrease, flashCardId ->
            viewModel.onEvent(FlipFlashCardUiAction.OnUpdateCountKnown(isIncrease, flashCardId))
        },
        onUpdateCountStillLearning = { isIncrease, flashCardId ->
            viewModel.onEvent(
                FlipFlashCardUiAction.OnUpdateCountStillLearning(
                    isIncrease,
                    flashCardId
                )
            )
        },
        onRestartClicked = {
            viewModel.onEvent(FlipFlashCardUiAction.OnRestartClicked)
        },
        onContinueLearningClicked = {
            viewModel.onEvent(FlipFlashCardUiAction.OnContinueLearningClicked)
        },
        isGetAll = uiState.isGetAll,
        learnFrom = uiState.learnFrom,
        onSwapCard = {
            viewModel.onEvent(FlipFlashCardUiAction.OnSwapCard)
        },
        isSwapCard = uiState.isSwapCard
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlipFlashCard(
    modifier: Modifier = Modifier,
    currentCardIndex: Int = 0,
    countKnown: Int = 0,
    countStillLearning: Int = 0,
    flashCards: List<FlashCardResponseModel> = emptyList(),
    studySetColor: Color = MaterialTheme.colorScheme.primary,
    isLoading: Boolean = false,
    isSwipingLeft: Boolean = false,
    isSwipingRight: Boolean = false,
    isEndOfList: Boolean = false,
    learningTime: Long = 0L,
    onEndSessionClick: () -> Unit = { },
    onUpdatedCardIndex: (Int) -> Unit = { },
    onSwipeRight: (Boolean) -> Unit = { },
    onSwipeLeft: (Boolean) -> Unit = { },
    onUpdateCountKnown: (Boolean, String) -> Unit = { _, _ -> },
    onUpdateCountStillLearning: (Boolean, String) -> Unit = { _, _ -> },
    onRestartClicked: () -> Unit = { },
    onContinueLearningClicked: () -> Unit = { },
    isGetAll: Boolean = false,
    learnFrom: LearnFrom = LearnFrom.STUDY_SET,
    onSwapCard: () -> Unit = {},
    isSwapCard: Boolean = false,
) {
    var showHintBottomSheet by remember {
        mutableStateOf(false)
    }
    var showExplanationBottomSheet by remember {
        mutableStateOf(false)
    }
    val hintBottomSheetState = rememberModalBottomSheetState()
    val explanationBottomSheetState = rememberModalBottomSheetState()
    var showUnfinishedLearningBottomSheet by remember { mutableStateOf(false) }
    val unFinishedLearningBottomSheetState = rememberModalBottomSheetState()
    val stillLearningColor = Color(0xffd05700)
    val knownColor = Color(0xff18ae79)
    val stackState = rememberStackState()
    val suggestedText = listOf(
        stringResource(R.string.txt_tap_the_card_to_flip),
        stringResource(R.string.txt_swipe_left_to_mark_as_known),
        stringResource(R.string.txt_swipe_right_to_mark_as_still_learning)
    )
    var currentTextIndex by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(5000)
            currentTextIndex = (currentTextIndex + 1) % suggestedText.size
        }
    }
    var isFlipCard by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            StudyTopAppBar(
                currentCardIndex = currentCardIndex,
                totalCards = flashCards.size,
                onBackClicked = {
                    if (isEndOfList) {
                        onEndSessionClick()
                    } else {
                        showUnfinishedLearningBottomSheet = true
                    }
                },
                isEnOfSet = isEndOfList,
                onRestartClicked = {
                    onRestartClicked()
                    stackState.reset()
                },
                shouldShowRestart = !isEndOfList,
                isGetAll = isGetAll,
                learnFrom = learnFrom,
                isSwapCard = isSwapCard,
                onSwapCard = {
                    onSwapCard()
                    stackState.reset()
                },
                studySetColor = studySetColor
            )
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .padding(innerPadding)
                .zIndex(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
            ) {
                val currentProgress by animateFloatAsState(
                    targetValue = if (flashCards.isNotEmpty()) currentCardIndex / flashCards.size.toFloat() else 0f
                )
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = {
                        if (currentProgress.isNaN()) 0f else currentProgress
                    },
                    color = when {
                        currentProgress < 0.2f -> studySetColor.copy(alpha = 0.2f)
                        currentProgress < 0.5f -> studySetColor.copy(alpha = 0.5f)
                        currentProgress < 0.8f -> studySetColor.copy(alpha = 0.8f)
                        else -> studySetColor
                    }
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .zIndex(1f)
                ) {
                    when (isEndOfList) {
                        false -> {
                            Column(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                FlipFlashCardStatusRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    countStillLearning = countStillLearning,
                                    countKnown = countKnown,
                                    stillLearningColor = stillLearningColor,
                                    knownColor = knownColor,
                                    suggestedText = suggestedText,
                                    currentTextIndex = currentTextIndex,
                                    isSwipingLeft = isSwipingLeft,
                                    isSwipingRight = isSwipingRight
                                )
                                if (flashCards.isNotEmpty()) {
                                    CardStack(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .zIndex(2f),
                                        stackState = stackState,
                                        cardElevation = 10.dp,
                                        scaleRatio = 0.95f,
                                        rotationMaxDegree = 0,
                                        displacementThreshold = 120.dp,
                                        animationDuration = Duration.NORMAL,
                                        visibleCount = minOf(3, flashCards.size),
                                        stackDirection = Direction.Bottom,
                                        swipeDirection = SwipeDirection.FREEDOM,
                                        swipeMethod = SwipeMethod.AUTOMATIC_AND_MANUAL,
                                        items = flashCards,
                                        onSwiped = { index, direction ->
                                            isFlipCard = false
                                            if (index in flashCards.indices) {
                                                onUpdatedCardIndex(index)
                                                val flashCardId = flashCards[index].id
                                                when (direction) {
                                                    Direction.Left, Direction.TopAndLeft, Direction.BottomAndLeft, Direction.Top -> {
                                                        onUpdateCountStillLearning(
                                                            true,
                                                            flashCardId
                                                        )
                                                    }

                                                    Direction.Right, Direction.TopAndRight, Direction.BottomAndRight, Direction.Bottom -> {
                                                        onUpdateCountKnown(true, flashCardId)
                                                    }

                                                    else -> {
                                                    }
                                                }
                                            }
                                        },
                                        onChange = { direction ->
                                            when (direction) {
                                                Direction.Left, Direction.TopAndLeft, Direction.BottomAndLeft, Direction.Top -> {
                                                    onSwipeLeft(true)
                                                    onSwipeRight(false)
                                                }

                                                Direction.Right, Direction.TopAndRight, Direction.BottomAndRight, Direction.Bottom -> {
                                                    onSwipeRight(true)
                                                    onSwipeLeft(false)
                                                }

                                                Direction.None -> {
                                                    onSwipeLeft(false)
                                                    onSwipeRight(false)
                                                }
                                            }
                                        }
                                    ) { flashcard ->
                                        var flipCard by remember {
                                            mutableStateOf(false)
                                        }
                                        StudyFlipFlashCard(
                                            flashCard = flashcard,
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            isSwipingLeft = isSwipingLeft,
                                            isSwipingRight = isSwipingRight,
                                            stillLearningColor = stillLearningColor,
                                            knownColor = knownColor,
                                            isShowingEffect = currentCardIndex == flashCards.indexOf(
                                                flashcard
                                            ),
                                            isFlipped = flipCard,
                                            flashCardColor = studySetColor,
                                            onChangeFlipped = {
                                                isFlipCard = it
                                                flipCard = it
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        true -> {
                            FlipFlashCardFinish(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp),
                                isEndOfList = true,
                                countStillLearning = countStillLearning,
                                countKnown = countKnown,
                                studySetColor = studySetColor,
                                flashCardSize = flashCards.size,
                                learningTime = learningTime,
                                onContinueLearningClicked = {
                                    onContinueLearningClicked()
                                    stackState.reset()
                                },
                                onRestartClicked = {
                                    onRestartClicked()
                                    stackState.reset()
                                },
                                isGetAll = isGetAll
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = !isEndOfList,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp)
                    ) {
                        FlipFlashCardIconButton(
                            knownColor = stillLearningColor,
                            contentDescription = stringResource(R.string.txt_swipe_left),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            onClick = {
                                stackState.swipe(Direction.Left)
                            }
                        )
                        if (flashCards.isNotEmpty()) {
                            if (flashCards.getOrNull(currentCardIndex)?.hint?.isNotEmpty() == true && !isFlipCard) {
                                FlipFlashCardButton(
                                    onClick = {
                                        showHintBottomSheet = true
                                    },
                                    studySetColor = studySetColor,
                                    title = stringResource(R.string.txt_show_hint)
                                )
                            }

                            if (flashCards.getOrNull(currentCardIndex)?.explanation?.isNotEmpty() == true && isFlipCard) {
                                FlipFlashCardButton(
                                    onClick = {
                                        showExplanationBottomSheet = true
                                    },
                                    studySetColor = studySetColor,
                                    title = stringResource(R.string.txt_show_explanation)
                                )
                            }
                        }
                        FlipFlashCardIconButton(
                            knownColor = knownColor,
                            contentDescription = stringResource(R.string.txt_swipe_right),
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            onClick = {
                                stackState.swipe(Direction.Right)
                            }
                        )

                    }
                }
            }
            LoadingOverlay(
                isLoading = isLoading,
                color = studySetColor.copy(alpha = 0.6f)
            )
        }
    }

    StudyCardBottomSheet(
        modifier = Modifier,
        title = stringResource(R.string.txt_hint),
        contentText = flashCards.getOrNull(currentCardIndex)?.hint ?: "",
        onDismiss = {
            showHintBottomSheet = false
        },
        isShowBottomSheet = showHintBottomSheet,
        sheetState = hintBottomSheetState
    )

    StudyCardBottomSheet(
        modifier = Modifier,
        title = stringResource(R.string.txt_explanation),
        contentText = flashCards.getOrNull(currentCardIndex)?.explanation ?: "",
        onDismiss = {
            showExplanationBottomSheet = false
        },
        isShowBottomSheet = showExplanationBottomSheet,
        sheetState = explanationBottomSheetState
    )

    if (showUnfinishedLearningBottomSheet) {
        UnfinishedLearningBottomSheet(
            onDismissRequest = {
                showUnfinishedLearningBottomSheet = false
            },
            onKeepLearningClick = {
                showUnfinishedLearningBottomSheet = false
            },
            onEndSessionClick = {
                onEndSessionClick()
                showUnfinishedLearningBottomSheet = false
            },
            sheetState = unFinishedLearningBottomSheetState
        )
    }

}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun FlipFlashCardScreenPreview() {
    QuickMemTheme {
        FlipFlashCard(
            isEndOfList = true,
        )
    }
}