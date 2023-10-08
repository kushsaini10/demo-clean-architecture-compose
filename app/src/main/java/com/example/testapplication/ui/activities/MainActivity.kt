package com.example.testapplication.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.testapplication.ui.composables.App
import com.example.testapplication.viewmodels.AppViewModel
import java.lang.ref.WeakReference

class MainActivity : ComponentActivity() {

    private val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return  AppViewModel(WeakReference(application)) as T
        }
    }

    private val appViewModel: AppViewModel by lazy {
        ViewModelProvider(this, factory)[AppViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            appViewModel.getItems()
        }
        setContent {
            App(appViewModel)
        }
    }
}
