package com.ndejje.saccomobileapplication.model

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SaccoRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: SaccoRepository

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = SaccoRepository(
            userDao = database.userDao(),
            memberAccountDao = database.memberAccountDao(),
            transactionDao = database.transactionDao(),
            loanRequestDao = database.loanRequestDao()
        )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testRegisterUserSuccess() = runBlocking {
        // Act
        val result = repository.registerUser(
            fullName = "John Doe",
            phoneNumber = "0770000000",
            idNumber = "CM123456789",
            password = "password123"
        )

        // Assert
        assertTrue(result > 0) // Valid userId returned

        val user = repository.getUserById(result)
        assertNotNull(user)
        assertEquals("John Doe", user?.fullName)
        assertEquals("0770000000", user?.phoneNumber)
        assertEquals("CM123456789", user?.idNumber)

        val account = repository.getMemberAccount(result)
        assertNotNull(account)
        assertEquals(1000.0, account?.savingsBalance)
        assertEquals(2000.0, account?.shareCapital)

        val transactions = repository.getTransactions(result)
        assertEquals(1, transactions.size)
        assertEquals("Account Opening Deposit", transactions[0].description)
        assertEquals(1000.0, transactions[0].amount)
        assertEquals("CREDIT", transactions[0].type)
    }

    @Test
    fun testRegisterUserDuplicatePhoneReturnsMinusOne() = runBlocking {
        // Arrange: Insert the first user
        repository.registerUser(
            fullName = "John Doe",
            phoneNumber = "0770000000",
            idNumber = "CM123456789",
            password = "password123"
        )

        // Act: Try to register with same phone
        val result = repository.registerUser(
            fullName = "Jane Doe",
            phoneNumber = "0770000000",
            idNumber = "CF987654321",
            password = "password456"
        )

        // Assert
        assertEquals(-1, result) // -1 signifies duplicate phone number
    }
}
