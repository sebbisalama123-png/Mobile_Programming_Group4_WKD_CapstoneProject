package com.ndejje.saccomobileapplication.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MemberAccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: MemberAccountEntity)

    @Query("SELECT * FROM member_accounts WHERE userId = :userId LIMIT 1")
    suspend fun getByUserId(userId: Int): MemberAccountEntity?

    @Query("UPDATE member_accounts SET savingsBalance = savingsBalance + :amount WHERE userId = :userId")
    suspend fun addToSavingsBalance(userId: Int, amount: Double)
}