package com.pwhs.quickmem.presentation.app.explore.create_study_set_ai

import androidx.annotation.StringRes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.DifficultyLevel
import com.pwhs.quickmem.core.data.enums.QuestionType
import com.pwhs.quickmem.presentation.app.explore.create_study_set_ai.components.FABAI
import com.pwhs.quickmem.presentation.app.explore.create_study_set_ai.components.NumberOfFlashcard
import com.pwhs.quickmem.presentation.app.explore.create_study_set_ai.components.SelectDifficultLevel
import com.pwhs.quickmem.presentation.app.explore.create_study_set_ai.components.SelectLanguage
import com.pwhs.quickmem.presentation.app.explore.create_study_set_ai.components.SelectQuestionType
import com.pwhs.quickmem.presentation.components.LanguageBottomSheet
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.pwhs.quickmem.utils.rememberImeState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStudySetAITab(
    modifier: Modifier = Modifier,
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
    isPlus: Boolean = false,
) {
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
        floatingActionButton = {
            if (title.isNotEmpty()) {
                FABAI(
                    onCreateStudySet = onCreateStudySet,
                    isPlus = isPlus,
                    numberOfFlashcards = numberOfFlashcards,
                )
            }
        },
        bottomBar = {
            Spacer(modifier = Modifier.height(100.dp))
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = modifier
                    .verticalScroll(scrollState)
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
    }

    if (showBottomSheetLanguage) {
        LanguageBottomSheet(
            sheetState = sheetLanguageState,
            onDismissRequest = { showBottomSheetLanguage = false },
            onLanguageChange = onLanguageChange,
            language = language,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateStudySetAITabPreview() {
    QuickMemTheme {
        CreateStudySetAITab(
            modifier = Modifier.padding(16.dp),
            numberOfFlashcards = 5,
            language = "English",
        )
    }
}