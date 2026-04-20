package com.ndejje.saccomobileapplication

import android.app.Application
import com.ndejje.saccomobileapplication.model.AppDatabase
import com.ndejje.saccomobileapplication.model.SaccoRepository

class SaccoApplication : Application() {
        SaccoRepository(
        )
    }
}