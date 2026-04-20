package com.ndejje.saccomobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.saccomobileapplication.model.SaccoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: SaccoRepository,
    private val userId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            try {
                val user = repository.getUserById(userId)
                val account = repository.getMemberAccount(userId)
                if (user == null || account == null) {
                    _uiState.value = ProfileUiState.Error("Could not load profile")
                    return@launch
                }

                _uiState.value = ProfileUiState.Success(
                    ProfileData(
                        fullName = user.fullName,
                        phoneNumber = user.phoneNumber,
                        idNumber = user.idNumber,
                        accountNumber = account.accountNumber,
                        savingsBalance = account.savingsBalance,
                        shareCapital = account.shareCapital
                    )
                )
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error("Failed to load profile")
            }
        }
    }

    companion object {
        fun factory(repository: SaccoRepository, userId: Int) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ProfileViewModel(repository, userId) as T
            }
        }
    }
}