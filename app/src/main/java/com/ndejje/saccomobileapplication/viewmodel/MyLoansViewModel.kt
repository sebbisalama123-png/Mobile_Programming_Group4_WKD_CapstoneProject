package com.ndejje.saccomobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.saccomobileapplication.model.SaccoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MyLoansViewModel(
    private val repository: SaccoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MyLoansUiState>(MyLoansUiState.Loading)
    val uiState: StateFlow<MyLoansUiState> = _uiState

    fun loadLoans(userId: Int) {
        viewModelScope.launch {
            _uiState.value = MyLoansUiState.Loading
            try {
                val loans = repository.getLoansByUserId(userId).map { entity ->
                    LoanItem(
                        requestId   = entity.requestId,
                        loanProduct = entity.loanProduct,
                        amount      = entity.amount,
                        status      = entity.status,
                        date        = entity.createdAt.toDisplayDate()
                    )
                }
                _uiState.value = MyLoansUiState.Success(loans)
            } catch (e: Exception) {
                _uiState.value = MyLoansUiState.Error("Failed to load loans")
            }
        }
    }

    companion object {
        fun factory(repository: SaccoRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MyLoansViewModel(repository) as T
            }
        }
    }
}