package com.ndejje.saccomobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.saccomobileapplication.model.SaccoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: SaccoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState: StateFlow<ForgotPasswordUiState> = _uiState

    fun setPhone(value: String) = _uiState.update { it.copy(phoneNumber = value, errorMessage = null) }

    fun setIdNumber(value: String) = _uiState.update { it.copy(idNumber = value, errorMessage = null) }

    fun setNewPassword(value: String) = _uiState.update { it.copy(newPassword = value, errorMessage = null) }

    fun setConfirmPassword(value: String) = _uiState.update { it.copy(confirmPassword = value, errorMessage = null) }

    fun verifyIdentity() {
        val state = _uiState.value
        if (!state.isVerifyFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val userId = repository.verifyIdentity(state.phoneNumber, state.idNumber)
            if (userId != null) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        verifiedUserId = userId,
                        step = ForgotPasswordStep.SET_NEW_PASSWORD
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "No account found with those details") }
            }
        }
    }

    fun resetPassword() {
        val state = _uiState.value
        val userId = state.verifiedUserId ?: return
        if (!state.isResetFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            repository.resetPassword(userId, state.newPassword)
            _uiState.update { it.copy(isLoading = false, isSuccess = true) }
        }
    }

    companion object {
        fun factory(repository: SaccoRepository) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ForgotPasswordViewModel(repository) as T
            }
        }
    }
}