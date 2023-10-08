package com.example.testapplication.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testapplication.viewmodels.AppViewModel

@Composable
fun DetailScreen(itemId: Int, appViewModel: AppViewModel = viewModel()) {
    val itemState = appViewModel.itemDetailFlow.collectAsState(initial = null)

    LaunchedEffect(key1 = itemId, block = {
        appViewModel.getItem(itemId)
    })

    itemState.value?.let {
        Surface {
        }
    } ?: kotlin.run {
        Surface {
            LoadingUi()
        }
    }
}

@Composable
private fun LoadingUi() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator()
    }
}
