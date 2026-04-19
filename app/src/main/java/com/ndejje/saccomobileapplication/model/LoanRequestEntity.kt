package com.ndejje.saccomobileapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "loan_requests")
data class LoanRequestEntity(
    @PrimaryKey val requestId:  String,
    val userId:      Int,
    val memberName:  String,
    val loanProduct: String,
    val amount:      Double,
    val status:      String,    // "PENDING" | "APPROVED" | "REJECTED"
    val createdAt:   Long
)