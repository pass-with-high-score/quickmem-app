package com.pwhs.quickmem.presentation.app.report

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pwhs.quickmem.R
import com.pwhs.quickmem.presentation.components.LoadingOverlay
import com.pwhs.quickmem.ui.theme.QuickMemTheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination<RootGraph>(navArgs = ReportArgs::class)
@Composable
fun ReportScreen(
    navigator: DestinationsNavigator,
    viewModel: ReportViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ReportUiEvent.OnSubmitReport -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.txt_report_submitted), Toast.LENGTH_SHORT
                    ).show()
                    navigator.popBackStack()
                }

                is ReportUiEvent.OnError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Report(
        isLoading = uiState.isLoading,
        title = uiState.reportType?.title ?: R.string.txt_report_this_class,
        questionText = uiState.reportType?.questionText ?: R.string.txt_why_report_this_class,
        options = uiState.reportType?.options ?: emptyList(),
        selectedReason = uiState.reason,
        onReasonSelected = { reason ->
            val res = context.getString(reason)
            viewModel.onEvent(ReportUiAction.OnReasonChanged(res))
        },
        onContinue = {
            viewModel.onEvent(ReportUiAction.OnSubmitReport)
        },
        onBackClick = { navigator.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Report(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    isLoading: Boolean = false,
    @StringRes questionText: Int,
    @StringRes options: List<Int>,
    selectedReason: String,
    onReasonSelected: (Int) -> Unit,
    onContinue: () -> Unit,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.txt_back)
                        )
                    }
                },
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 35.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(title),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            fontSize = 20.sp
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(questionText),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                options.forEach { reason ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onReasonSelected(reason)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedReason == stringResource(reason),
                            onClick = { onReasonSelected(reason) }
                        )
                        Text(
                            text = stringResource(reason),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .padding(vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedReason.isEmpty()) Color.Gray else MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.extraLarge,
                    enabled = selectedReason.isNotEmpty()
                ) {
                    Text(
                        text = stringResource(R.string.txt_continue),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            LoadingOverlay(isLoading = isLoading)
        }
    }
}

@Preview(showSystemUi = true)
@Preview(showSystemUi = true, locale = "vi")
@Composable
fun ReportScreenPreview() {
    QuickMemTheme {
        Report(
            title = R.string.txt_report_this_class,
            questionText = R.string.txt_why_report_this_class,
            options = listOf(
                R.string.txt_class_name_misleading,
                R.string.txt_class_inappropriate,
                R.string.txt_class_cheating,
                R.string.txt_class_ip_violation,
                R.string.txt_class_joining_trouble
            ),
            selectedReason = "",
            onReasonSelected = {},
            onContinue = {},
            onBackClick = {}
        )
    }
}
