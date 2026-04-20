package com.ndejje.saccomobileapplication

import android.app.Application
import com.ndejje.saccomobileapplication.model.AppDatabase
import com.ndejje.saccomobileapplication.model.SaccoRepository

class SaccoApplication : Application() {
    // Using lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val repository: SaccoRepository by lazy {
        SaccoRepository(
            database.userDao(),
            database.memberAccountDao(),
            database.transactionDao(),
            database.loanRequestDao()
        )
    }
}
