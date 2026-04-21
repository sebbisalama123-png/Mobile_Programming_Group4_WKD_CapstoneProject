package com.ndejje.saccomobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.saccomobileapplication.model.SaccoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TopUpSavingsViewModel(
    private val repository: SaccoRepository,
    private val userId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(TopUpSavingsUiState())
    val uiState: StateFlow<TopUpSavingsUiState> = _uiState

    init {
        loadBalance()
    }

    private fun loadBalance() {
        viewModelScope.launch {
            val account = repository.getMemberAccount(userId)
            if (account != null) {
                _uiState.update { it.copy(currentBalance = account.savingsBalance) }
            }
        }
    }

    fun setAmount(value: String) {
        _uiState.update { it.copy(amountInput = value, errorMessage = null) }
    }

    fun submit() {
        val state = _uiState.value
        if (!state.isAmountValid) {
            _uiState.update { it.copy(errorMessage = "Enter a valid amount greater than zero") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.topUpSavings(userId, state.parsedAmount)
                _uiState.update {
                    it.copy(
                        currentBalance = it.currentBalance + state.parsedAmount,
                        amountInput = "",
                        isLoading = false,
                        isSuccess = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Deposit failed. Please try again.") }
            }
        }
    }

    companion object {
        fun factory(repository: SaccoRepository, userId: Int) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TopUpSavingsViewModel(repository, userId) as T
            }
        }
    }
}