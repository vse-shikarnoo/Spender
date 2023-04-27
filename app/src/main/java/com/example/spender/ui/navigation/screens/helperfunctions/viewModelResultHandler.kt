package com.example.spender.ui.navigation.screens.helperfunctions

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.State
import com.example.spender.data.DataResult

inline fun <reified T> viewModelResultHandler(
    context: Context,
    result: State<DataResult<T>?>,
    onSuccess: (data: T) -> Unit = {},
    onError: () -> Unit = {},
    onComplete: () -> Unit = {},
    msgShow: Boolean = false
) {
    if (result.value == null) {
        return
    }
    if (result.value is DataResult.Error) {
        val resultError = (result.value as DataResult.Error).exception
        if (msgShow) {
            Toast.makeText(context, resultError, Toast.LENGTH_SHORT).show()
        }
        onError.invoke()
        onComplete.invoke()
        return
    }
    val resultSuccess = (result.value as DataResult.Success).data
    if (resultSuccess !is String) {
        onSuccess.invoke(resultSuccess)
        onComplete.invoke()
        return
    }
    if (msgShow) {
        Toast.makeText(context, resultSuccess, Toast.LENGTH_SHORT).show()
    }
    onSuccess.invoke(resultSuccess)
    onComplete.invoke()
}
