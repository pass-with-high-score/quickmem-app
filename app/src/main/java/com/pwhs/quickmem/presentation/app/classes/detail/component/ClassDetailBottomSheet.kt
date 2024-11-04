package com.pwhs.quickmem.presentation.app.classes.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.pwhs.quickmem.presentation.app.study_set.detail.component.ItemMenuBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailBottomSheet(
    modifier: Modifier = Modifier,
    onInviteMembers: () -> Unit = {},
    onEditClass: () -> Unit = {},
    onDeleteClass: () -> Unit = {},
    onShareClass: () -> Unit = {},
    onReportClass: () -> Unit = {},
    showMoreBottomSheet: Boolean = false,
    sheetShowMoreState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit = {},
) {
    if (showMoreBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            sheetState = sheetShowMoreState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                ItemMenuBottomSheet(
                    onClick = onEditClass,
                    icon = Outlined.Edit,
                    title = "Edit"
                )
                ItemMenuBottomSheet(
                    onClick = onShareClass,
                    icon = Default.IosShare,
                    title = "Share Class"
                )
                ItemMenuBottomSheet(
                    onClick = onReportClass,
                    icon = Outlined.Report,
                    title = "Report Class"
                )
                ItemMenuBottomSheet(
                    onClick = onDeleteClass,
                    icon = Default.DeleteOutline,
                    title = "Delete Class",
                    color = Color.Red
                )
            }
        }
    }
}