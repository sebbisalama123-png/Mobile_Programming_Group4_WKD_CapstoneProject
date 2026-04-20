package com.ndejje.saccomobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.saccomobileapplication.model.SaccoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: SaccoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState

    fun loadDashboard(userId: Int) {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            try {
                val user    = repository.getUserById(userId)
                val account = repository.getMemberAccount(userId)
                val loans   = repository.getApprovedLoanCount(userId)

                if (user == null || account == null) {
                    _uiState.value = DashboardUiState.Error("Could not load account data")
                    return@launch
                }

                _uiState.value = DashboardUiState.Success(
                    DashboardData(
                        memberName          = user.fullName,
                        maskedAccountNumber = maskAccount(account.accountNumber),
                        savingsBalance      = account.savingsBalance,
                        shareCapital        = account.shareCapital,
                        activeLoanCount     = loans
                    )
                )
            } catch (e: Exception) {
                _uiState.value = DashboardUiState.Error("Failed to load dashboard")
            }
        }
    }

    private fun maskAccount(accountNumber: String): String {
        // "MUSC-000001" → "MUSC-XXXXXX"
        val parts = accountNumber.split("-")
        return if (parts.size == 2) "MUSC-XXXXXX" else accountNumber
    }

    companion object {
        fun factory(repository: SaccoRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return DashboardViewModel(repository) as T
            }
        }
    }
}