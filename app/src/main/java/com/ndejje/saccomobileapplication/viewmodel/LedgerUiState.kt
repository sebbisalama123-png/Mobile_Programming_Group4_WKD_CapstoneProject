package com.ndejje.saccomobileapplication.viewmodel

enum class TransactionType { CREDIT, DEBIT }

data class Transaction(
    val id:          String,
    val date:        String,           // "DD MMM YYYY" — swap to Long when Room is wired
    val description: String,
    val amount:      Double,
    val type:        TransactionType
)

sealed class LedgerUiState {
    object Loading                                         : LedgerUiState()
    data class Success(val transactions: List<Transaction>): LedgerUiState()
    data class Error(val message: String)                  : LedgerUiState()
}