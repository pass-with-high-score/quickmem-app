package com.pwhs.quickmem.presentation.app.flashcard.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.components.BottomSheetItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    onShowHintClicked: (Boolean) -> Unit,
    onShowExplanationClicked: (Boolean) -> Unit,
    isEdit: Boolean = false,
    onDeleteFlashcardClicked: () -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BottomSheetItem(
                title = stringResource(R.string.txt_hint_to_question),
                icon = R.drawable.ic_add,
                onClick = {
                    onShowHintClicked(true)
                    onDismissRequest()
                },
            )
            BottomSheetItem(
                title = stringResource(R.string.txt_explanation_to_answer),
                icon = R.drawable.ic_add,
                onClick = {
                    onShowExplanationClicked(true)
                    onDismissRequest()
                },
            )
            if (isEdit) {
                BottomSheetItem(
                    title = stringResource(R.string.txt_delete_flashcard),
                    imageVector = Icons.Default.Delete,
                    onClick = {
                        onDeleteFlashcardClicked()
                        onDismissRequest()
                    },
                )
            }
        }
    }
}