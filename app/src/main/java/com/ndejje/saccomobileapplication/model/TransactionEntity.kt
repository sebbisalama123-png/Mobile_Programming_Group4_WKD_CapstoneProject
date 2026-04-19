package com.ndejje.saccomobileapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val transactionId: String,
    val userId:      Int,
    val date:        Long,      // epoch milliseconds
    val description: String,
    val amount:      Double,
    val type:        String     // "CREDIT" | "DEBIT"
)