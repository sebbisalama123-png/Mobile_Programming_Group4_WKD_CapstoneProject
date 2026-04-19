package com.ndejje.saccomobileapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "member_accounts")
data class MemberAccountEntity(
    @PrimaryKey val userId:        Int,
    val accountNumber:  String,
    val savingsBalance: Double,
    val shareCapital:   Double
)