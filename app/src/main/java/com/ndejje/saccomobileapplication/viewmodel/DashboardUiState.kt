package com.ndejje.saccomobileapplication.viewmodel

data class DashboardData(
    val memberName:          String,
    val maskedAccountNumber: String,
    val savingsBalance:      Double,
    val shareCapital:        Double,
    val activeLoanCount:     Int
)

sealed class DashboardUiState {
    object Loading                              : DashboardUiState()
    data class Success(val data: DashboardData) : DashboardUiState()
    data class Error(val message: String)       : DashboardUiState()
}