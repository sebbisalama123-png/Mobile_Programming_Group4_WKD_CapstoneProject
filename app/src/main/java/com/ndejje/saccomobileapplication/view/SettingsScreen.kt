@file:OptIn(ExperimentalMaterial3Api::class)

package com.ndejje.saccomobileapplication.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ndejje.saccomobileapplication.R
import com.ndejje.saccomobileapplication.ui.theme.MediumGrey
import com.ndejje.saccomobileapplication.ui.theme.SaccoBlue
import com.ndejje.saccomobileapplication.ui.theme.SaccoRed
import com.ndejje.saccomobileapplication.ui.theme.White

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit
) {
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
                        text = stringResource(R.string.title_settings),
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
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingMedium)))

            SettingsSectionLabel(text = stringResource(R.string.settings_section_app))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimensionResource(R.dimen.cardCornerRadius))
            ) {
                Column(modifier = Modifier.padding(dimensionResource(R.dimen.cardPaddingHorizontal))) {
                    SettingsRow(
                        label = stringResource(R.string.settings_app_name),
                        value = stringResource(R.string.settings_app_value)
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                    SettingsRow(
                        label = stringResource(R.string.settings_sacco_name),
                        value = stringResource(R.string.settings_sacco_value)
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
                    SettingsRow(
                        label = stringResource(R.string.settings_contact),
                        value = stringResource(R.string.settings_contact_value)
                    )
                }
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingMedium)))

            SettingsSectionLabel(text = stringResource(R.string.settings_section_account))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(dimensionResource(R.dimen.cardCornerRadius))
            ) {
                ListItem(
                    leadingContent = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            tint = SaccoRed
                        )
                    },
                    headlineContent = {
                        TextButton(onClick = onLogout, contentPadding = PaddingValues(0.dp)) {
                            Text(
                                text = stringResource(R.string.settings_logout),
                                color = SaccoRed,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingLarge)))
        }
    }
}

@Composable
private fun SettingsSectionLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.SemiBold,
            color = MediumGrey
        ),
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.spacingSmall))
    )
}

@Composable
private fun SettingsRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.spacingSmall)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(color = MediumGrey),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}