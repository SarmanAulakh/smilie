package com.example.smilie.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smilie.screens.sign_up.SignUpState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

open class SmilieViewModel() : ViewModel() {
    var errorMessage = mutableStateOf("")
        private set
    open fun launchCatching(onError: () -> Unit = {}, block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                run {
                    errorMessage.value = throwable.message.orEmpty()
                    onError()
                }
            },
            block = block
        )
}