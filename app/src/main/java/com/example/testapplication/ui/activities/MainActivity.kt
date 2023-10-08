package com.example.testapplication.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testapplication.ui.composables.ListUi
import com.example.testapplication.viewmodels.HomeViewModel
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {

    private val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return  HomeViewModel(WeakReference(application)) as T
        }
    }

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.getItems()

//        CoroutineScope(Dispatchers.Main).launch {
//            viewModel.uiState.collect {
//                Log.d("KUSH", "state")
//            }
//        }
        setContent {
            ListUi(homeViewModel)
        }
    }
}
