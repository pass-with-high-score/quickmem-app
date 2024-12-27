package com.pwhs.quickmem.presentation.app.folder.detail.component

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.app.study_set.detail.component.ItemMenuBottomSheet
import com.pwhs.quickmem.ui.theme.QuickMemTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderMenuBottomSheet(
    isOwner: Boolean,
    modifier: Modifier = Modifier,
    onEditFolder: () -> Unit = {},
    onDeleteFolder: () -> Unit = {},
    onShareFolder: () -> Unit = {},
    showMoreBottomSheet: Boolean = false,
    sheetShowMoreState: SheetState = rememberModalBottomSheetState(),
    onDismissRequest: () -> Unit = {},
    onReportClick: () -> Unit = {}
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
                        onClick = onEditFolder,
                        icon = Outlined.Edit,
                        title = stringResource(R.string.txt_edit_folder),
                    )
                }
                ItemMenuBottomSheet(
                    onClick = onShareFolder,
                    icon = Default.IosShare,
                    title = stringResource(R.string.txt_share_folder)
                )

                if (!isOwner) {
                    ItemMenuBottomSheet(
                        onClick = onReportClick,
                        icon = Outlined.Report,
                        title = stringResource(R.string.txt_report_folder),
                    )
                }


                if (isOwner) {
                    ItemMenuBottomSheet(
                        onClick = onDeleteFolder,
                        icon = Default.DeleteOutline,
                        title = stringResource(R.string.txt_delete_folder),
                        color = Color.Red
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun FolderMenuBottomSheetPreview() {
    QuickMemTheme {
        FolderMenuBottomSheet(
            showMoreBottomSheet = true,
            isOwner = true
        )
    }
}