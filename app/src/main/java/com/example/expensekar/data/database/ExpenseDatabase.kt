package com.example.expensekar.data.database
import androidx.room3.Database
import androidx.room3.RoomDatabase
import com.example.expensekar.data.model.Transaction

@Database(
    entities = [Transaction::class],
    version = 1
)
abstract class ExpenseDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}