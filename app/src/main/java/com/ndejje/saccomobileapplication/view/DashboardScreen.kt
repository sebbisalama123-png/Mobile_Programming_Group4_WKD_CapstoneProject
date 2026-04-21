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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ndejje.saccomobileapplication.R
import com.ndejje.saccomobileapplication.SaccoApplication
import com.ndejje.saccomobileapplication.ui.theme.MediumGrey
import com.ndejje.saccomobileapplication.ui.theme.SaccoBlue
import com.ndejje.saccomobileapplication.ui.theme.SaccoRed
import com.ndejje.saccomobileapplication.ui.theme.White
import com.ndejje.saccomobileapplication.viewmodel.DashboardData
import com.ndejje.saccomobileapplication.viewmodel.DashboardUiState
import com.ndejje.saccomobileapplication.viewmodel.DashboardViewModel

// ── Root screen ─────────────────────────────────────────────────────────────

@Composable
fun DashboardScreen(
    userId: Int,
    onLogout: () -> Unit,
    onApplyLoan: (String) -> Unit,
    onMiniStatement: () -> Unit,
    onMyLoans: () -> Unit,
    onTopUpSavings: () -> Unit,
    onChangePassword: () -> Unit,
    onProfile: () -> Unit,
    onSettings: () -> Unit
) {
    val app = LocalContext.current.applicationContext as SaccoApplication
    val viewModel = viewModel<DashboardViewModel>(factory = DashboardViewModel.factory(app.repository))
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadDashboard(userId)
    }

    Scaffold(
        topBar = {
            DashboardTopBar(
                onLogout = onLogout,
                onProfile = onProfile,
                onSettings = onSettings
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {

            is DashboardUiState.Loading -> {
                Box(
                    modifier         = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SaccoBlue)
                }
            }

            is DashboardUiState.Success -> {
                DashboardContent(
                    data = state.data,
                    onApplyLoan = onApplyLoan,
                    onMiniStatement = onMiniStatement,
                    onMyLoans = onMyLoans,
                    onTopUpSavings = onTopUpSavings,
                    onChangePassword = onChangePassword,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is DashboardUiState.Error -> {
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

// ── Top app bar ──────────────────────────────────────────────────────────────

@Composable
private fun DashboardTopBar(
    onLogout: () -> Unit,
    onProfile: () -> Unit,
    onSettings: () -> Unit
) {
    var overflowExpanded by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = { /* drawer: deferred */ }) {
                Icon(
                    imageVector        = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.cd_open_menu),
                    tint               = White
                )
            }
        },
        title = {
            Text(
                text          = stringResource(R.string.title_dashboard),
                color         = White,
                fontWeight    = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        },
        actions = {
            IconButton(onClick = onProfile) {
                Icon(
                    imageVector        = Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.cd_profile),
                    tint               = White
                )
            }
            Box {
                IconButton(onClick = { overflowExpanded = true }) {
                    Icon(
                        imageVector        = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.cd_more_options),
                        tint               = White
                    )
                }
                DropdownMenu(
                    expanded         = overflowExpanded,
                    onDismissRequest = { overflowExpanded = false }
                ) {
                    DropdownMenuItem(
                        text    = { Text(stringResource(R.string.menu_profile)) },
                        onClick = { overflowExpanded = false; onProfile() }
                    )
                    DropdownMenuItem(
                        text    = { Text(stringResource(R.string.menu_settings)) },
                        onClick = { overflowExpanded = false; onSettings() }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text    = { Text(stringResource(R.string.btn_logout), color = SaccoRed) },
                        onClick = { overflowExpanded = false; onLogout() }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = SaccoBlue
        )
    )
}

// ── Main content ─────────────────────────────────────────────────────────────

@Composable
private fun DashboardContent(
    data: DashboardData,
    onApplyLoan: (String) -> Unit,
    onMiniStatement: () -> Unit,
    onMyLoans: () -> Unit,
    onTopUpSavings: () -> Unit,
    onChangePassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showBalanceDialog by remember { mutableStateOf(false) }

    if (showBalanceDialog) {
        CheckBalanceDialog(
            savingsBalance = data.savingsBalance,
            shareCapital = data.shareCapital,
            onDismiss = { showBalanceDialog = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        GreetingSection(memberName = data.memberName)

        SectionLabel(text = stringResource(R.string.section_quick_actions))
        QuickActionsRow(
            onApplyLoan = { onApplyLoan(data.memberName) },
            onMyLoans = onMyLoans,
            onTopUpSavings = onTopUpSavings,
            onChangePassword = onChangePassword
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingSmall)))

        SectionLabel(text = stringResource(R.string.section_account_summary))
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingTiny)))

        SavingsCard(
            maskedAccountNumber = data.maskedAccountNumber,
            onMiniStatement = onMiniStatement,
            onCheckBalance = { showBalanceDialog = true }
        )
        ShareCapitalCard(shareCapital = data.shareCapital)
        LoanBalanceCard(
            activeLoanCount = data.activeLoanCount,
            onMyLoans       = onMyLoans
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingLarge)))
    }
}

// ── Greeting ─────────────────────────────────────────────────────────────────

@Composable
private fun GreetingSection(memberName: String) {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.spacingMedium),
                vertical   = dimensionResource(R.dimen.spacingMedium) + 4.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text  = stringResource(R.string.greeting_member, memberName),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingTiny)))
            Text(
                text  = stringResource(R.string.greeting_prompt),
                style = MaterialTheme.typography.bodyMedium.copy(color = SaccoRed)
            )
        }
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacingSmall) + 4.dp))
        Icon(
            imageVector        = Icons.Default.AccountCircle,
            contentDescription = stringResource(R.string.cd_user_avatar),
            tint               = SaccoBlue,
            modifier           = Modifier.size(dimensionResource(R.dimen.avatarSize))
        )
    }
}

