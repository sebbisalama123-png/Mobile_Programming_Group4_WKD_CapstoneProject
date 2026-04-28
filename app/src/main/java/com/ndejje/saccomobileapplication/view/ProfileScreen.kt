@file:OptIn(ExperimentalMaterial3Api::class)

package com.ndejje.saccomobileapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.ndejje.saccomobileapplication.ui.theme.MediumGrey
import com.ndejje.saccomobileapplication.ui.theme.SaccoBlue
import com.ndejje.saccomobileapplication.ui.theme.SaccoRed
import com.ndejje.saccomobileapplication.ui.theme.White
import com.ndejje.saccomobileapplication.viewmodel.ProfileData
import com.ndejje.saccomobileapplication.viewmodel.ProfileUiState
import com.ndejje.saccomobileapplication.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    userId: Int,
    onNavigateBack: () -> Unit
) {
    val app = LocalContext.current.applicationContext as SaccoApplication
    val viewModel = viewModel<ProfileViewModel>(
        factory = ProfileViewModel.factory(app.repository, userId)
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                            tint = White
                        )
                    }
                },
                title = {
                    Text(
                        text = stringResource(R.string.title_profile),
                        color = White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SaccoBlue)
            )
        }
    ) { innerPadding ->
        when (val state = uiState) {
            is ProfileUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = SaccoBlue)
                }
            }

            is ProfileUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = SaccoRed)
                }
            }

            is ProfileUiState.Success -> {
                ProfileContent(
                    data = state.data,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun ProfileContent(data: ProfileData, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.spacingMedium))
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingMedium)))

        Box(
            modifier = Modifier
                .size(dimensionResource(R.dimen.spacingXXLarge))
                .background(SaccoBlue, CircleShape)
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = null,
                tint = White,
                modifier = Modifier.size(dimensionResource(R.dimen.spacingXLarge))
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingSmall)))

        Text(
            text = data.fullName,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingMedium)))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(dimensionResource(R.dimen.cardCornerRadius))
        ) {
            Column(modifier = Modifier.padding(dimensionResource(R.dimen.cardPaddingHorizontal))) {
                ProfileRow(stringResource(R.string.profile_full_name), data.fullName)
                ProfileDivider()
                ProfileRow(stringResource(R.string.profile_phone), data.phoneNumber)
                ProfileDivider()
                ProfileRow(stringResource(R.string.profile_id_number), data.idNumber)
                ProfileDivider()
                ProfileRow(stringResource(R.string.profile_account_number), data.accountNumber)
                ProfileDivider()
                ProfileRow(
                    stringResource(R.string.profile_savings_balance),
                    stringResource(R.string.currency_amount, "%.2f".format(data.savingsBalance))
                )
                ProfileDivider()
                ProfileRow(
                    stringResource(R.string.profile_share_capital),
                    stringResource(R.string.currency_amount, "%.2f".format(data.shareCapital))
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingLarge)))
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.spacingSmall)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium.copy(color = MediumGrey))
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacingSmall)))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}

@Composable
private fun ProfileDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = dimensionResource(R.dimen.hairlineStroke))
}