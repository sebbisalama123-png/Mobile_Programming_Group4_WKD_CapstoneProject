package com.ndejje.saccomobileapplication.viewmodel

import app.cash.turbine.test
import com.ndejje.saccomobileapplication.model.SaccoRepository
import com.ndejje.saccomobileapplication.model.UserEntity
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private lateinit var viewModel: AuthViewModel
    private val mockRepository = mockk<SaccoRepository>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login with empty fields emits Error`() = runTest {
        viewModel.authState.test {
            assertEquals(AuthUiState.Idle, awaitItem())
            
            viewModel.login("", "")
            
            val errorState = awaitItem()
            assertTrue(errorState is AuthUiState.Error)
            assertEquals("All fields are required", (errorState as AuthUiState.Error).message)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login with valid credentials emits Loading then Success`() = runTest {
        val dummyUser = UserEntity(userId = 1, fullName = "Test User", phoneNumber = "12345", idNumber = "ID123", passwordHash = "hash")
        coEvery { mockRepository.loginUser("12345", "password") } returns dummyUser

        viewModel.authState.test {
            assertEquals(AuthUiState.Idle, awaitItem())
            
            viewModel.login("12345", "password")
            
            // Due to StandardTestDispatcher, we can control the flow. Turbine sees the state changes.
            assertEquals(AuthUiState.Loading, awaitItem())
            
            val successState = awaitItem()
            assertTrue(successState is AuthUiState.Success)
            assertEquals(1, (successState as AuthUiState.Success).userId)
            
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `login with invalid credentials emits Loading then Error`() = runTest {
        coEvery { mockRepository.loginUser("12345", "wrongpass") } returns null

        viewModel.authState.test {
            assertEquals(AuthUiState.Idle, awaitItem())
            
            viewModel.login("12345", "wrongpass")
            
            assertEquals(AuthUiState.Loading, awaitItem())
            
            val errorState = awaitItem()
            assertTrue(errorState is AuthUiState.Error)
            assertEquals("Invalid phone number or password", (errorState as AuthUiState.Error).message)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
