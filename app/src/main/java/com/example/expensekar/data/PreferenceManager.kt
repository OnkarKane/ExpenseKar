package com.example.expensekar.data

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("expensekar_prefs", Context.MODE_PRIVATE)

    fun saveInitialBalance(balance: Double) {
        sharedPreferences.edit().putFloat("initial_balance", balance.toFloat()).apply()
        sharedPreferences.edit().putBoolean("is_balance_set", true).apply()
    }

    fun getInitialBalance(): Double {
        return sharedPreferences.getFloat("initial_balance", 0f).toDouble()
    }

    fun isBalanceSet(): Boolean {
        return sharedPreferences.getBoolean("is_balance_set", false)
    }
}
