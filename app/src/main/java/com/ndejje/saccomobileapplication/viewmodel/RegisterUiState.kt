package com.ndejje.saccomobileapplication.viewmodel

data class RegisterUiState(
    // Input fields — owned by ViewModel so validateAndRegister() reads them from state
    val fullName:        String  = "",
    val phoneNumber:     String  = "",
    val idNumber:        String  = "",
    val password:        String  = "",
    val confirmPassword: String  = "",

    // UI state
    val isLoading:    Boolean = false,
    val errorMessage: String? = null,   // null = no error banner shown
    val isRegistered: Boolean = false
)