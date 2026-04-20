package com.ndejje.saccomobileapplication.viewmodel

data class TopUpSavingsUiState(
    val currentBalance: Double = 0.0,
    val amountInput: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
) {
    val parsedAmount: Double
        get() = amountInput.toDoubleOrNull() ?: 0.0

    val isAmountValid: Boolean
        get() = parsedAmount > 0.0
}