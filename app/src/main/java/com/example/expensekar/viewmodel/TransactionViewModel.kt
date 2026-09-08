package com.example.expensekar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensekar.data.model.Transaction
import com.example.expensekar.data.repository.TransactionRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import com.example.expensekar.data.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.Calendar

class TransactionViewModel(
    private val repository: TransactionRepository,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val transactions: StateFlow<List<Transaction>> =
        repository.transactions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalRevenue: StateFlow<Double> = transactions.map { list ->
        list.filter { it.type == "Income" }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalExpense: StateFlow<Double> = transactions.map { list ->
        list.filter { it.type == "Expense" }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val monthlyNetChange: StateFlow<Double> = transactions.map { list ->
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        val monthlyTransactions = list.filter {
            val transCal = Calendar.getInstance().apply { timeInMillis = it.date }
            transCal.get(Calendar.MONTH) == currentMonth && transCal.get(Calendar.YEAR) == currentYear
        }

        val rev = monthlyTransactions.filter { it.type == "Income" }.sumOf { it.amount }
        val exp = monthlyTransactions.filter { it.type == "Expense" }.sumOf { it.amount }
        rev - exp
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    private val _initialBalance = MutableStateFlow(preferenceManager.getInitialBalance())
    val initialBalance: StateFlow<Double> = _initialBalance.asStateFlow()

    val currentBalance: StateFlow<Double> = combine(_initialBalance, totalRevenue, totalExpense) { initial, rev, exp ->
        initial + rev - exp
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _initialBalance.value)

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.addTransaction(transaction)
        }
    }

    fun saveInitialBalance(balance: Double) {
        preferenceManager.saveInitialBalance(balance)
        _initialBalance.value = balance
    }

    fun isBalanceSet(): Boolean = preferenceManager.isBalanceSet()
}
