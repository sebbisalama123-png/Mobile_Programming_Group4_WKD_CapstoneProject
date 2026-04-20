package com.ndejje.saccomobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.saccomobileapplication.model.SaccoRepository
import com.ndejje.saccomobileapplication.model.TransactionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class LedgerViewModel(
    private val repository: SaccoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LedgerUiState>(LedgerUiState.Loading)
    val uiState: StateFlow<LedgerUiState> = _uiState

    fun loadTransactions(userId: Int) {
        viewModelScope.launch {
            _uiState.value = LedgerUiState.Loading
            try {
                val entities = repository.getTransactions(userId)
                _uiState.value = LedgerUiState.Success(entities.map { it.toTransaction() })
            } catch (e: Exception) {
                _uiState.value = LedgerUiState.Error("Failed to load transactions")
            }
        }
    }

    private fun TransactionEntity.toTransaction(): Transaction {
        val formatted = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(date))
        return Transaction(
            id          = transactionId,
            date        = formatted,
            description = description,
            amount      = amount,
            type        = if (type == "CREDIT") TransactionType.CREDIT else TransactionType.DEBIT
        )
    }

    companion object {
        fun factory(repository: SaccoRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return LedgerViewModel(repository) as T
            }
        }
    }
}