package com.example.expensekar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.expensekar.data.repository.TransactionRepository

import com.example.expensekar.data.PreferenceManager

class TransactionViewModelFactory(
    private val repository: TransactionRepository,
    private val preferenceManager: PreferenceManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TransactionViewModel(repository, preferenceManager) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}