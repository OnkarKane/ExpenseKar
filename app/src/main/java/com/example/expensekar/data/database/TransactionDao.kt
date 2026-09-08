package com.example.expensekar.data.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import com.example.expensekar.data.model.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("Select * from transactions")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Insert
    suspend fun insertTransaction(transaction: Transaction)
}