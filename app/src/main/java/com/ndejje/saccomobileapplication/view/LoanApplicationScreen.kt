@file:OptIn(ExperimentalMaterial3Api::class)

package com.ndejje.saccomobileapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ndejje.saccomobileapplication.R
import com.ndejje.saccomobileapplication.SaccoApplication
import com.ndejje.saccomobileapplication.ui.theme.MediumGrey
import com.ndejje.saccomobileapplication.ui.theme.SaccoBlue
import com.ndejje.saccomobileapplication.ui.theme.SaccoRed
import com.ndejje.saccomobileapplication.ui.theme.White
import com.ndejje.saccomobileapplication.viewmodel.LoanApplicationUiState
import com.ndejje.saccomobileapplication.viewmodel.LoanApplicationViewModel
import com.ndejje.saccomobileapplication.viewmodel.LoanProduct

// ── Root screen ──────────────────────────────────────────────────────────────

@Composable
fun LoanApplicationScreen(
    userId:         Int,
    memberName:     String,
    onNavigateBack: () -> Unit,
    onNavigateNext: () -> Unit
) {
    val context   = LocalContext.current
    val app       = context.applicationContext as SaccoApplication
    val viewModel = viewModel<LoanApplicationViewModel>(
        factory = LoanApplicationViewModel.factory(app.repository, userId)
    )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSubmitted) {
        if (uiState.isSubmitted) onNavigateNext()
    }

    Scaffold(
        topBar = { LoanTopBar(onNavigateBack = onNavigateBack) }
    ) { innerPadding ->
        LoanApplicationContent(
            uiState           = uiState,
            onProductSelect   = viewModel::selectLoanProduct,
            onAmountChange    = viewModel::setRequestedAmount,
            onTermsToggle     = { viewModel.setTermsAccepted(!uiState.termsAccepted) },
            onSubmit          = { viewModel.submitApplication(memberName) },
            onNavigateBack    = onNavigateBack,
            modifier          = Modifier.padding(innerPadding)
        )
    }
}

// ── Top app bar ───────────────────────────────────────────────────────────────

@Composable
private fun LoanTopBar(onNavigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector        = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    tint               = White
                )
            }
        },
        title = {
            Text(
                text       = stringResource(R.string.title_apply_loan),
                color      = White,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            Icon(
                imageVector        = Icons.Default.Description,
                contentDescription = stringResource(R.string.title_apply_loan),
                tint               = White,
                modifier           = Modifier.padding(end = dimensionResource(R.dimen.spacingMedium))
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = SaccoBlue
        )
    )
}

// ── Main content ──────────────────────────────────────────────────────────────

@Composable
private fun LoanApplicationContent(
    uiState:          LoanApplicationUiState,
    onProductSelect:  (LoanProduct) -> Unit,
    onAmountChange:   (String) -> Unit,
    onTermsToggle:    () -> Unit,
    onSubmit:         () -> Unit,
    onNavigateBack:   () -> Unit,
    modifier:         Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = dimensionResource(R.dimen.spacingMedium),
                vertical   = dimensionResource(R.dimen.spacingSmall) + 4.dp
            )
    ) {
        Text(
            text       = stringResource(R.string.loan_section_product),
            style      = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color      = SaccoRed
            ),
            modifier   = Modifier.padding(bottom = dimensionResource(R.dimen.spacingSmall) + 4.dp)
        )

        LoanProductDropdown(
            selected = uiState.selectedLoanProduct,
            onSelect = onProductSelect
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingMedium) + 4.dp))

        EligibilityInfoCard(uiState = uiState)

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingMedium)))

        if (uiState.isEligible) {
            LoanAmountInput(
                amount       = if (uiState.requestedAmount > 0) uiState.requestedAmount.toString() else "",
                errorMessage = uiState.amountError,
                onAmountChange = onAmountChange
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingMedium)))
        }

        TermsRow(
            accepted = uiState.termsAccepted,
            onToggle = onTermsToggle
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingLarge)))

        BottomNavButtons(
            isNextEnabled  = uiState.isEligible && uiState.termsAccepted && !uiState.isLoading
                    && uiState.requestedAmount > 0 && uiState.amountError == null,
            isLoading      = uiState.isLoading,
            onNavigateBack = onNavigateBack,
            onSubmit       = onSubmit
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingLarge)))
    }
}

// ── Loan product dropdown ─────────────────────────────────────────────────────

@Composable
private fun LoanProductDropdown(
    selected: LoanProduct,
    onSelect: (LoanProduct) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded         = expanded,
        onExpandedChange = { expanded = it },
        modifier         = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value         = selected.displayName,
            onValueChange = {},
            readOnly      = true,
            label         = { Text(stringResource(R.string.loan_product_label)) },
            trailingIcon  = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier      = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SaccoBlue,
                focusedLabelColor  = SaccoBlue,
                cursorColor        = SaccoBlue
            )
        )
        ExposedDropdownMenu(
            expanded         = expanded,
            onDismissRequest = { expanded = false }
        ) {
            LoanProduct.entries.forEach { product ->
                DropdownMenuItem(
                    text    = { Text(product.displayName) },
                    onClick = {
                        onSelect(product)
                        expanded = false
                    }
                )
            }
        }
    }
}

