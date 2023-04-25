package com.example.spender.ui.navigation.screens.helperfunctions

import androidx.compose.runtime.State
import com.example.spender.data.DataResult

inline fun <reified T> viewModelResultHandler(
    result: State<DataResult<T>?>,
    onSuccess: (data: T) -> Unit = {},
    onError: (error: String) -> Unit = {},
) {
    if (result.value == null) {
        return
    }
    if (result.value!! is DataResult.Error) {
        onError.invoke((result.value!! as DataResult.Error).exception)
        return
    }
    onSuccess.invoke((result.value!! as DataResult.Success<T>).data)
}
