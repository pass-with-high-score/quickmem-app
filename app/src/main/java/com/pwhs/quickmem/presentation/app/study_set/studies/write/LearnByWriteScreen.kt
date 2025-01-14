package com.pwhs.quickmem.presentation.app.study_set.studies.write

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.ArrowCircleUp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.WriteStatus
import com.pwhs.quickmem.domain.model.flashcard.FlashCardResponseModel
import com.pwhs.quickmem.presentation.app.home.HomeUiAction
import com.pwhs.quickmem.presentation.app.home.HomeViewModel
import com.pwhs.quickmem.presentation.app.study_set.studies.component.UnfinishedLearningBottomSheet
import com.pwhs.quickmem.presentation.app.study_set.studies.write.component.ExpandableCard
import com.pwhs.quickmem.presentation.app.study_set.studies.write.component.WriteFlashcardFinish
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.pwhs.quickmem.presentation.components.ShowImageDialog
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.ui.theme.incorrectColor
import com.pwhs.quickmem.utils.rememberImeState
import com.pwhs.quickmem.utils.toColor
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.random.Random

@Destination<RootGraph>(
    navArgs = LearnByWriteArgs::class
)
@Composable
fun LearnByWriteScreen(
    modifier: Modifier = Modifier,
    resultBackNavigator: ResultBackNavigator<Boolean>,
    viewModel: LearnByWriteViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                LearnByWriteUiEvent.Back -> {
                    resultBackNavigator.navigateBack(true)
                }

                LearnByWriteUiEvent.Finished -> {
                    homeViewModel.onEvent(HomeUiAction.UpdateStreak)
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_you_have_finished), Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    LearnByWrite(
        modifier = modifier,
        onEndSessionClick = {
            viewModel.onEvent(LearnByWriteUiAction.OnBackClicked)
        },
        isLoading = uiState.isLoading,
        isEndOfList = uiState.isEndOfList,
        currentCardIndex = uiState.currentCardIndex,
        flashCardList = uiState.flashCardList,
        studySetColor = uiState.studySetColor.hexValue.toColor(),
        writeQuestion = uiState.writeQuestion,
        wrongAnswerCount = uiState.wrongAnswerCount,
        learningTime = uiState.learningTime,
        listWrongAnswer = uiState.listWrongAnswer,
        onContinueLearningClicked = {
            viewModel.onEvent(LearnByWriteUiAction.ContinueLearnWrongAnswer)
        },
        onRestart = {
            viewModel.onEvent(LearnByWriteUiAction.RestartLearn)
        },
        onSubmitAnswer = { id, status, answer ->
            viewModel.onEvent(LearnByWriteUiAction.OnAnswer(id, status, answer))
        },
        isGetAll = uiState.isGetAll,
        isGenerateHint = uiState.isGenerateHint,
        onCreateHintByAI = {
            viewModel.onEvent(
                LearnByWriteUiAction.OnCreateHintByAI
            )
        },
        onSwapCard = {
            viewModel.onEvent(LearnByWriteUiAction.OnSwapCard)
        },
        isSwapCard = uiState.isSwapCard
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnByWrite(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEndOfList: Boolean = false,
    currentCardIndex: Int = 0,
    studySetColor: Color = MaterialTheme.colorScheme.primary,
    flashCardList: List<FlashCardResponseModel> = emptyList(),
    writeQuestion: WriteQuestion? = null,
    onEndSessionClick: () -> Unit = {},
    onRestart: () -> Unit = {},
    wrongAnswerCount: Int = 0,
    learningTime: Long = 0,
    listWrongAnswer: List<WriteQuestion> = emptyList(),
    onContinueLearningClicked: () -> Unit = {},
    onSubmitAnswer: (String, WriteStatus, String) -> Unit = { _, _, _ -> },
    isGetAll: Boolean = false,
    isGenerateHint: Boolean = false,
    onCreateHintByAI: () -> Unit = {},
    onSwapCard: () -> Unit = {},
    isSwapCard: Boolean = false,
) {
    var isImageViewerOpen by remember { mutableStateOf(false) }
    var definitionImageUri by remember { mutableStateOf("") }
    var userAnswer by rememberSaveable { mutableStateOf("") }
    val showHintBottomSheet = remember { mutableStateOf(false) }
    val hintBottomSheetState = rememberModalBottomSheetState()
    var showUnfinishedLearningBottomSheet by remember { mutableStateOf(false) }
    val unFinishedLearningBottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var debounceJob: Job? = null
    val imeState = rememberImeState()
    val scrollState = rememberScrollState()
    LaunchedEffect(key1 = imeState.value) {
        if (imeState.value) {
            scrollState.animateScrollTo(scrollState.viewportSize, tween(300))
        }
    }
    var isDontKnowButtonVisible by remember { mutableStateOf(false) }
    val revealCount = remember { mutableIntStateOf(0) }
    val maxRevealCount = writeQuestion?.term?.length ?: 0
    val revealedIndices = remember { mutableSetOf<Int>() }

    fun isAnswerCorrect(
        userAnswer: String,
        correctAnswer: String,
    ): Boolean {
        return userAnswer.trim()
            .equals(correctAnswer.trim(), ignoreCase = true)
    }

    fun levenshtein(a: String, b: String): Int {
        val costs = IntArray(b.length + 1) { it }
        for (i in 1..a.length) {
            var lastValue = i - 1
            for (j in 1..b.length) {
                val newValue = min(
                    min(
                        costs[j] + 1,
                        lastValue + if (a[i - 1] == b[j - 1]) 0 else 1
                    ),
                    costs[j - 1] + 1
                )
                costs[j - 1] = lastValue
                lastValue = newValue
            }
            costs[b.length] = lastValue
        }
        return costs[b.length]
    }

    fun isAnswerSimilar(
        userAnswer: String,
        correctAnswer: String,
        threshold: Int = 2,
    ): Boolean {
        return levenshtein(
            userAnswer.trim(),
            correctAnswer.trim()
        ) <= threshold
    }

    LaunchedEffect(writeQuestion) {
        isDontKnowButtonVisible = false
        delay(3000)
        isDontKnowButtonVisible = true
    }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    when (isLoading) {
                        true -> Text(stringResource(R.string.txt_loading))
                        false -> when (isEndOfList) {
                            false -> Text("${currentCardIndex + 1}/${flashCardList.size}")
                            true -> Text(stringResource(R.string.txt_finished))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isEndOfList) {
                                onEndSessionClick()
                            } else {
                                showUnfinishedLearningBottomSheet = true
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Default.Clear,
                            contentDescription = stringResource(R.string.txt_back),
                        )
                    }
                },
                actions = {
                    if (!isEndOfList) {
                        IconButton(
                            onClick = onSwapCard
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_swap_card),
                                contentDescription = stringResource(R.string.txt_swap_card),
                                tint = if (isSwapCard) studySetColor.copy(alpha = 0.5f) else studySetColor,
                            )
                        }
                        if (isGetAll) {
                            IconButton(
                                onClick = onRestart
                            ) {
                                Icon(
                                    imageVector = Default.RestartAlt,
                                    contentDescription = stringResource(R.string.txt_restart),
                                    tint = studySetColor
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                showHintBottomSheet.value = true
                            }
                        ) {
                            Icon(
                                imageVector = Default.Lightbulb,
                                contentDescription = stringResource(R.string.txt_hint),
                                tint = studySetColor
                            )
                        }
                    }
                }
            )
        },
    ) { innerPadding ->
        Box {
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val currentProgress by animateFloatAsState(
                    targetValue = currentCardIndex.toFloat() / flashCardList.size.toFloat()
                        .coerceAtLeast(1f),
                )
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = {
                        when (isEndOfList) {
                            false -> currentProgress.coerceAtLeast(0f)
                            true -> 1f
                        }
                    },
                    color = when {
                        currentProgress < 0.2f -> studySetColor.copy(alpha = 0.2f)
                        currentProgress < 0.5f -> studySetColor.copy(alpha = 0.5f)
                        currentProgress < 0.8f -> studySetColor.copy(alpha = 0.8f)
                        else -> studySetColor
                    }
                )
                when (isEndOfList) {
                    false -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(scrollState)
                                .imePadding(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.txt_click_to_icon_to_show_hint_if_you_feel_stuck),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.padding(8.dp)
                            )
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .requiredHeight(300.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                shape = MaterialTheme.shapes.small
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = writeQuestion?.definition ?: "",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = when (writeQuestion?.definition?.length
                                                ?: 0) {
                                                in 0..10 -> 18.sp
                                                in 11..20 -> 16.sp
                                                else -> 14.sp
                                            }
                                        ),
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .padding(bottom = 8.dp)
                                    )
                                    if (writeQuestion?.definitionImageUrl?.isNotEmpty() == true) {
                                        AsyncImage(
                                            model = writeQuestion.definitionImageUrl,
                                            contentDescription = stringResource(R.string.txt_definition_image),
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clickable {
                                                    isImageViewerOpen = true
                                                    definitionImageUri =
                                                        writeQuestion.definitionImageUrl
                                                }
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextField(
                                    value = userAnswer,
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            userAnswer = ""
                                        }
                                    ),
                                    keyboardOptions = KeyboardOptions(
                                        autoCorrectEnabled = false,
                                        imeAction = ImeAction.Done,
                                        showKeyboardOnFocus = true
                                    ),
                                    isError = when {
                                        userAnswer.length > (writeQuestion?.term?.length ?: 0) -> {
                                            !isAnswerCorrect(
                                                userAnswer,
                                                writeQuestion?.term ?: ""
                                            ) && !isAnswerSimilar(
                                                userAnswer,
                                                writeQuestion?.term ?: ""
                                            )
                                        }

                                        else -> false
                                    },
                                    placeholder = {
                                        Text(
                                            text = stringResource(R.string.txt_enter_your_answer),
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    },
                                    trailingIcon = {
                                        AnimatedVisibility(
                                            visible = isDontKnowButtonVisible && userAnswer.isEmpty()
                                        ) {
                                            TextButton(
                                                onClick = {
                                                    debounceJob?.cancel()
                                                    debounceJob = scope.launch {
                                                        delay(500)
                                                        revealCount.intValue = 0
                                                        revealedIndices.clear()
                                                        onSubmitAnswer(
                                                            writeQuestion?.id ?: "",
                                                            WriteStatus.SKIPPED,
                                                            ""
                                                        )
                                                    }
                                                }
                                            ) {
                                                Text(
                                                    text = stringResource(R.string.txt_don_t_know),
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        color = studySetColor,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                )
                                            }
                                        }
                                    },
                                    onValueChange = {
                                        userAnswer = it
                                    },
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        focusedIndicatorColor = studySetColor,
                                        unfocusedIndicatorColor = studySetColor,
                                        errorContainerColor = Color.Transparent,
                                        errorTextColor = incorrectColor,
                                        errorIndicatorColor = incorrectColor
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 8.dp),
                                )

                                IconButton(
                                    onClick = {
                                        debounceJob?.cancel()
                                        debounceJob = scope.launch {
                                            delay(500)
                                            revealCount.intValue = 0
                                            revealedIndices.clear()
                                            if (userAnswer.isNotEmpty() && isAnswerCorrect(
                                                    userAnswer,
                                                    writeQuestion?.term ?: ""
                                                ) || isAnswerSimilar(
                                                    userAnswer,
                                                    writeQuestion?.term ?: ""
                                                )
                                            ) {
                                                onSubmitAnswer(
                                                    writeQuestion?.id ?: "",
                                                    WriteStatus.CORRECT,
                                                    userAnswer
                                                )
                                                userAnswer = ""
                                            } else {
                                                onSubmitAnswer(
                                                    writeQuestion?.id ?: "",
                                                    WriteStatus.WRONG,
                                                    userAnswer
                                                )
                                                userAnswer = ""
                                            }
                                        }
                                    },
                                    enabled = userAnswer.isNotEmpty(),
                                    colors = IconButtonDefaults.iconButtonColors(
                                        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(
                                            alpha = 0.5f
                                        ),
                                        contentColor = studySetColor
                                    ),
                                    modifier = Modifier.rotate(degrees = 90f)
                                ) {
                                    Icon(
                                        imageVector = Default.ArrowCircleUp,
                                        contentDescription = stringResource(R.string.txt_submit),
                                    )
                                }
                            }
                        }
                    }

                    true -> {
                        WriteFlashcardFinish(
                            isEndOfList = true,
                            wrongAnswerCount = wrongAnswerCount,
                            correctAnswerCount = flashCardList.size - wrongAnswerCount,
                            studySetColor = studySetColor,
                            learningTime = learningTime,
                            onContinueLearningClicked = onContinueLearningClicked,
                            listWrongAnswer = listWrongAnswer,
                            flashCardSize = flashCardList.size,
                            onRestartClicked = onRestart,
                            isGetAll = isGetAll
                        )
                    }
                }
            }
            LoadingOverlay(isLoading = isLoading)
            // Image Viewer Dialog
            if (isImageViewerOpen) {
                ShowImageDialog(
                    definitionImageUri = definitionImageUri,
                    onDismissRequest = {
                        isImageViewerOpen = false
                        definitionImageUri = ""
                    }
                )
            }

            fun generateHint(answer: String, revealCount: Int): String {
                val indices = answer.indices.filter { answer[it] != ' ' && it !in revealedIndices }
                    .shuffled(Random(System.currentTimeMillis()))
                revealedIndices.addAll(indices.take(revealCount - revealedIndices.size))
                return answer.mapIndexed { index, c ->
                    if (c == ' ' || index in revealedIndices) c else '_'
                }.joinToString("")
            }

            if (showHintBottomSheet.value) {
                val numOfWords = writeQuestion?.term?.split(" ")?.size ?: 0
                val length = writeQuestion?.term?.length ?: 0
                val hint =
                    writeQuestion?.term?.let { generateHint(it, revealCount.intValue) } ?: ""

                ModalBottomSheet(
                    sheetState = hintBottomSheetState,
                    onDismissRequest = { showHintBottomSheet.value = false }
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = stringResource(R.string.txt_hint),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )

                                IconButton(onClick = onCreateHintByAI) {
                                    Icon(
                                        painter = painterResource(R.drawable.ic_generative_ai),
                                        contentDescription = stringResource(R.string.txt_ai_hint),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                        }

                        item {
                            if (!isGenerateHint) {
                                HorizontalDivider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                            if (isGenerateHint) {
                                LinearProgressIndicator(
                                    color = studySetColor,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                            writeQuestion?.aiHint?.let {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(R.string.txt_hint_from_answer),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            ExpandableCard(
                                title = stringResource(R.string.txt_the_answer_has),
                                content = {
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Text(
                                            text = stringResource(
                                                R.string.txt_answer_has_words,
                                                numOfWords
                                            ),
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        Text(
                                            text = stringResource(
                                                R.string.txt_answer_has_characters,
                                                length
                                            ),
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        if (writeQuestion?.hint?.isNotEmpty() == true) {
                                            Text(
                                                text = stringResource(
                                                    R.string.txt_hint_answer,
                                                    writeQuestion.hint
                                                ),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = stringResource(R.string.txt_random_character),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            Text(
                                text = hint,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Button(
                                onClick = {
                                    if (revealCount.intValue < maxRevealCount) {
                                        revealCount.intValue += 1
                                    }
                                },
                                enabled = revealCount.intValue < maxRevealCount,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = studySetColor
                                )
                            ) {
                                Text(text = stringResource(R.string.txt_show_more_hint))
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

            }

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
    }
}

@Preview(showSystemUi = true)
@Composable
private fun LearnByWriteScreenPreview() {
    QuickMemTheme {
        LearnByWrite()
    }
}