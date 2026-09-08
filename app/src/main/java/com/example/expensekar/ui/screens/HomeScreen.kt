package com.example.expensekar.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensekar.viewmodel.TransactionViewModel
import androidx.compose.foundation.lazy.items
import android.app.Application
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensekar.ExpenseKarApplication
import com.example.expensekar.data.repository.TransactionRepository
import com.example.expensekar.viewmodel.TransactionViewModelFactory
import com.example.expensekar.data.model.Transaction
import androidx.activity.compose.BackHandler

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.border
import com.example.expensekar.ui.theme.*

@Composable
fun HomeScreen(viewModel: TransactionViewModel) {
    val transactions by viewModel.transactions.collectAsState()
    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val currentBalance by viewModel.currentBalance.collectAsState()
    val monthlyChange by viewModel.monthlyNetChange.collectAsState()

    var showAddTransactionScreen by remember { mutableStateOf(false) }
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }

    if (showAddTransactionScreen) {
        BackHandler {
            showAddTransactionScreen = false
        }
        AddTransaction(
            viewModel = viewModel,
            onTransactionAdded = {
                showAddTransactionScreen = false
            }
        )
    } else if (selectedTransaction != null) {
        BackHandler {
            selectedTransaction = null
        }
        TransactionDetailScreen(
            transaction = selectedTransaction!!,
            onBack = { selectedTransaction = null }
        )
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Current Balance (Takes full width)
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(GlassWhite)
                    .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(text = "Current Balance", color = Color.LightGray, fontSize = 16.sp)
                    Text(
                        text = "₹ $currentBalance",
                        fontSize = 40.sp,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // 2. Row container for Revenue and Expense
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Revenue Box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp)
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(GlassWhite)
                        .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Revenue", color = Color.LightGray, fontSize = 14.sp)
                        Text(
                            text = "₹ $totalRevenue",
                            fontSize = 22.sp,
                            color = RevenueGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Expense Box
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp)
                        .padding(start = 8.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(GlassWhite)
                        .border(1.dp, GlassBorder, RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Expense", color = Color.LightGray, fontSize = 14.sp)
                        Text(
                            text = "₹ $totalExpense",
                            fontSize = 22.sp,
                            color = ExpenseRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Monthly Net Change
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (monthlyChange >= 0) RevenueGreen.copy(alpha = 0.1f) else ExpenseRed.copy(alpha = 0.1f))
                    .border(
                        1.dp,
                        if (monthlyChange >= 0) RevenueGreen.copy(alpha = 0.3f) else ExpenseRed.copy(alpha = 0.3f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Monthly Net Change", color = Color.White, fontSize = 16.sp)
                    Text(
                        text = "${if (monthlyChange >= 0) "PLUS +" else "MINUS -"} ₹${kotlin.math.abs(monthlyChange)}",
                        color = if (monthlyChange >= 0) RevenueGreen else ExpenseRed,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // 3. Transactions Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Recent Transactions",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(transactions) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            onClick = { selectedTransaction = transaction }
                        )
                    }
                }
            }
            
            FloatingActionButton(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp),
                containerColor = AccentColor,
                onClick = { showAddTransactionScreen = true }
            ) {
                Text(
                    text = "Add Transaction",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
