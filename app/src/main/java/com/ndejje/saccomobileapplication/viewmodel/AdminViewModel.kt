package com.ndejje.saccomobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.saccomobileapplication.model.LoanRequestEntity
import com.ndejje.saccomobileapplication.model.SaccoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel(
    private val repository: SaccoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AdminUiState>(AdminUiState.Loading)
    val uiState: StateFlow<AdminUiState> = _uiState

    fun loadRequests() {
        viewModelScope.launch {
            _uiState.value = AdminUiState.Loading
            try {
                val entities = repository.getPendingLoanRequests()
                _uiState.value = AdminUiState.Success(entities.map { it.toLoanRequest() })
            } catch (e: Exception) {
                _uiState.value = AdminUiState.Error("Failed to load requests")
            }
        }
    }

    // ── Public actions ────────────────────────────────────────────────────────

    fun approveLoan(id: String) = updateStatus(id, LoanStatus.APPROVED, "APPROVED")
    fun rejectLoan(id: String)  = updateStatus(id, LoanStatus.REJECTED,  "REJECTED")

    // ── Private helpers ───────────────────────────────────────────────────────

    /** Optimistic update: removes from list immediately, persists to DB in background. */
    private fun updateStatus(id: String, newStatus: LoanStatus, dbStatus: String) {
        val current = _uiState.value as? AdminUiState.Success ?: return
        val updated = current.requests.map { if (it.id == id) it.copy(status = newStatus) else it }
        _uiState.value = AdminUiState.Success(updated)

        viewModelScope.launch {
            try {
                repository.updateLoanStatus(id, dbStatus)
            } catch (_: Exception) {
                // If DB write fails, reload to restore correct state
                loadRequests()
            }
        }
    }

    private fun LoanRequestEntity.toLoanRequest() = LoanRequest(
        id          = requestId,
        memberName  = memberName,
        loanProduct = loanProduct,
        amount      = amount,
        status      = LoanStatus.PENDING
    )

    companion object {
        fun factory(repository: SaccoRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AdminViewModel(repository) as T
            }
        }
    }
}