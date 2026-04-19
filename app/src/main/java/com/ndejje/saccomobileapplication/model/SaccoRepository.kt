package com.ndejje.saccomobileapplication.model

import java.util.UUID

class SaccoRepository(
    private val userDao:          UserDao,
    private val memberAccountDao: MemberAccountDao,
    private val transactionDao:   TransactionDao,
    private val loanRequestDao:   LoanRequestDao
) {

    private fun hashPassword(password: String): String = password.hashCode().toString()

    /**
     * Register a new member.
     * Seeds a [MemberAccountEntity] and an opening [TransactionEntity] on success.
     *
     * @return the generated userId on success, -1 if phoneNumber is taken, -2 on DB error.
     */
    suspend fun registerUser(
        fullName:    String,
        phoneNumber: String,
        idNumber:    String,
        password:    String
    ): Int {
        val trimmedPhone = phoneNumber.trim()
        if (userDao.findByPhoneNumber(trimmedPhone) != null) return -1   // duplicate

        val newUser = UserEntity(
            fullName     = fullName.trim(),
            phoneNumber  = trimmedPhone,
            idNumber     = idNumber.trim(),
            passwordHash = hashPassword(password)
        )
        val rowId = userDao.insertUser(newUser)
        if (rowId == -1L) return -2

        val savedUser = userDao.findByPhoneNumber(trimmedPhone) ?: return -2
        val userId    = savedUser.userId

        // Seed member account with opening balance
        memberAccountDao.insertAccount(
            MemberAccountEntity(
                userId        = userId,
                accountNumber = "MUSC-${userId.toString().padStart(6, '0')}",
                savingsBalance = 1_000.0,
                shareCapital  = 2_000.0
            )
        )

        // Seed opening deposit transaction
        transactionDao.insertTransaction(
            TransactionEntity(
                transactionId = UUID.randomUUID().toString(),
                userId        = userId,
                date          = System.currentTimeMillis(),
                description   = "Account Opening Deposit",
                amount        = 1_000.0,
                type          = "CREDIT"
            )
        )

        return userId
    }

    /** Returns the matching [UserEntity] or null if credentials are invalid. */
    suspend fun loginUser(phoneNumber: String, password: String): UserEntity? =
        userDao.login(phoneNumber.trim(), hashPassword(password))

    suspend fun getUserById(userId: Int): UserEntity? =
        userDao.findById(userId)

    suspend fun getMemberAccount(userId: Int): MemberAccountEntity? =
        memberAccountDao.getByUserId(userId)

    suspend fun getTransactions(userId: Int): List<TransactionEntity> =
        transactionDao.getByUserId(userId)

    /**
     * Submit a loan application.
     * [memberName] is the display name stored on the loan record for the Admin panel.
     */
    suspend fun saveLoanRequest(
        userId:      Int,
        memberName:  String,
        loanProduct: String,
        amount:      Double
    ) {
        loanRequestDao.insertLoanRequest(
            LoanRequestEntity(
                requestId   = UUID.randomUUID().toString(),
                userId      = userId,
                memberName  = memberName,
                loanProduct = loanProduct,
                amount      = amount,
                status      = "PENDING",
                createdAt   = System.currentTimeMillis()
            )
        )
    }

    suspend fun getPendingLoanRequests(): List<LoanRequestEntity> =
        loanRequestDao.getPendingRequests()

    suspend fun updateLoanStatus(requestId: String, status: String) =
        loanRequestDao.updateStatus(requestId, status)

    suspend fun getApprovedLoanCount(userId: Int): Int =
        loanRequestDao.getApprovedCount(userId)

    suspend fun getLoansByUserId(userId: Int): List<LoanRequestEntity> =
        loanRequestDao.getLoansByUserId(userId)

    /**
     * Changes the user's password after verifying the current one.
     * Returns true on success, false if the current password is wrong.
     */
    suspend fun changePassword(
        userId: Int,
        currentPassword: String,
        newPassword: String
    ): Boolean {
        val user = userDao.findById(userId) ?: return false
        if (user.passwordHash != hashPassword(currentPassword)) return false
        userDao.updatePassword(userId, hashPassword(newPassword))
        return true
    }

    /**
     * Tops up savings: updates the balance and records a CREDIT transaction.
     */
    suspend fun topUpSavings(userId: Int, amount: Double) {
        memberAccountDao.addToSavingsBalance(userId, amount)
        transactionDao.insertTransaction(
            TransactionEntity(
                transactionId = UUID.randomUUID().toString(),
                userId = userId,
                date = System.currentTimeMillis(),
                description = "Savings Top-Up",
                amount = amount,
                type = "CREDIT"
            )
        )
    }

    /**
     * Verifies a member's identity using phone + national ID.
     * Returns the userId on success, null if no match.
     */
    suspend fun verifyIdentity(phoneNumber: String, idNumber: String): Int? =
        userDao.findByPhoneAndId(phoneNumber.trim(), idNumber.trim())?.userId

    /**
     * Sets a new password without requiring the old one.
     * Only call after identity has been verified.
     */
    suspend fun resetPassword(userId: Int, newPassword: String) =
        userDao.updatePassword(userId, hashPassword(newPassword))
}