package com.ndejje.saccomobileapplication.viewmodel

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
) {
    val newPasswordsMatch: Boolean
        get() = newPassword == confirmPassword

    val isFormValid: Boolean
        get() = currentPassword.isNotBlank() && newPassword.length >= 6 && newPasswordsMatch
}