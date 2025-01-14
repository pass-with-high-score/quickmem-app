package com.pwhs.quickmem.presentation.app.settings.user_info.change_role

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.core.data.enums.UserRole
import com.pwhs.quickmem.presentation.app.settings.component.SettingTopAppBar
import com.pwhs.quickmem.presentation.app.settings.user_info.change_role.component.RoleItem
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.result.ResultBackNavigator

@Destination<RootGraph>(navArgs = ChangeRoleArgs::class)
@Composable
fun ChangeRoleScreen(
    modifier: Modifier = Modifier,
    viewModel: ChangeRoleViewModel = hiltViewModel(),
    resultNavigator: ResultBackNavigator<Boolean>
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ChangeRoleUiEvent.ShowUnderageDialog -> {
                    dialogMessage = context.getString(
                        R.string.dialog_message_underage,
                        event.message
                    )
                    showDialog = true
                }

                is ChangeRoleUiEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is ChangeRoleUiEvent.RoleChangedSuccessfully -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_role_updated_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    resultNavigator.navigateBack(true)
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(R.string.dialog_title_not_eligible)) },
            text = { Text(dialogMessage) },
            confirmButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text(text = stringResource(R.string.txt_ok))
                }
            }
        )
    }

    ChangeRole(
        modifier = modifier,
        role = uiState.role,
        isLoading = uiState.isLoading,
        onRoleChanged = { role ->
            viewModel.onEvent(ChangeRoleUiAction.SelectRole(role))
        },
        onNavigateBack = {
            resultNavigator.navigateBack(false)
        },
        onSaved = {
            viewModel.onEvent(ChangeRoleUiAction.SaveRole)
        }
    )
}

@Composable
private fun ChangeRole(
    modifier: Modifier = Modifier,
    role: UserRole,
    isLoading: Boolean = false,
    onRoleChanged: (UserRole) -> Unit,
    onNavigateBack: () -> Unit,
    onSaved: () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            SettingTopAppBar(
                title = stringResource(R.string.txt_role),
                onNavigateBack = onNavigateBack,
                onSaved = onSaved,
                enabled = !isLoading
            )
        }
    ) { innerPadding ->
        Box {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Card(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column {
                        RoleItem(
                            role = UserRole.STUDENT,
                            isSelected = role == UserRole.STUDENT,
                            onRoleChanged = { onRoleChanged(UserRole.STUDENT) }
                        )
                        HorizontalDivider()
                        RoleItem(
                            role = UserRole.TEACHER,
                            isSelected = role == UserRole.TEACHER,
                            onRoleChanged = { onRoleChanged(UserRole.TEACHER) }
                        )
                    }
                }
            }
            LoadingOverlay(isLoading = isLoading)
        }
    }
}


@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
private fun ChangeRoleScreenPreview() {
    QuickMemTheme {
        ChangeRole(
            role = UserRole.STUDENT,
            onRoleChanged = {},
            onNavigateBack = {},
            onSaved = {}
        )
    }
}
