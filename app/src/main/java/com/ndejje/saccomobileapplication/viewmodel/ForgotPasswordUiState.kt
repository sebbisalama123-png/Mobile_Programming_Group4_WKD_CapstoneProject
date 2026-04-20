package com.ndejje.saccomobileapplication.viewmodel

enum class ForgotPasswordStep {
    VERIFY_IDENTITY,
    SET_NEW_PASSWORD
}

data class ForgotPasswordUiState(
    val step: ForgotPasswordStep = ForgotPasswordStep.VERIFY_IDENTITY,
    val phoneNumber: String = "",
    val idNumber: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val verifiedUserId: Int? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
) {
    val passwordsMatch: Boolean
        get() = newPassword == confirmPassword

    val isVerifyFormValid: Boolean
        get() = phoneNumber.isNotBlank() && idNumber.isNotBlank()

    val isResetFormValid: Boolean
        get() = newPassword.length >= 6 && passwordsMatch
}