@file:OptIn(ExperimentalMaterial3Api::class)

package com.ndejje.saccomobileapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.ndejje.saccomobileapplication.viewmodel.RegisterViewModel

// ── Root screen ───────────────────────────────────────────────────────────────

@Composable
fun RegisterScreen(
    onNavigateBack:    () -> Unit,
    onRegisterSuccess: () -> Unit
) {
    val app       = LocalContext.current.applicationContext as SaccoApplication
    val viewModel = viewModel<RegisterViewModel>(factory = RegisterViewModel.factory(app.repository))
    val uiState   by viewModel.uiState.collectAsState()

    var passwordVisible        by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isRegistered) {
        if (uiState.isRegistered) onRegisterSuccess()
    }

    Scaffold(
        topBar = { RegisterTopBar(onNavigateBack = onNavigateBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = dimensionResource(R.dimen.screenPadding),
                    vertical   = dimensionResource(R.dimen.spacingMedium) + 4.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Header ────────────────────────────────────────────────────────
            Text(
                text      = stringResource(R.string.register_subtitle),
                style     = MaterialTheme.typography.bodyMedium.copy(color = MediumGrey),
                textAlign = TextAlign.Center,
                modifier  = Modifier.padding(bottom = dimensionResource(R.dimen.spacingLarge))
            )

            // ── Full Name ─────────────────────────────────────────────────────
            SaccoOutlinedTextField(
                value         = uiState.fullName,
                onValueChange = viewModel::onFullNameChange,
                label         = stringResource(R.string.label_full_name),
                leadingIcon   = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = SaccoBlue)
                }
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingFieldGap)))

            // ── Phone Number ──────────────────────────────────────────────────
            SaccoOutlinedTextField(
                value           = uiState.phoneNumber,
                onValueChange   = viewModel::onPhoneNumberChange,
                label           = stringResource(R.string.label_phone_number),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon     = {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = SaccoBlue)
                }
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingFieldGap)))

            // ── National ID Number ────────────────────────────────────────────
            SaccoOutlinedTextField(
                value           = uiState.idNumber,
                onValueChange   = viewModel::onIdNumberChange,
                label           = stringResource(R.string.label_id_number),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                leadingIcon     = {
                    Icon(Icons.Default.Badge, contentDescription = null, tint = SaccoBlue)
                }
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingFieldGap)))

            // ── Password ──────────────────────────────────────────────────────
            SaccoOutlinedTextField(
                value                = uiState.password,
                onValueChange        = viewModel::onPasswordChange,
                label                = stringResource(R.string.label_password),
                keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon  = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = SaccoBlue)
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
                            tint               = MediumGrey
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingFieldGap)))

            // ── Confirm Password ──────────────────────────────────────────────
            SaccoOutlinedTextField(
                value                = uiState.confirmPassword,
                onValueChange        = viewModel::onConfirmPasswordChange,
                label                = stringResource(R.string.label_confirm_password),
                keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (confirmPasswordVisible)
                    VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon  = {
                    Icon(Icons.Default.Lock, contentDescription = null, tint = SaccoBlue)
                },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector        = if (confirmPasswordVisible)
                                Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (confirmPasswordVisible)
                                stringResource(R.string.cd_hide_password)
                            else
                                stringResource(R.string.cd_show_password),
                            tint               = MediumGrey
                        )
                    }
                }
            )

            // ── Error message ─────────────────────────────────────────────────
            uiState.errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingSmall) + 2.dp))
                Text(
                    text  = message,
                    color = SaccoRed,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingLarge)))

            // ── Register button ───────────────────────────────────────────────
            Button(
                onClick  = viewModel::validateAndRegister,
                enabled  = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.buttonHeight)),
                colors = ButtonDefaults.buttonColors(containerColor = SaccoBlue),
                shape  = RoundedCornerShape(dimensionResource(R.dimen.buttonCornerRadius))
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(dimensionResource(R.dimen.progressIndicatorSize)),
                        color       = White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text          = stringResource(R.string.btn_register),
                        fontWeight    = FontWeight.Bold,
                        fontSize      = 16.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingLarge)))
        }
    }
}

// ── Top app bar ───────────────────────────────────────────────────────────────

@Composable
private fun RegisterTopBar(onNavigateBack: () -> Unit) {
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
                text       = stringResource(R.string.title_register),
                color      = White,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = SaccoBlue
        )
    )
}

// ── Shared field component ────────────────────────────────────────────────────

@Composable
private fun SaccoOutlinedTextField(
    value:                String,
    onValueChange:        (String) -> Unit,
    label:                String,
    modifier:             Modifier                  = Modifier.fillMaxWidth(),
    keyboardOptions:      KeyboardOptions           = KeyboardOptions.Default,
    visualTransformation: VisualTransformation      = VisualTransformation.None,
    leadingIcon:          @Composable (() -> Unit)? = null,
    trailingIcon:         @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value                = value,
        onValueChange        = onValueChange,
        label                = { Text(label) },
        modifier             = modifier,
        singleLine           = true,
        keyboardOptions      = keyboardOptions,
        visualTransformation = visualTransformation,
        leadingIcon          = leadingIcon,
        trailingIcon         = trailingIcon,
        colors               = OutlinedTextFieldDefaults.colors(
            focusedBorderColor      = SaccoBlue,
            focusedLabelColor       = SaccoBlue,
            focusedLeadingIconColor = SaccoBlue,
            cursorColor             = SaccoBlue,
        )
    )
}