package com.ndejje.saccomobileapplication.viewmodel

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class LoanItem(
    val requestId:   String,
    val loanProduct: String,
    val amount:      Double,
    val status:      String,   // "PENDING" | "APPROVED" | "REJECTED"
    val date:        String
)

sealed interface MyLoansUiState {
    data object Loading : MyLoansUiState
    data class  Success(val loans: List<LoanItem>) : MyLoansUiState
    data class  Error(val message: String) : MyLoansUiState
}

private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

fun Long.toDisplayDate(): String = dateFormat.format(Date(this))