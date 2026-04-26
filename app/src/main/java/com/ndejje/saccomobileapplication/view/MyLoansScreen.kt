@file:OptIn(ExperimentalMaterial3Api::class)

package com.ndejje.saccomobileapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
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
import com.ndejje.saccomobileapplication.viewmodel.LoanItem
import com.ndejje.saccomobileapplication.viewmodel.MyLoansUiState
import com.ndejje.saccomobileapplication.viewmodel.MyLoansViewModel

// ── Root screen ───────────────────────────────────────────────────────────────

@Composable
fun MyLoansScreen(
    userId:         Int,
    onNavigateBack: () -> Unit
) {
    val context   = LocalContext.current
    val app       = context.applicationContext as SaccoApplication
    val viewModel = viewModel<MyLoansViewModel>(factory = MyLoansViewModel.factory(app.repository))
    val uiState   by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadLoans(userId)
    }

    Scaffold(
        topBar = { MyLoansTopBar(onNavigateBack = onNavigateBack) }
    ) { innerPadding ->
        when (val state = uiState) {
            is MyLoansUiState.Loading -> {
                Box(
                    modifier         = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SaccoBlue)
                }
            }
            is MyLoansUiState.Success -> {
                MyLoansContent(
                    loans    = state.loans,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is MyLoansUiState.Error -> {
                Box(
                    modifier         = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = SaccoRed)
                }
            }
        }
    }
}

// ── Top bar ───────────────────────────────────────────────────────────────────

@Composable
private fun MyLoansTopBar(onNavigateBack: () -> Unit) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector        = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint               = White
                )
            }
        },
        title = {
            Text(
                text       = "My Loans",
                color      = White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = SaccoBlue
        )
    )
}

// ── Content with tabs ─────────────────────────────────────────────────────────

private enum class LoanTab(val label: String, val filter: String?) {
    ALL("All", null),
    PENDING("Pending", "PENDING"),
    APPROVED("Approved", "APPROVED"),
    REJECTED("Rejected", "REJECTED")
}

@Composable
private fun MyLoansContent(
    loans:    List<LoanItem>,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(LoanTab.ALL) }

    val displayed = if (selectedTab.filter == null) loans
    else loans.filter { it.status == selectedTab.filter }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab row
        ScrollableTabRow(
            selectedTabIndex = LoanTab.entries.indexOf(selectedTab),
            containerColor   = MaterialTheme.colorScheme.surface,
            contentColor     = SaccoBlue,
            edgePadding      = dimensionResource(R.dimen.spacingNone)
        ) {
            LoanTab.entries.forEach { tab ->
                val count = if (tab.filter == null) loans.size
                else loans.count { it.status == tab.filter }
                Tab(
                    selected = selectedTab == tab,
                    onClick  = { selectedTab = tab },
                    text     = {
                        Text(
                            text       = "${tab.label} ($count)",
                            fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness =dimensionResource(R.dimen.hairlineStroke) )

        if (displayed.isEmpty()) {
            Box(
                modifier         = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text  = "No loans found",
                    style = MaterialTheme.typography.bodyMedium.copy(color = MediumGrey)
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(items = displayed, key = { it.requestId }) { loan ->
                    LoanListItem(loan = loan)
                    HorizontalDivider(
                        color     = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

// ── Loan list item ────────────────────────────────────────────────────────────

@Composable
private fun LoanListItem(loan: LoanItem) {
    ListItem(
        leadingContent = {
            Box(
                modifier         = Modifier
                    .size(dimensionResource(R.dimen.transactionIconSize))
                    .background(color = SaccoBlue, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.Assignment,
                    contentDescription = null,
                    tint               = White,
                    modifier           = Modifier.size(dimensionResource(R.dimen.transactionIconInner))
                )
            }
        },
        headlineContent = {
            Text(
                text  = loan.loanProduct,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
        },
        supportingContent = {
            Text(
                text  = loan.date,
                style = MaterialTheme.typography.bodySmall.copy(color = MediumGrey)
            )
        },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text  = "UGX ${"%.2f".format(loan.amount)}",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color      = SaccoBlue
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                StatusBadge(status = loan.status)
            }
        }
    )
}

// ── Status badge ──────────────────────────────────────────────────────────────

@Composable
private fun StatusBadge(status: String) {
    val (bgColor, textColor) = when (status) {
        "APPROVED" -> CreditGreen to White
        "REJECTED" -> SaccoRed   to White
        else       -> Color(0xFFFFA000) to White   // amber for PENDING
    }
    Surface(
        shape = RoundedCornerShape(50),
        color = bgColor
    ) {
        Text(
            text     = status,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style    = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color      = textColor
            )
        )
    }
}
