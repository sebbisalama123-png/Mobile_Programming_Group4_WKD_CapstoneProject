package com.ndejje.saccomobileapplication.viewmodel

enum class LoanStatus { PENDING, APPROVED, REJECTED }

data class LoanRequest(
    val id:          String,
    val memberName:  String,
    val loanProduct: String,   // plain string — admin views any product type by name
    val amount:      Double,
    val status:      LoanStatus
)

sealed class AdminUiState {
    object Loading                                       : AdminUiState()
    data class Success(val requests: List<LoanRequest>)  : AdminUiState()
    data class Error(val message: String)                : AdminUiState()
}