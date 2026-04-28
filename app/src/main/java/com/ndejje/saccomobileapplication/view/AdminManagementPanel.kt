@file:OptIn(ExperimentalMaterial3Api::class)

package com.ndejje.saccomobileapplication.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
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
import com.ndejje.saccomobileapplication.viewmodel.AdminUiState
import com.ndejje.saccomobileapplication.viewmodel.AdminViewModel
import com.ndejje.saccomobileapplication.viewmodel.LoanRequest
import com.ndejje.saccomobileapplication.viewmodel.LoanStatus

// ── Root screen ───────────────────────────────────────────────────────────────

@Composable
fun AdminScreen(
    onNavigateBack: () -> Unit
) {
    val context   = LocalContext.current
    val app       = context.applicationContext as SaccoApplication
    val viewModel = viewModel<AdminViewModel>(factory = AdminViewModel.factory(app.repository))
    val uiState   by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRequests()
    }

    Scaffold(
        topBar = { AdminTopBar(onNavigateBack = onNavigateBack) }
    ) { innerPadding ->
        when (val state = uiState) {

            is AdminUiState.Loading -> {
                Box(
                    modifier         = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SaccoBlue)
                }
            }

            is AdminUiState.Success -> {
                AdminContent(
                    requests  = state.requests,
                    onApprove = viewModel::approveLoan,
                    onReject  = viewModel::rejectLoan,
                    modifier  = Modifier.padding(innerPadding)
                )
            }

            is AdminUiState.Error -> {
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
private fun AdminTopBar(onNavigateBack: () -> Unit) {
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
                text       = stringResource(R.string.title_admin_dashboard),
                color      = White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = SaccoBlue
        )
    )
}

// ── Main content ──────────────────────────────────────────────────────────────

@Composable
private fun AdminContent(
    requests:  List<LoanRequest>,
    onApprove: (String) -> Unit,
    onReject:  (String) -> Unit,
    modifier:  Modifier = Modifier
) {
    val pending = requests.filter { it.status == LoanStatus.PENDING }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (pending.isEmpty()) {
            AllCaughtUpState()
        } else {
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = dimensionResource(R.dimen.spacingSmall))
            ) {
                items(items = pending, key = { it.id }) { request ->
                    LoanRequestCard(
                        request   = request,
                        onApprove = { onApprove(request.id) },
                        onReject  = { onReject(request.id) }
                    )
                }
            }
        }
    }
}

// ── Empty state ───────────────────────────────────────────────────────────────

@Composable
private fun AllCaughtUpState() {
    Box(
        modifier         = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacingSmall))
        ) {
            Icon(
                imageVector        = Icons.Default.CheckCircle,
                contentDescription = null,
                tint               = CreditGreen,
                modifier           = Modifier.size(dimensionResource(R.dimen.emptyStateIconSize))
            )
            Text(
                text  = stringResource(R.string.admin_all_caught_up),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text  = stringResource(R.string.admin_no_pending),
                style = MaterialTheme.typography.bodyMedium.copy(color = MediumGrey)
            )
        }
    }
}

// ── Loan request card ─────────────────────────────────────────────────────────

@Composable
private fun LoanRequestCard(
    request:   LoanRequest,
    onApprove: () -> Unit,
    onReject:  () -> Unit
) {
    ElevatedCard(
        modifier  = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.cardPaddingHorizontal),
                vertical   = dimensionResource(R.dimen.spacingXSmall)
            ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = dimensionResource(R.dimen.cardElevation)
        ),
        shape = RoundedCornerShape(dimensionResource(R.dimen.cardCornerRadius))
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.cardPaddingHorizontal))
        ) {

            // ── Header row ────────────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text  = request.memberName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text  = request.loanProduct,
                        style = MaterialTheme.typography.bodySmall.copy(color = MediumGrey)
                    )
                }
                Surface(
                    color = SaccoRed.copy(alpha = 0.10f),
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text     = stringResource(R.string.status_pending),
                        style    = MaterialTheme.typography.labelSmall.copy(
                            color      = SaccoRed,
                            fontWeight = FontWeight.SemiBold
                        ),
                        modifier = Modifier.padding(
                            horizontal = dimensionResource(R.dimen.spacingSmall) + dimensionResource(R.dimen.spacingXXSmall),
                            vertical   = dimensionResource(R.dimen.spacingTiny)
                        )
                    )
                }
            }

            HorizontalDivider(
                modifier  = Modifier.padding(vertical = dimensionResource(R.dimen.spacingSmall) + dimensionResource(R.dimen.spacingXXSmall)),
                color     = MaterialTheme.colorScheme.outlineVariant,
                thickness = dimensionResource(R.dimen.hairlineStroke)
            )

            // ── Amount ────────────────────────────────────────────────────────
            Text(
                text  = stringResource(R.string.currency_amount, "%.2f".format(request.amount)),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color      = SaccoBlue
                )
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingSmall) + dimensionResource(R.dimen.spacingTiny)))

            // ── Action row ────────────────────────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onReject,
                    border  = BorderStroke(dimensionResource(R.dimen.thinStroke), SaccoRed)
                ) {
                    Text(text = stringResource(R.string.btn_reject), color = SaccoRed)
                }
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacingSmall)))
                Button(
                    onClick = onApprove,
                    colors  = ButtonDefaults.buttonColors(containerColor = CreditGreen)
                ) {
                    Text(text = stringResource(R.string.btn_approve), color = White)
                }
            }
        }
    }
}
