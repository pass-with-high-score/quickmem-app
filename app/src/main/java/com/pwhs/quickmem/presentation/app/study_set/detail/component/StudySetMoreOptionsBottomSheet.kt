package com.pwhs.quickmem.presentation.app.study_set.detail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.pwhs.quickmem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudySetMoreOptionsBottomSheet(
    modifier: Modifier = Modifier,
    isOwner: Boolean,
    onAddToFolder: () -> Unit,
    onEditStudySet: () -> Unit,
    onDeleteStudySet: () -> Unit,
    onInfoStudySet: () -> Unit,
    showMoreBottomSheet: Boolean,
    sheetShowMoreState: SheetState,
    onDismissRequest: () -> Unit,
    onResetProgress: () -> Unit,
    onCopyStudySet: () -> Unit,
    onReportClick: () -> Unit
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
                if (isOwner) {
                    ItemMenuBottomSheet(
                        onClick = onEditStudySet,
                        icon = Outlined.Edit,
                        title = stringResource(id = R.string.txt_edit_study_set)
                    )
                    ItemMenuBottomSheet(
                        onClick = onAddToFolder,
                        icon = Outlined.Folder,
                        title = stringResource(R.string.txt_add_to_folder)
                    )
                    ItemMenuBottomSheet(
                        onClick = onResetProgress,
                        icon = Outlined.Refresh,
                        title = stringResource(R.string.txt_reset_progress)
                    )
                }
                ItemMenuBottomSheet(
                    onClick = onCopyStudySet,
                    icon = Outlined.ContentCopy,
                    title = stringResource(R.string.txt_make_a_copy)
                )
                ItemMenuBottomSheet(
                    onClick = onInfoStudySet,
                    icon = Outlined.Info,
                    title = stringResource(R.string.txt_study_set_info)
                )
                if (isOwner) {
                    ItemMenuBottomSheet(
                        onClick = onDeleteStudySet,
                        icon = Default.DeleteOutline,
                        title = stringResource(R.string.txt_delete_study_set),
                        color = Color.Red
                    )
                }
                if (!isOwner) {
                    ItemMenuBottomSheet(
                        onClick = onReportClick,
                        icon = Outlined.Report,
                        title = stringResource(R.string.txt_report_study_set)
                    )
                }
            }
        }
    }
}