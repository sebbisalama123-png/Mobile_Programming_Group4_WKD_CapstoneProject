package com.ndejje.saccomobileapplication.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.ndejje.saccomobileapplication.R


@Composable
fun HomeScreen(
    username: String,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.screenPadding)),
        verticalArrangement  = Arrangement.Center,
        horizontalAlignment  = Alignment.CenterHorizontally
    ) {
        Text(
            text      = stringResource(R.string.label_welcome, username),
            style     = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacingLarge)))

        Button(
            onClick  = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.buttonHeight))
        ) {
            Text(stringResource(R.string.btn_logout))
        }
    }
}