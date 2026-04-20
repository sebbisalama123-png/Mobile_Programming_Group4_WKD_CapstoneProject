package com.ndejje.saccomobileapplication.viewmodel

enum class LoanProduct(val displayName: String) {
    SUPER_LOAN       ("SUPER LOAN"),
    EMERGENCY_LOAN   ("EMERGENCY LOAN"),
    DEVELOPMENT_LOAN ("DEVELOPMENT LOAN")
}

data class LoanApplicationUiState(
    val selectedLoanProduct:    LoanProduct,
    val minimumSavingsRequired: Double,
    val myDepositBalance:       Double,
    val availableDeposits:      Double,
    val isEligible:             Boolean,
    val guarantorsRequired:     Int,
    val maxLoanAwardable:       Double,
    val requestedAmount:        Double  = 0.0,
    val amountError:            String? = null,
    val termsAccepted:          Boolean,
    val isLoading:              Boolean = false,
    val isSubmitted:            Boolean = false,
    val errorMessage:           String? = null
)