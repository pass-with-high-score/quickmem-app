package com.pwhs.quickmem.presentation.app.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pwhs.quickmem.core.utils.Resources
import com.pwhs.quickmem.domain.model.report.CreateReportRequestModel
import com.pwhs.quickmem.domain.repository.ReportRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = Channel<ReportUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val reportType: ReportTypeEnum =
            savedStateHandle.get<ReportTypeEnum>("reportType") ?: ReportTypeEnum.CLASS
        val reportedEntityId: String = savedStateHandle.get<String>("reportedEntityId") ?: ""
        val ownerOfReportedEntity: String =
            savedStateHandle.get<String>("ownerOfReportedEntity") ?: ""
        _uiState.update {
            it.copy(
                reportType = reportType,
                reportedEntityId = reportedEntityId,
                ownerOfReportedEntity = ownerOfReportedEntity
            )
        }
    }

    fun onEvent(event: ReportUiAction) {
        when (event) {
            is ReportUiAction.OnReasonChanged -> {
                _uiState.value = _uiState.value.copy(reason = event.reason)
            }

            is ReportUiAction.OnSubmitReport -> {
                sendReport()
            }
        }
    }

    private fun sendReport() {
        viewModelScope.launch {
            val reason = _uiState.value.reason
            val reportedEntityId = _uiState.value.reportedEntityId
            val reportType = _uiState.value.reportType
            val ownerOfReportedEntity = _uiState.value.ownerOfReportedEntity

            val reportRequestModel = CreateReportRequestModel(
                reason = reason,
                reportedEntityId = reportedEntityId,
                ownerOfReportedEntityId = ownerOfReportedEntity,
                reportedType = reportType?.name ?: ""
            )

            reportRepository.createReport(
                createReportRequestModel = reportRequestModel
            ).collect { resources ->
                when (resources) {
                    is Resources.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.send(ReportUiEvent.OnError(resources.message ?: ""))
                    }

                    is Resources.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is Resources.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _uiEvent.send(ReportUiEvent.OnSubmitReport)
                    }
                }
            }
        }
    }
}