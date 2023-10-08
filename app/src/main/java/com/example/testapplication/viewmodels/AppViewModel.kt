package com.example.testapplication.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapplication.R
import com.example.testapplication.data.models.entities.Failure
import com.example.testapplication.data.models.entities.Item
import com.example.testapplication.data.models.entities.NoInternetException
import com.example.testapplication.data.models.entities.Success
import com.example.testapplication.network.Network
import com.example.testapplication.repository.Repository
import com.example.testapplication.ui.state.Error
import com.example.testapplication.ui.state.Loaded
import com.example.testapplication.ui.state.Loading
import com.example.testapplication.ui.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class AppViewModel(
    private val weakApp: WeakReference<Application>
) : AndroidViewModel(weakApp.get()!!) {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _itemDetailFlow = MutableStateFlow<Item?>(null)
    val itemDetailFlow = _itemDetailFlow.asStateFlow()

    private val repository by lazy {
        Repository(weakApp = weakApp, service = Network.service)
    }

    fun getItems() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.emit(Loading)

            when (val apiResponse = repository.getItems()) {
                is Success -> {
                    handleSuccess(apiResponse)
                }

                is Failure -> {
                    handleFailure(apiResponse)
                }
            }
        }
    }

    private suspend fun handleSuccess(apiResponse: Success<List<Item>>) {
        _uiState.emit(Loaded(apiResponse.data))
    }

    private suspend fun handleFailure(apiResponse: Failure<List<Item>>) {
        val message = when (apiResponse.error) {
            NoInternetException -> {
                R.string.no_internet
            }

            else -> {
                R.string.common_error
            }
        }
        _uiState.emit(
            Error(
                message = weakApp.get()?.resources?.getString(message)
            )
        )
    }

    fun getItem(itemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = repository.getItem(itemId)
            _itemDetailFlow.emit(item)
        }
    }

}