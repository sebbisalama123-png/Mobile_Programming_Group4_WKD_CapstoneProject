package com.ndejje.saccomobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.saccomobileapplication.model.SaccoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val repository: SaccoRepository,
    private val userId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState

    fun setCurrentPassword(value: String) = _uiState.update { it.copy(currentPassword = value, errorMessage = null) }

    fun setNewPassword(value: String) = _uiState.update { it.copy(newPassword = value, errorMessage = null) }

    fun setConfirmPassword(value: String) = _uiState.update { it.copy(confirmPassword = value, errorMessage = null) }

    fun submit() {
        val state = _uiState.value
        if (!state.isFormValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val changed = repository.changePassword(userId, state.currentPassword, state.newPassword)
            if (changed) {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Current password is incorrect") }
            }
        }
    }

    companion object {
        fun factory(repository: SaccoRepository, userId: Int) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ChangePasswordViewModel(repository, userId) as T
            }
        }
    }
}