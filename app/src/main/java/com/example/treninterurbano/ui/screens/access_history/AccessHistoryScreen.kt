package com.example.treninterurbano.ui.screens.access_history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.treninterurbano.data.model.AccessHistory
import com.example.treninterurbano.ui.components.AccessHistoryItem
import com.example.treninterurbano.ui.components.LoadingIndicator
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessHistoryScreen(
    viewModel: AccessHistoryViewModel = hiltViewModel()
) {
    var accessHistory by remember { mutableStateOf<List<AccessHistory>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }
    
    LaunchedEffect(key1 = Unit) {
        loadHistory(viewModel) { history ->
            accessHistory = history ?: emptyList()
            isLoading = false
        }
    }
    
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de Accesos") }
            )
        }
    ) { padding ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = {
                isRefreshing = true
                loadHistory(viewModel) { history ->
                    accessHistory = history ?: emptyList()
                    isRefreshing = false
                }
            }
        ) {
            if (isLoading && !isRefreshing) {
                LoadingIndicator()
            } else {
                if (accessHistory.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No hay registros de accesos",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(accessHistory) { item ->
                            AccessHistoryItem(accessHistory = item)
                        }
                    }
                }
            }
        }
    }
}

private suspend fun loadHistory(
    viewModel: AccessHistoryViewModel,
    onResult: (List<AccessHistory>?) -> Unit
) {
    val result = viewModel.getAccessHistory()
    onResult(result.getOrNull())
}

