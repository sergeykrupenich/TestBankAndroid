package com.example.testbankapp.presentation.accounts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import com.example.testbankapp.domain.model.Account
import com.example.testbankapp.presentation.deposit.DepositBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountListScreen(
    viewModel: AccountsViewModel = koinViewModel(),
    onLogoutClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedAccountForDeposit by remember { mutableStateOf<Account?>(null) }
    var showCreateAccountDialog by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.errorMessage, uiState.successMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
        uiState.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Accounts") },
                actions = {
                    IconButton(onClick = { viewModel.fetchAccounts() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reload"
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.logout()
                            onLogoutClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = "Logout"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateAccountDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Open an account")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading && uiState.accounts.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.accounts, key = { it.accountNumber }) { account ->
                        AccountItemCard(
                            account = account,
                            onDepositClick = { selectedAccountForDeposit = account }
                        )
                    }
                }
            }
        }

        selectedAccountForDeposit?.let { account ->
            DepositBottomSheet(
                account = account,
                onDismiss = {
                    selectedAccountForDeposit = null
                },
                onConfirmDeposit = { amount ->
                    viewModel.depositFunds(account.accountNumber, amount)
                    selectedAccountForDeposit = null
                }
            )
        }

        if (showCreateAccountDialog) {
            CreateAccountDialog(
                onDismiss = { showCreateAccountDialog = false },
                onConfirm = { currency ->
                    viewModel.createAccount(currency)
                    showCreateAccountDialog = false
                }
            )
        }
    }
}

@Composable
fun AccountItemCard(
    account: Account,
    onDepositClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Account: ${account.accountNumber}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = account.currency.toString(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${account.balance} ${account.currency}",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onDepositClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Top up")
            }
        }
    }
}

@Composable
fun CreateAccountDialog(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val currencies = listOf("BYN", "USD", "EUR", "CYN")
    var selectedCurrency by remember { mutableStateOf(currencies.first()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Open a new account") },
        text = {
            Column {
                Text("Select a currency:")
                Spacer(modifier = Modifier.height(8.dp))
                currencies.forEach { currency ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = (currency == selectedCurrency),
                            onClick = { selectedCurrency = currency }
                        )
                        Text(text = currency)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(selectedCurrency) }) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
