package com.pwhs.quickmem.presentation.app.study_set.studies.component

import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.LearnFrom
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyTopAppBar(
    modifier: Modifier = Modifier,
    currentCardIndex: Int = 0,
    totalCards: Int = 0,
    onBackClicked: () -> Unit = {},
    isEnOfSet: Boolean = false,
    onRestartClicked: () -> Unit = {},
    shouldShowRestart: Boolean = true,
    isGetAll: Boolean = false,
    learnFrom: LearnFrom = LearnFrom.STUDY_SET,
    isSwapCard: Boolean = false,
    onSwapCard: () -> Unit = {},
    studySetColor: Color = MaterialTheme.colorScheme.primary,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = if (isEnOfSet) stringResource(R.string.txt_congratulations) else stringResource(
                    R.string.txt_title_learn_flashcard, currentCardIndex, totalCards
                ),
                style = typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                )
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClicked
            ) {
                Icon(
                    imageVector = Default.Clear,
                    contentDescription = stringResource(R.string.txt_back)
                )
            }
        },
        actions = {
            if (shouldShowRestart) {
                IconButton(
                    onClick = onSwapCard
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_swap_card),
                        contentDescription = stringResource(R.string.txt_swap_card),
                        tint = if (isSwapCard) studySetColor.copy(alpha = 0.5f) else studySetColor,
                    )
                }
                if (learnFrom != LearnFrom.FOLDER && isGetAll) {
                    IconButton(
                        onClick = onRestartClicked
                    ) {
                        Icon(
                            imageVector = Default.RestartAlt,
                            contentDescription = stringResource(R.string.txt_restart),
                            tint = studySetColor
                        )
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun StudyTopAppBarPreview() {
    QuickMemTheme {
        StudyTopAppBar()
    }
}