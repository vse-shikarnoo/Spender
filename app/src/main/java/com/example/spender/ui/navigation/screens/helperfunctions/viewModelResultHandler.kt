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
) {
    if (result.value == null) { return }
    if (result.value is DataResult.Error){
        val resultError = (result.value as DataResult.Error).exception
        if (Show.showError(resultError)) {
            Toast.makeText(
                context,
                resultError,
                Toast.LENGTH_SHORT
            ).show()
        }
        onError.invoke()
        return
    }
    val resultSuccess = (result.value as DataResult.Success).data
    if (resultSuccess !is String) {
        onSuccess.invoke(resultSuccess)
        return
    }
    if (Show.showSuccess(resultSuccess as String)) {
        Toast.makeText(
            context,
            resultSuccess as String,
            Toast.LENGTH_SHORT
        ).show()
        onSuccess.invoke(resultSuccess)
    }
}

object Show {
    var oldError = ""
    var oldSuccess = ""
    fun showError(newError: String): Boolean {
        val result = oldError != newError
        oldError = newError
        return result
    }
    fun showSuccess(newSuccess: String): Boolean {
        val result = oldSuccess != newSuccess
        oldSuccess = newSuccess
        return result
    }
}