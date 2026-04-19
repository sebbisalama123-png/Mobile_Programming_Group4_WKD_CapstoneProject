package com.ndejje.saccomobileapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val userId:       Int    = 0,
    val fullName:     String,
    val phoneNumber:  String,       // login identifier
    val idNumber:     String,
    val passwordHash: String
)