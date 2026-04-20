package com.ndejje.saccomobileapplication.viewmodel

sealed class AuthUiState {
    object Idle                                 : AuthUiState()
    object Loading                              : AuthUiState()
    data class Success(val userId: Int)         : AuthUiState()
    data class Error(val message: String)       : AuthUiState()
}