@file:OptIn(ExperimentalMaterial3Api::class)

package com.ndejje.saccomobileapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ndejje.saccomobileapplication.R
import com.ndejje.saccomobileapplication.SaccoApplication
import com.ndejje.saccomobileapplication.ui.theme.CreditGreen
import com.ndejje.saccomobileapplication.ui.theme.MediumGrey
import com.ndejje.saccomobileapplication.ui.theme.SaccoBlue
import com.ndejje.saccomobileapplication.ui.theme.SaccoRed
import com.ndejje.saccomobileapplication.ui.theme.White
import com.ndejje.saccomobileapplication.viewmodel.LedgerUiState
import com.ndejje.saccomobileapplication.viewmodel.LedgerViewModel
import com.ndejje.saccomobileapplication.viewmodel.Transaction
import com.ndejje.saccomobileapplication.viewmodel.TransactionType

// ── Root screen ───────────────────────────────────────────────────────────────

@Composable
fun TransactionLedgerScreen(
    userId:         Int,
    onNavigateBack: () -> Unit
) {
    val app       = LocalContext.current.applicationContext as SaccoApplication
    val viewModel = viewModel<LedgerViewModel>(factory = LedgerViewModel.factory(app.repository))
    val uiState   by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadTransactions(userId)
    }

    Scaffold(
        topBar = { LedgerTopBar(onNavigateBack = onNavigateBack) }
    ) { innerPadding ->
        when (val state = uiState) {

            is LedgerUiState.Loading -> {
                Box(
                    modifier         = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SaccoBlue)
                }
            }

            is LedgerUiState.Success -> {
                LedgerContent(
                    transactions = state.transactions,
                    modifier     = Modifier.padding(innerPadding)
                )
            }

            is LedgerUiState.Error -> {
                Box(
                    modifier         = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = SaccoRed)
                }
            }
        }
    }
}

// ── Top app bar ───────────────────────────────────────────────────────────────

@Composable
private fun LedgerTopBar(onNavigateBack: () -> Unit) {
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
                text       = stringResource(R.string.title_mini_statement),
                color      = White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = SaccoBlue
        )
    )
}

// ── Ledger content ────────────────────────────────────────────────────────────

@Composable
private fun LedgerContent(
    transactions: List<Transaction>,
    modifier:     Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LedgerSummaryHeader(count = transactions.size)

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items = transactions, key = { it.id }) { transaction ->
                TransactionItem(transaction = transaction)
                HorizontalDivider(
                    color     = MaterialTheme.colorScheme.outlineVariant,
                    thickness = dimensionResource(R.dimen.hairlineStroke)
                )
            }
        }
    }
}

// ── Summary header ────────────────────────────────────────────────────────────

@Composable
private fun LedgerSummaryHeader(count: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.cardPaddingHorizontal),
                vertical   = dimensionResource(R.dimen.cardPaddingVertical)
            )
    ) {
        Text(
            text  = stringResource(R.string.ledger_title),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onBackground
            )
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingXXSmall)))
        Text(
            text  = stringResource(R.string.ledger_records_count, count),
            style = MaterialTheme.typography.bodySmall.copy(color = MediumGrey)
        )
    }
    HorizontalDivider(
        color     = MaterialTheme.colorScheme.outlineVariant,
        thickness = dimensionResource(R.dimen.hairlineStroke)
    )
}

// ── Transaction item ──────────────────────────────────────────────────────────

@Composable
private fun TransactionItem(transaction: Transaction) {
    ListItem(
        leadingContent = {
            TypeIcon(type = transaction.type)
        },
        headlineContent = {
            Text(
                text  = transaction.description,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        supportingContent = {
            Text(
                text  = transaction.date,
                style = MaterialTheme.typography.bodySmall.copy(color = MediumGrey)
            )
        },
        trailingContent = {
            AmountText(amount = transaction.amount, type = transaction.type)
        }
    )
}

// ── Type icon ─────────────────────────────────────────────────────────────────

@Composable
private fun TypeIcon(type: TransactionType) {
    val backgroundColor = if (type == TransactionType.CREDIT) CreditGreen else SaccoRed
    val icon            = if (type == TransactionType.CREDIT)
        Icons.Default.ArrowDownward else Icons.Default.ArrowUpward

    Box(
        modifier         = Modifier
            .size(dimensionResource(R.dimen.transactionIconSize))
            .background(color = backgroundColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = if (type == TransactionType.CREDIT)
                stringResource(R.string.transaction_credit)
            else
                stringResource(R.string.transaction_debit),
            tint               = White,
            modifier           = Modifier.size(dimensionResource(R.dimen.transactionIconInner))
        )
    }
}

// ── Amount text ───────────────────────────────────────────────────────────────

@Composable
private fun AmountText(amount: Double, type: TransactionType) {
    val text  = if (type == TransactionType.CREDIT)
        stringResource(R.string.credit_amount, "%.2f".format(amount))
    else
        stringResource(R.string.debit_amount,  "%.2f".format(amount))
    val color = if (type == TransactionType.CREDIT) CreditGreen else SaccoRed

    Text(
        text  = text,
        style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.Bold,
            color      = color
        )
    )
}