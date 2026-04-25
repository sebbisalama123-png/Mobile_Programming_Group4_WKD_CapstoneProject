package com.ndejje.saccomobileapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ndejje.saccomobileapplication.R
import com.ndejje.saccomobileapplication.ui.theme.MediumGrey
import com.ndejje.saccomobileapplication.ui.theme.SaccoBlue
import com.ndejje.saccomobileapplication.ui.theme.SaccoRed
import com.ndejje.saccomobileapplication.viewmodel.AuthUiState
import com.ndejje.saccomobileapplication.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel:            AuthViewModel,
    onLoginSuccess:       (Int) -> Unit,
    onNavigateToRegister: () -> Unit,
    onAdminLogin:         () -> Unit,
    onForgotPassword:     () -> Unit
) {
    val authState by viewModel.authState.collectAsState()

    var phoneInput      by remember { mutableStateOf("") }
    var passwordInput   by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        if (authState is AuthUiState.Success) {
            onLoginSuccess((authState as AuthUiState.Success).userId)
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = dimensionResource(R.dimen.screenPadding)),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // ── Logo block ──────────────────────────────────────────────────────
        Column(
            modifier            = Modifier.padding(top = dimensionResource(R.dimen.logoPaddingTop)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(dimensionResource(R.dimen.logoBoxSize))
                    .background(
                        color = SaccoBlue.copy(alpha = 0.10f),
                        shape = RoundedCornerShape(dimensionResource(R.dimen.logoCornerRadius))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.AccountBalance,
                    contentDescription = stringResource(R.string.cd_sacco_logo),
                    tint               = SaccoBlue,
                    modifier           = Modifier.size(dimensionResource(R.dimen.logoIconSize))
                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingSmall) +  dimensionResource(R.dimen.spacingTiny)))
            Text(
                text  = stringResource(R.string.app_name),
                style = MaterialTheme.typography.titleMedium.copy(
                    color      = SaccoBlue,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        // ── Form block ──────────────────────────────────────────────────────
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.spacingLarge) + dimensionResource(R.dimen.spacingSmall)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text  = stringResource(R.string.login_headline),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingXSmall)))
            Text(
                text  = stringResource(R.string.login_subtitle),
                style = MaterialTheme.typography.bodyMedium.copy(color = MediumGrey)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingLarge) + dimensionResource(R.dimen.spacingTiny)))

            // Phone Number field
            OutlinedTextField(
                value         = phoneInput,
                onValueChange = { phoneInput = it },
                label         = { Text(stringResource(R.string.label_phone_number)) },
                leadingIcon   = {
                    Icon(
                        imageVector        = Icons.Default.Person,
                        contentDescription = null,
                        tint               = SaccoBlue
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction    = ImeAction.Next
                ),
                modifier   = Modifier.fillMaxWidth(),
                singleLine = true,
                colors     = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = SaccoBlue,
                    focusedLabelColor       = SaccoBlue,
                    focusedLeadingIconColor = SaccoBlue,
                    cursorColor             = SaccoBlue,
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingMedium)))

            // Password field
            OutlinedTextField(
                value         = passwordInput,
                onValueChange = { passwordInput = it },
                label         = { Text(stringResource(R.string.label_password)) },
                leadingIcon   = {
                    Icon(
                        imageVector        = Icons.Default.Lock,
                        contentDescription = null,
                        tint               = SaccoBlue
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector        = if (passwordVisible)
                                Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (passwordVisible)
                                stringResource(R.string.cd_hide_password)
                            else
                                stringResource(R.string.cd_show_password),
                            tint = MediumGrey
                        )
                    }
                },
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier        = Modifier.fillMaxWidth(),
                singleLine      = true,
                colors          = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = SaccoBlue,
                    focusedLabelColor       = SaccoBlue,
                    focusedLeadingIconColor = SaccoBlue,
                    cursorColor             = SaccoBlue,
                )
            )

            if (authState is AuthUiState.Error) {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingSmall)))
                Text(
                    text  = (authState as AuthUiState.Error).message,
                    color = SaccoRed,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingLarge)))

            // LOGIN button
            Button(
                onClick  = { viewModel.login(phoneInput, passwordInput) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.buttonHeight)),
                enabled = authState !is AuthUiState.Loading,
                colors  = ButtonDefaults.buttonColors(containerColor = SaccoBlue),
                shape   = RoundedCornerShape(dimensionResource(R.dimen.buttonCornerRadius))
            ) {
                if (authState is AuthUiState.Loading) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(dimensionResource(R.dimen.progressIndicatorSize)),
                        color       = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = dimensionResource(R.dimen.spacingXXSmall)
                    )
                } else {
                    Text(
                        text          = stringResource(R.string.btn_login),
                        fontWeight    = FontWeight.Bold,
                        fontSize      = 16.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingSmall) + dimensionResource(R.dimen.spacingTiny)))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                TextButton(onClick = onForgotPassword) {
                    Text(
                        text  = stringResource(R.string.link_forgot_password),
                        color = SaccoRed,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                TextButton(onClick = onNavigateToRegister) {
                    Text(
                        text  = stringResource(R.string.link_no_account),
                        color = SaccoBlue,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // ── Footer ──────────────────────────────────────────────────────────
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.spacingLarge)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Text(
                text  = stringResource(R.string.copyright),
                style = MaterialTheme.typography.bodySmall.copy(color = MediumGrey)
            )
            TextButton(
                onClick        = onAdminLogin,
                contentPadding = PaddingValues(dimensionResource(R.dimen.spacingNone))
            ) {
                Text(
                    text  = stringResource(R.string.btn_admin_login),
                    style = MaterialTheme.typography.bodySmall.copy(color = MediumGrey)
                )
            }
        }
    }
}