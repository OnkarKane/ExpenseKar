package com.example.expensekar.ui.screens

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.expensekar.data.model.Transaction
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensekar.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.expensekar.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransaction(viewModel: TransactionViewModel, onTransactionAdded: () -> Unit) {
    var noteInput by remember { mutableStateOf("") }
    var amountInput by remember { mutableStateOf("") }
    var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
    var showDatePickerModal by remember { mutableStateOf(false) }
    val transactionTypes = listOf("Expense", "Income")
    val (selectedType, onTypeSelected) = remember { mutableStateOf(transactionTypes[0]) }
    val paymentMethods = listOf("UPI", "Cash", "Card")
    val (selectedMethod, onMethodSelected) = remember { mutableStateOf(paymentMethods[0]) }
    val categories = listOf("Food", "Rent", "Transport", "Miscellaneous")
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .imePadding()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Add Transaction",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Glass Container for Form
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(GlassWhite)
                    .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Type Selection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        transactionTypes.forEach { text ->
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .selectable(
                                        selected = text == selectedType,
                                        onClick = { onTypeSelected(text) },
                                        role = Role.RadioButton
                                    )
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (text == selectedType) AccentColor.copy(alpha = 0.2f) else Color.Transparent)
                                    .border(1.dp, if (text == selectedType) AccentColor else GlassBorder, RoundedCornerShape(12.dp))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                RadioButton(
                                    selected = (text == selectedType),
                                    onClick = null,
                                    colors = RadioButtonDefaults.colors(selectedColor = AccentColor, unselectedColor = Color.Gray)
                                )
                                Text(
                                    text = text,
                                    modifier = Modifier.padding(start = 4.dp),
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = amountInput,
                        label = { Text("Amount") },
                        onValueChange = { newValue -> amountInput = newValue },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = GlassBorder,
                            focusedLabelColor = AccentColor,
                            unfocusedLabelColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    HorizontalDivider(color = GlassBorder)

                    // Payment Methods
                    Text(text = "Payment Method", color = Color.Gray, fontSize = 14.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        paymentMethods.forEach { text ->
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .selectable(
                                        selected = text == selectedMethod,
                                        onClick = { onMethodSelected(text) },
                                        role = Role.RadioButton
                                    )
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (text == selectedMethod) GlassWhite else Color.Transparent)
                                    .border(1.dp, if (text == selectedMethod) AccentColor else GlassBorder, RoundedCornerShape(12.dp))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = text,
                                    color = if (text == selectedMethod) AccentColor else Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedCategory,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Category") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = AccentColor,
                                unfocusedBorderColor = GlassBorder,
                                focusedLabelColor = AccentColor,
                                unfocusedLabelColor = Color.Gray
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.background(CardBackground)
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    text = { Text(text = category, color = Color.White) },
                                    onClick = {
                                        selectedCategory = category
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = selectedDateMillis?.let { convertMillisToDate(it) } ?: "",
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Date") },
                        placeholder = { Text("DD/MM/YYYY") },
                        trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = "Select date", tint = AccentColor) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(selectedDateMillis) {
                                awaitEachGesture {
                                    awaitFirstDown(pass = PointerEventPass.Initial)
                                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                    if (upEvent != null) {
                                        showDatePickerModal = true
                                    }
                                }
                            },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = GlassBorder,
                            focusedLabelColor = AccentColor,
                            unfocusedLabelColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )

                    OutlinedTextField(
                        value = noteInput,
                        label = { Text("Note") },
                        onValueChange = { newValue -> noteInput = newValue },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = AccentColor,
                            unfocusedBorderColor = GlassBorder,
                            focusedLabelColor = AccentColor,
                            unfocusedLabelColor = Color.Gray
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (amountInput.isNotEmpty() && selectedDateMillis != null && selectedCategory.isNotEmpty()) {
                        val transaction = Transaction(
                            type = selectedType,
                            amount = amountInput.toDouble(),
                            method = selectedMethod,
                            category = selectedCategory,
                            date = selectedDateMillis!!,
                            note = noteInput
                        )
                        viewModel.addTransaction(transaction)
                        onTransactionAdded()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentColor)
            ) {
                Text("Save Transaction", color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Date Picker Dialog Modal
        if (showDatePickerModal) {
            DatePickerModal(
                onDateSelected = { dateMillis ->
                    selectedDateMillis = dateMillis
                },
                onDismiss = { showDatePickerModal = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        // Compose DatePicker outputs milliseconds in UTC midnight,
        // so we force UTC parsing to prevent local timezone offsets from shifting the day back/forward.
        timeZone = TimeZone.getTimeZone("IST")
    }
    return formatter.format(Date(millis))
}
