package com.example.expensekar

import android.app.Application
import androidx.room3.Room
import com.example.expensekar.data.database.ExpenseDatabase

class ExpenseKarApplication: Application(){
    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            ExpenseDatabase::class.java,
            "expensekar_database"
        ).build()
    }
}