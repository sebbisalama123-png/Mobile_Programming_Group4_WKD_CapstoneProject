package com.ndejje.saccomobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.saccomobileapplication.model.SaccoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: SaccoRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val authState: StateFlow<AuthUiState> = _authState

    fun login(phoneNumber: String, password: String) {
        if (phoneNumber.isBlank() || password.isBlank()) {
            _authState.value = AuthUiState.Error("All fields are required")
            return
        }
        viewModelScope.launch {
            _authState.value = AuthUiState.Loading
            val user = repository.loginUser(phoneNumber.trim(), password)
            _authState.value = if (user != null) {
                AuthUiState.Success(user.userId)
            } else {
                AuthUiState.Error("Invalid phone number or password")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthUiState.Idle
    }

    companion object {
        fun factory(repository: SaccoRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(repository) as T
            }
        }
    }
}