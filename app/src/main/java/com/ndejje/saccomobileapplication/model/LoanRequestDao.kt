package com.ndejje.saccomobileapplication.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LoanRequestDao {

    @Insert
    suspend fun insertLoanRequest(request: LoanRequestEntity)

    @Query("SELECT * FROM loan_requests WHERE status = 'PENDING' ORDER BY createdAt DESC")
    suspend fun getPendingRequests(): List<LoanRequestEntity>

    @Query("UPDATE loan_requests SET status = :status WHERE requestId = :requestId")
    suspend fun updateStatus(requestId: String, status: String)

    @Query("SELECT COUNT(*) FROM loan_requests WHERE userId = :userId AND status = 'APPROVED'")
    suspend fun getApprovedCount(userId: Int): Int

    @Query("SELECT * FROM loan_requests WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getLoansByUserId(userId: Int): List<LoanRequestEntity>
}