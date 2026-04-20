@file:OptIn(ExperimentalMaterial3Api::class)

package com.ndejje.saccomobileapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ndejje.saccomobileapplication.R
import com.ndejje.saccomobileapplication.SaccoApplication
import com.ndejje.saccomobileapplication.ui.theme.MediumGrey
import com.ndejje.saccomobileapplication.ui.theme.SaccoBlue
import com.ndejje.saccomobileapplication.ui.theme.SaccoRed
import com.ndejje.saccomobileapplication.ui.theme.White
import com.ndejje.saccomobileapplication.viewmodel.TopUpSavingsViewModel

@Composable
fun TopUpSavingsScreen(
    userId: Int,
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val app = LocalContext.current.applicationContext as SaccoApplication
    val viewModel = viewModel<TopUpSavingsViewModel>(
        factory = TopUpSavingsViewModel.factory(app.repository, userId)
    )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
        }
    }

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
                        text = stringResource(R.string.title_topup_savings),
                        color = White,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = SaccoBlue)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = dimensionResource(R.dimen.spacingMedium))
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalance,
                contentDescription = null,
                tint = SaccoBlue,
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = dimensionResource(R.dimen.spacingMedium))
            )

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.spacingMedium))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.cardPaddingHorizontal)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.topup_current_balance),
                        style = MaterialTheme.typography.bodyMedium.copy(color = MediumGrey)
                    )
                    Text(
                        text = stringResource(R.string.currency_amount, "%.2f".format(uiState.currentBalance)),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = SaccoBlue
                        )
                    )
                }
            }

            OutlinedTextField(
                value = uiState.amountInput,
                onValueChange = viewModel::setAmount,
                label = { Text(stringResource(R.string.label_topup_amount)) },
                isError = uiState.errorMessage != null,
                supportingText = uiState.errorMessage?.let {
                    { Text(text = it, color = SaccoRed) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SaccoBlue,
                    focusedLabelColor = SaccoBlue,
                    cursorColor = SaccoBlue
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(R.dimen.spacingMedium))
            )

            Button(
                onClick = viewModel::submit,
                enabled = uiState.isAmountValid && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.buttonHeight))
                    .padding(top = dimensionResource(R.dimen.spacingLarge)),
                shape = RoundedCornerShape(dimensionResource(R.dimen.buttonCornerRadius)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SaccoBlue,
                    disabledContainerColor = MediumGrey
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimensionResource(R.dimen.progressIndicatorSize)),
                        color = White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = stringResource(R.string.btn_topup), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}