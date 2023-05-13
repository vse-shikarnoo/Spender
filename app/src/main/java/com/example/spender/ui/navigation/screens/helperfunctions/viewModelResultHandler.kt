package com.example.spender.ui.navigation.screens.helperfunctions

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import com.example.spender.data.DataResult

inline fun <reified T> viewModelResultHandler(
    context: Context,
    result: State<DataResult<T>?>,
    onSuccess: (data: T) -> Unit = {},
    onError: () -> Unit = {},
    restMsgShowState: () -> Unit = {},
    msgShow: Boolean = false
) {
    result.value?.let { resultValue ->
        restMsgShowState.invoke()
        when (resultValue) {
            is DataResult.Success -> {
                val data = resultValue.data
                if (data is String && msgShow) {
                    Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
                    Log.d("ResultSuccess", data)
                }
                onSuccess.invoke(data)
            }

            is DataResult.Error -> {
                val error = resultValue.exception
                if (msgShow) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                    Log.d("ResultError", error)
                }
                onError.invoke()
            }
        }
    }
}
