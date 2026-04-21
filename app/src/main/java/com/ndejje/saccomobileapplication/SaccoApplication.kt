package com.ndejje.saccomobileapplication

import android.app.Application
import com.ndejje.saccomobileapplication.model.AppDatabase
import com.ndejje.saccomobileapplication.model.SaccoRepository

class SaccoApplication : Application() {

    val database by lazy { AppDatabase.getInstance(this) }

    val repository by lazy {
        SaccoRepository(
            userDao          = database.userDao(),
            memberAccountDao = database.memberAccountDao(),
            transactionDao   = database.transactionDao(),
            loanRequestDao   = database.loanRequestDao()
        )
    }
}