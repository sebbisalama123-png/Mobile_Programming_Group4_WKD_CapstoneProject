package com.ndejje.saccomobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.saccomobileapplication.model.SaccoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: SaccoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    // ── Field updates ─────────────────────────────────────────────────────────

    fun onFullNameChange(value: String)        { _uiState.update { it.copy(fullName        = value) } }
    fun onPhoneNumberChange(value: String)     { _uiState.update { it.copy(phoneNumber     = value) } }
    fun onIdNumberChange(value: String)        { _uiState.update { it.copy(idNumber        = value) } }
    fun onPasswordChange(value: String)        { _uiState.update { it.copy(password        = value) } }
    fun onConfirmPasswordChange(value: String) { _uiState.update { it.copy(confirmPassword = value) } }

    // ── Validation & submission ───────────────────────────────────────────────

    fun validateAndRegister() {
        val state = _uiState.value

        if (state.fullName.isBlank()    || state.phoneNumber.isBlank() ||
            state.idNumber.isBlank()    || state.password.isBlank()    ||
            state.confirmPassword.isBlank()
        ) {
            _uiState.update { it.copy(errorMessage = "All fields are required") }
            return
        }

        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Passwords do not match") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.registerUser(
                fullName    = state.fullName,
                phoneNumber = state.phoneNumber,
                idNumber    = state.idNumber,
                password    = state.password
            )

            _uiState.update {
                when {
                    result > 0  -> it.copy(isLoading = false, isRegistered = true)
                    result == -1 -> it.copy(isLoading = false, errorMessage = "Phone number already registered")
                    else         -> it.copy(isLoading = false, errorMessage = "Registration failed. Please try again.")
                }
            }
        }
    }

    companion object {
        fun factory(repository: SaccoRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return RegisterViewModel(repository) as T
            }
        }
    }
}