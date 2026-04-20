package com.ndejje.saccomobileapplication

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ndejje.saccomobileapplication.view.AdminScreen
import com.ndejje.saccomobileapplication.view.ChangePasswordScreen
import com.ndejje.saccomobileapplication.view.DashboardScreen
import com.ndejje.saccomobileapplication.view.ForgotPasswordScreen
import com.ndejje.saccomobileapplication.view.LoanApplicationScreen
import com.ndejje.saccomobileapplication.view.LoginScreen
import com.ndejje.saccomobileapplication.view.MyLoansScreen
import com.ndejje.saccomobileapplication.view.ProfileScreen
import com.ndejje.saccomobileapplication.view.RegisterScreen
import com.ndejje.saccomobileapplication.view.SettingsScreen
import com.ndejje.saccomobileapplication.view.TopUpSavingsScreen
import com.ndejje.saccomobileapplication.view.TransactionLedgerScreen
import com.ndejje.saccomobileapplication.viewmodel.AuthViewModel

// Route constants — defined once to avoid typos
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home/{userId}"
    const val LOAN_APPLICATION = "loan_application/{userId}/{memberName}"
    const val TRANSACTION_LEDGER = "transaction_ledger/{userId}"
    const val MY_LOANS = "my_loans/{userId}"
    const val CHANGE_PASSWORD = "change_password/{userId}"
    const val TOPUP_SAVINGS = "topup_savings/{userId}"
    const val PROFILE = "profile/{userId}"
    const val FORGOT_PASSWORD = "forgot_password"
    const val SETTINGS = "settings/{userId}"
    const val ADMIN_PANEL = "admin_panel"
}

@Composable
fun AppNavigation(viewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {

        // ── Login ──────────────────────────────────────────────────────────────
        composable(Routes.LOGIN) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { userId ->
                    navController.navigate("home/$userId") {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    viewModel.resetState()
                    navController.navigate(Routes.REGISTER)
                },
                onAdminLogin = { navController.navigate(Routes.ADMIN_PANEL) },
                onForgotPassword = { navController.navigate(Routes.FORGOT_PASSWORD) }
            )
        }

        // ── Register ───────────────────────────────────────────────────────────
        composable(Routes.REGISTER) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = { navController.popBackStack() }
            )
        }

        // ── Dashboard ──────────────────────────────────────────────────────────
        composable(
            route = Routes.HOME,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            DashboardScreen(
                userId = userId,
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                },
                onApplyLoan = { memberName ->
                    navController.navigate("loan_application/$userId/$memberName")
                },
                onMiniStatement = { navController.navigate("transaction_ledger/$userId") },
                onMyLoans = { navController.navigate("my_loans/$userId") },
                onTopUpSavings = { navController.navigate("topup_savings/$userId") },
                onChangePassword = { navController.navigate("change_password/$userId") },
                onProfile = { navController.navigate("profile/$userId") },
                onSettings = { navController.navigate("settings/$userId") }
            )
        }

        // ── Loan Application ───────────────────────────────────────────────────
        composable(
            route = Routes.LOAN_APPLICATION,
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("memberName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val memberName = backStackEntry.arguments?.getString("memberName") ?: ""
            LoanApplicationScreen(
                userId = userId,
                memberName = memberName,
                onNavigateBack = { navController.popBackStack() },
                onNavigateNext = { navController.popBackStack() }
            )
        }

        // ── Transaction Ledger ─────────────────────────────────────────────────
        composable(
            route = Routes.TRANSACTION_LEDGER,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            TransactionLedgerScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ── My Loans ───────────────────────────────────────────────────────────
        composable(
            route = Routes.MY_LOANS,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            MyLoansScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.CHANGE_PASSWORD,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            ChangePasswordScreen(
                userId = backStackEntry.arguments?.getInt("userId") ?: 0,
                onNavigateBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.TOPUP_SAVINGS,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            TopUpSavingsScreen(
                userId = backStackEntry.arguments?.getInt("userId") ?: 0,
                onNavigateBack = { navController.popBackStack() },
                onSuccess = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.PROFILE,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->
            ProfileScreen(
                userId = backStackEntry.arguments?.getInt("userId") ?: 0,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.FORGOT_PASSWORD) {
            ForgotPasswordScreen(
                onNavigateBack = { navController.popBackStack() },
                onSuccess = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Routes.SETTINGS,
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.HOME) { inclusive = true }
                    }
                }
            )
        }

        // ── Admin Panel ────────────────────────────────────────────────────────
        composable(Routes.ADMIN_PANEL) {
            AdminScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}