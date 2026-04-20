package com.ndejje.saccomobileapplication.viewmodel

data class ProfileData(
    val fullName: String,
    val phoneNumber: String,
    val idNumber: String,
    val accountNumber: String,
    val savingsBalance: Double,
    val shareCapital: Double
)

sealed class ProfileUiState {
    object Loading : ProfileUiState()
    data class Success(val data: ProfileData) : ProfileUiState()
    data class Error(val message: String) : ProfileUiState()
}