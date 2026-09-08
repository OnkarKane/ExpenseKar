# Cleanup and Enhancements for ExpenseKar

This plan addresses two main areas: adding a splash screen experience and improving the transactions UI with a detail view.

## Proposed Changes

### 1. App Launch Experience (Splash Screen)

I will implement a simple splash screen logic in `MainActivity` to show the "Welcome to ExpenseKar" message for a few seconds before navigating to the main content.

#### [MODIFY] [MainActivity.kt](file:///E:/ExpenseKar/app/src/main/java/com/example/expensekar/MainActivity.kt)
- Add a `showSplash` state using `remember { mutableStateOf(true) }`.
- Use `LaunchedEffect` to wait for 2 seconds and then set `showSplash = false`.
- Conditional rendering to show `ScreenTitle()` during splash and `HomeScreen()` afterwards.

### 2. Transactions UI Improvement

I will enhance the transaction list items and add a detail view.

#### [NEW] [TransactionItem.kt](file:///E:/ExpenseKar/app/src/main/java/com/example/expensekar/ui/screens/TransactionItem.kt)
- Create a new composable for a single transaction row.
- Use a `Card` layout with proper padding and elevation.
- Display category, date, and amount with color-coding (Red for Expense, Green for Income).

#### [NEW] [TransactionDetailScreen.kt](file:///E:/ExpenseKar/app/src/main/java/com/example/expensekar/ui/screens/TransactionDetailScreen.kt)
- Create a new screen to show all details of a transaction (Amount, Category, Date, Method, Note).
- Add a back button to return to the list.

#### [MODIFY] [HomeScreen.kt](file:///E:/ExpenseKar/app/src/main/java/com/example/expensekar/ui/screens/HomeScreen.kt)
- Update `LazyColumn` to use the new `TransactionItem`.
- Implement navigation logic to show `TransactionDetailScreen` when an item is clicked.

## Verification Plan

### Automated Tests
- I will verify that the app builds successfully using `./gradlew assembleDebug`.

### Manual Verification
- Launch the app and observe the "Welcome to ExpenseKar" splash screen for 2 seconds.
- Verify that the transaction list looks better with cards and color-coding.
- Click on a transaction and verify that the detail screen opens with correct information.
- Use the back button on the detail screen to return to the list.
