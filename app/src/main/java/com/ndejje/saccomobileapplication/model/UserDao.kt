package com.ndejje.saccomobileapplication.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE phoneNumber = :phoneNumber LIMIT 1")
    suspend fun findByPhoneNumber(phoneNumber: String): UserEntity?

    @Query("SELECT * FROM users WHERE phoneNumber = :phoneNumber AND passwordHash = :passwordHash LIMIT 1")
    suspend fun login(phoneNumber: String, passwordHash: String): UserEntity?

    @Query("SELECT * FROM users WHERE userId = :userId LIMIT 1")
    suspend fun findById(userId: Int): UserEntity?

    @Query("UPDATE users SET passwordHash = :passwordHash WHERE userId = :userId")
    suspend fun updatePassword(userId: Int, passwordHash: String)

    @Query("SELECT * FROM users WHERE phoneNumber = :phoneNumber AND idNumber = :idNumber LIMIT 1")
    suspend fun findByPhoneAndId(phoneNumber: String, idNumber: String): UserEntity?
}