// ── Eligibility info card ─────────────────────────────────────────────────────

@Composable
private fun EligibilityInfoCard(uiState: LoanApplicationUiState) {
    ElevatedCard(
        modifier  = Modifier.fillMaxWidth(),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = dimensionResource(R.dimen.cardElevation)
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.cardCornerRadius))
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.cardPaddingHorizontal),
                vertical   = dimensionResource(R.dimen.spacingSmall) + 4.dp
            )
        ) {
            InfoRow(
                label = stringResource(R.string.loan_min_savings),
                value = stringResource(R.string.currency_amount, "%.2f".format(uiState.minimumSavingsRequired))
            )
            RowDivider()
            InfoRow(
                label = stringResource(R.string.loan_deposit_balance),
                value = stringResource(R.string.currency_amount, "%.2f".format(uiState.myDepositBalance))
            )
            RowDivider()
            InfoRow(
                label = stringResource(R.string.loan_available_deposits),
                value = stringResource(R.string.currency_amount, "%.2f".format(uiState.availableDeposits))
            )
            RowDivider()
            InfoRow(
                label      = stringResource(R.string.loan_eligibility_status),
                value      = if (uiState.isEligible)
                    stringResource(R.string.status_eligible)
                else
                    stringResource(R.string.status_not_eligible),
                valueColor = if (uiState.isEligible) SaccoBlue else SaccoRed
            )
            RowDivider()
            InfoRow(
                label = stringResource(R.string.loan_guarantors),
                value = uiState.guarantorsRequired.toString()
            )
            RowDivider()
            InfoRow(
                label = stringResource(R.string.loan_max_awardable),
                value = stringResource(R.string.currency_amount, "%.2f".format(uiState.maxLoanAwardable))
            )
        }
    }
}

@Composable
private fun InfoRow(
    label:      String,
    value:      String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.spacingSmall)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Text(
            text  = label,
            style = MaterialTheme.typography.bodyMedium.copy(color = MediumGrey)
        )
        Text(
            text  = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color      = valueColor
            )
        )
    }
}

@Composable
private fun RowDivider() {
    HorizontalDivider(
        color     = MaterialTheme.colorScheme.outlineVariant,
        thickness = 0.5.dp
    )
}

// ── Terms & conditions ────────────────────────────────────────────────────────

@Composable
private fun TermsRow(accepted: Boolean, onToggle: () -> Unit) {
    Row(
        modifier          = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = dimensionResource(R.dimen.spacingTiny)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked         = accepted,
            onCheckedChange = null,
            colors          = CheckboxDefaults.colors(
                checkedColor   = SaccoBlue,
                checkmarkColor = White
            )
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacingSmall)))
        Text(
            text  = stringResource(R.string.loan_terms_label),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

// ── Loan amount input ─────────────────────────────────────────────────────────

@Composable
private fun LoanAmountInput(
    amount:         String,
    errorMessage:   String?,
    onAmountChange: (String) -> Unit
) {
    OutlinedTextField(
        value         = amount,
        onValueChange = onAmountChange,
        label         = { Text("Loan Amount (UGX)") },
        isError       = errorMessage != null,
        supportingText = errorMessage?.let { { Text(it, color = SaccoRed) } },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine    = true,
        modifier      = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SaccoBlue,
            focusedLabelColor  = SaccoBlue,
            cursorColor        = SaccoBlue
        )
    )
}

// ── Bottom navigation buttons ─────────────────────────────────────────────────

@Composable
private fun BottomNavButtons(
    isNextEnabled:  Boolean,
    isLoading:      Boolean,
    onNavigateBack: () -> Unit,
    onSubmit:       () -> Unit
) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(R.dimen.spacingSmall)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick        = onNavigateBack,
            modifier       = Modifier.size(dimensionResource(R.dimen.circularButtonSize)),
            shape          = CircleShape,
            colors         = ButtonDefaults.buttonColors(containerColor = SaccoRed),
            contentPadding = PaddingValues(0.dp)
        ) {
            Icon(
                imageVector        = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint               = White,
                modifier           = Modifier.size(dimensionResource(R.dimen.circularNavIconSize))
            )
        }

        Button(
            onClick        = onSubmit,
            enabled        = isNextEnabled,
            modifier       = Modifier.size(dimensionResource(R.dimen.circularButtonSize)),
            shape          = CircleShape,
            colors         = ButtonDefaults.buttonColors(
                containerColor         = SaccoBlue,
                disabledContainerColor = MediumGrey
            ),
            contentPadding = PaddingValues(0.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier    = Modifier.size(dimensionResource(R.dimen.progressIndicatorSizeSmall)),
                    color       = White,
                    strokeWidth = 2.dp
                )
            } else {
                Icon(
                    imageVector        = Icons.Default.ArrowForward,
                    contentDescription = stringResource(R.string.cd_submit),
                    tint               = White,
                    modifier           = Modifier.size(dimensionResource(R.dimen.circularNavIconSize))
                )
            }
        }
    }
}
