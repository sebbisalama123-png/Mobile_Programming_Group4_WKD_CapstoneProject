package com.ndejje.saccomobileapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.saccomobileapplication.model.SaccoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoanApplicationViewModel(
    private val repository: SaccoRepository,
    private val userId:     Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(buildInitialState())
    val uiState: StateFlow<LoanApplicationUiState> = _uiState

    // ── Initialisation — load real savings balance ────────────────────────────

    init {
        loadBalance()
    }

    private fun loadBalance() {
        viewModelScope.launch {
            val account = repository.getMemberAccount(userId)
            if (account != null) {
                _uiState.update { it.copy(myDepositBalance = account.savingsBalance) }
                recalculate()
            }
        }
    }

    // ── Public API ────────────────────────────────────────────────────────────

    fun selectLoanProduct(product: LoanProduct) {
        _uiState.update { it.copy(selectedLoanProduct = product, termsAccepted = false) }
        recalculate()
    }

    fun setRequestedAmount(input: String) {
        val amount = input.toDoubleOrNull() ?: 0.0
        val max    = _uiState.value.maxLoanAwardable
        val error  = when {
            amount <= 0          -> "Enter a valid amount"
            amount > max         -> "Amount exceeds maximum of UGX ${"%.2f".format(max)}"
            else                 -> null
        }
        _uiState.update { it.copy(requestedAmount = amount, amountError = error) }
    }

    fun setTermsAccepted(accepted: Boolean) {
        _uiState.update { it.copy(termsAccepted = accepted) }
    }

    fun submitApplication(memberName: String) {
        val state = _uiState.value
        if (!state.isEligible || !state.termsAccepted || state.requestedAmount <= 0 || state.amountError != null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                repository.saveLoanRequest(
                    userId      = userId,
                    memberName  = memberName,
                    loanProduct = state.selectedLoanProduct.displayName,
                    amount      = state.requestedAmount
                )
                _uiState.update { it.copy(isLoading = false, isSubmitted = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Failed to submit application") }
            }
        }
    }

    // ── Private recalculation ─────────────────────────────────────────────────

    private fun recalculate() {
        _uiState.update { state ->
            val config    = loanConfig(state.selectedLoanProduct)
            val eligible  = state.myDepositBalance >= config.minSavings
            val available = (state.myDepositBalance - config.minSavings).coerceAtLeast(0.0)
            state.copy(
                minimumSavingsRequired = config.minSavings,
                guarantorsRequired     = config.guarantors,
                availableDeposits      = available,
                isEligible             = eligible,
                maxLoanAwardable       = if (eligible) state.myDepositBalance * 3 else 0.0
            )
        }
    }

    private data class LoanConfig(val minSavings: Double, val guarantors: Int)

    private fun loanConfig(product: LoanProduct): LoanConfig = when (product) {
        LoanProduct.SUPER_LOAN       -> LoanConfig(minSavings = 1_000.0, guarantors = 2)
        LoanProduct.EMERGENCY_LOAN   -> LoanConfig(minSavings =   500.0, guarantors = 1)
        LoanProduct.DEVELOPMENT_LOAN -> LoanConfig(minSavings = 2_000.0, guarantors = 3)
    }

    private fun buildInitialState(): LoanApplicationUiState {
        val initialProduct = LoanProduct.SUPER_LOAN
        val config         = loanConfig(initialProduct)
        return LoanApplicationUiState(
            selectedLoanProduct    = initialProduct,
            minimumSavingsRequired = config.minSavings,
            myDepositBalance       = 0.0,
            availableDeposits      = 0.0,
            isEligible             = false,
            guarantorsRequired     = config.guarantors,
            maxLoanAwardable       = 0.0,
            termsAccepted          = false
        )
    }

    companion object {
        fun factory(repository: SaccoRepository, userId: Int) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return LoanApplicationViewModel(repository, userId) as T
            }
        }
    }
}