package com.example.expensekar.data.model

import androidx.room3.Entity
import androidx.room3.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val type: String,
    val amount: Double,
    val method: String,
    val category: String,
    val date: Long,
    val note: String
)
