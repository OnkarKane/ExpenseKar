package com.example.expensekar.data.repository

import com.example.expensekar.data.database.TransactionDao
import com.example.expensekar.data.model.Transaction
import kotlinx.coroutines.flow.Flow
class TransactionRepository(private val transactionDao: TransactionDao) {
    val transactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()

    suspend fun addTransaction(transaction: Transaction){
        transactionDao.insertTransaction(transaction)
    }
}