// ── Quick actions ─────────────────────────────────────────────────────────────

private data class QuickAction(val label: String, val icon: ImageVector, val onClick: () -> Unit)

@Composable
private fun QuickActionsRow(
    onApplyLoan: () -> Unit,
    onMyLoans: () -> Unit,
    onTopUpSavings: () -> Unit,
    onChangePassword: () -> Unit
) {
    val actions = listOf(
        QuickAction(stringResource(R.string.action_topup_savings), Icons.Default.AccountBalance) { onTopUpSavings() },
        QuickAction(stringResource(R.string.action_apply_loan), Icons.Default.Assignment) { onApplyLoan() },
        QuickAction(stringResource(R.string.action_my_loans), Icons.Default.List) { onMyLoans() },
        QuickAction(stringResource(R.string.action_change_password), Icons.Default.Lock) { onChangePassword() }
    )

    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = dimensionResource(R.dimen.spacingSmall),
                vertical   = dimensionResource(R.dimen.spacingSmall)
            ),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        actions.forEach { action ->
            QuickActionItem(label = action.label, icon = action.icon, onClick = action.onClick)
        }
    }
}

@Composable
private fun QuickActionItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        modifier            = Modifier
            .width(dimensionResource(R.dimen.quickActionItemWidth))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier         = Modifier
                .size(dimensionResource(R.dimen.quickActionIconSize))
                .background(color = SaccoBlue, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = icon,
                contentDescription = label,
                tint               = White,
                modifier           = Modifier.size(dimensionResource(R.dimen.quickActionIconInner))
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingXSmall)))
        Text(
            text      = label,
            style     = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines  = 2
        )
    }
}

// ── Summary cards ─────────────────────────────────────────────────────────────

@Composable
private fun SavingsCard(
    maskedAccountNumber: String,
    onMiniStatement: () -> Unit,
    onCheckBalance: () -> Unit
) {
    SummaryCard {
        CardHeader(
            icon     = Icons.Default.CreditCard,
            title    = stringResource(R.string.card_my_savings),
            subtitle = maskedAccountNumber
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacingSmall) + 2.dp))
        CardActionRow(
            leftLabel = stringResource(R.string.btn_check_balance),
            rightLabel = stringResource(R.string.btn_mini_statement),
            onLeftClick = onCheckBalance,
            onRightClick = onMiniStatement
        )
    }
}

@Composable
private fun ShareCapitalCard(shareCapital: Double) {
    SummaryCard {
        CardHeader(
            icon     = Icons.Default.TrendingUp,
            title    = stringResource(R.string.card_share_capital),
            subtitle = stringResource(R.string.currency_amount, "%.2f".format(shareCapital))
        )
    }
}

@Composable
private fun LoanBalanceCard(activeLoanCount: Int, onMyLoans: () -> Unit) {
    SummaryCard {
        CardHeader(
            icon     = Icons.Default.Assignment,
            title    = stringResource(R.string.card_loan_balance),
            subtitle = stringResource(R.string.loans_given, activeLoanCount)
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacingSmall) + 2.dp))
        CardActionRow(
            leftLabel    = stringResource(R.string.btn_check_balance),
            rightLabel   = stringResource(R.string.btn_view_my_loans),
            onRightClick = onMyLoans
        )
    }
}

@Composable
private fun CheckBalanceDialog(
    savingsBalance: Double,
    shareCapital: Double,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.dialog_balance_title),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.dialog_balance_savings),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MediumGrey)
                    )
                    Text(
                        text = stringResource(R.string.currency_amount, "%.2f".format(savingsBalance)),
                        fontWeight = FontWeight.Bold
                    )
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.dialog_balance_share),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MediumGrey)
                    )
                    Text(
                        text = stringResource(R.string.currency_amount, "%.2f".format(shareCapital)),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.btn_close),
                    color = SaccoBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

// ── Card sub-components ───────────────────────────────────────────────────────

@Composable
private fun SummaryCard(content: @Composable ColumnScope.() -> Unit) {
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
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.cardPaddingHorizontal),
                vertical   = dimensionResource(R.dimen.cardPaddingVertical)
            ),
            content = content
        )
    }
}

@Composable
private fun CardHeader(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = SaccoBlue,
            modifier           = Modifier.size(dimensionResource(R.dimen.cardHeaderIconSize))
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacingSmall) + 4.dp))
        Column {
            Text(
                text  = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text  = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(color = MediumGrey)
            )
        }
    }
}

@Composable
private fun CardActionRow(
    leftLabel: String,
    rightLabel: String,
    onLeftClick: () -> Unit = {},
    onRightClick: () -> Unit = {}
) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(onClick = onLeftClick) {
            Text(
                text  = leftLabel,
                color = SaccoBlue,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
        TextButton(onClick = onRightClick) {
            Text(
                text  = rightLabel,
                color = SaccoBlue,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

// ── Shared helpers ────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String) {
    Text(
        text     = text,
        style    = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.SemiBold,
            color      = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier.padding(
            start  = dimensionResource(R.dimen.cardPaddingHorizontal),
            end    = dimensionResource(R.dimen.cardPaddingHorizontal),
            top    = dimensionResource(R.dimen.spacingSmall),
            bottom = dimensionResource(R.dimen.spacingTiny)
        )
    )
}

