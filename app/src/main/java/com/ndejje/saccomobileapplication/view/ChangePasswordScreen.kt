@file:OptIn(ExperimentalMaterial3Api::class)


package com.ndejje.saccomobileapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ndejje.saccomobileapplication.R
import com.ndejje.saccomobileapplication.SaccoApplication
import com.ndejje.saccomobileapplication.ui.theme.MediumGrey
import com.ndejje.saccomobileapplication.ui.theme.SaccoBlue
import com.ndejje.saccomobileapplication.ui.theme.SaccoRed
import com.ndejje.saccomobileapplication.ui.theme.White
import com.ndejje.saccomobileapplication.viewmodel.ChangePasswordViewModel

@Composable
fun ChangePasswordScreen(
    userId: Int,
    onNavigateBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val context = LocalContext.current
    val app = context.applicationContext as SaccoApplication
    val viewModel = viewModel<ChangePasswordViewModel>(
        factory = ChangePasswordViewModel.factory(app.repository, userId)
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
                        text = stringResource(R.string.title_change_password),
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
                .padding(
                    horizontal = dimensionResource(R.dimen.spacingMedium),
                    vertical = dimensionResource(R.dimen.spacingMedium)
                )
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = SaccoBlue,
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.CenterHorizontally)
            )

            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = SaccoRed,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.spacingMedium))
                )
            }

            PasswordField(
                value = uiState.currentPassword,
                label = stringResource(R.string.label_current_password),
                onValueChange = viewModel::setCurrentPassword,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacingMedium))
            )
            PasswordField(
                value = uiState.newPassword,
                label = stringResource(R.string.label_new_password),
                onValueChange = viewModel::setNewPassword,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacingSmall))
            )
            PasswordField(
                value = uiState.confirmPassword,
                label = stringResource(R.string.label_confirm_new_password),
                isError = uiState.confirmPassword.isNotEmpty() && !uiState.newPasswordsMatch,
                errorText = stringResource(R.string.error_passwords_mismatch),
                onValueChange = viewModel::setConfirmPassword,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.spacingSmall))
            )

            Button(
                onClick = viewModel::submit,
                enabled = uiState.isFormValid && !uiState.isLoading,
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
                    Text(
                        text = stringResource(R.string.btn_change_password),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    label: String,
    isError: Boolean = false,
    errorText: String = "",
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        isError = isError,
        supportingText = if (isError) {
            { Text(text = errorText, color = SaccoRed) }
        } else {
            null
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                Icon(
                    imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visible) {
                        stringResource(R.string.cd_hide_password)
                    } else {
                        stringResource(R.string.cd_show_password)
                    },
                    tint = MediumGrey
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SaccoBlue,
            focusedLabelColor = SaccoBlue,
            cursorColor = SaccoBlue
        ),
        modifier = modifier.fillMaxWidth()
    )
}
