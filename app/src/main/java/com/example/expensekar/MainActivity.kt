package com.example.expensekar

import android.os.Bundle
import android.view.RoundedCorner
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensekar.ui.screens.HomeScreen
import com.example.expensekar.ui.screens.InitialBalanceScreen
import com.example.expensekar.ui.theme.ExpenseKarTheme
import com.example.expensekar.data.PreferenceManager
import com.example.expensekar.data.repository.TransactionRepository
import com.example.expensekar.viewmodel.TransactionViewModel
import com.example.expensekar.viewmodel.TransactionViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

import androidx.compose.ui.graphics.Brush
import com.example.expensekar.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseKarTheme(dynamicColor = false) {
                val context = androidx.compose.ui.platform.LocalContext.current
                val application = context.applicationContext as ExpenseKarApplication
                val preferenceManager = remember { PreferenceManager(context) }
                val repository = remember { TransactionRepository(application.database.transactionDao()) }
                val factory = remember { TransactionViewModelFactory(repository, preferenceManager) }
                val viewModel: TransactionViewModel = viewModel(factory = factory)

                var showSplash by remember { mutableStateOf(true) }
                var isBalanceSet by remember { mutableStateOf(preferenceManager.isBalanceSet()) }

                LaunchedEffect(Unit) {
                    delay(2000)
                    showSplash = false
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DarkBackground
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF1A1A2E),
                                        DarkBackground
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        when {
                            showSplash -> ScreenTitle()
                            !isBalanceSet -> {
                                InitialBalanceScreen(
                                    viewModel = viewModel,
                                    onBalanceSaved = { isBalanceSet = true }
                                )
                            }
                            else -> HomeScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScreenTitle(){
    Text(
        text = "Welcome to ExpenseKar",
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold,
        style = MaterialTheme.typography.titleLarge,
        color = Color.White
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun TitlePreview() {
    ExpenseKarTheme {
        ScreenTitle()
    }
}