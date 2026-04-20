package com.ndejje.saccomobileapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ndejje.saccomobileapplication.viewmodel.AuthViewModel
import com.ndejje.saccomobileapplication.viewmodel.AuthViewModelFactory
import com.ndejje.saccomobileapplication.ui.theme.SaccoMobileApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = (application as SaccoApplication).repository
        val viewModel: AuthViewModel by viewModels { AuthViewModelFactory(repository) }

        setContent {
            SaccoMobileApplicationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation(viewModel = viewModel)
                }
            }
        }
    }
}