package com.hn_2452.shoes_nike.utility

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.hn_2452.shoes_nike.data.NetworkResult
import com.hn_2452.shoes_nike.ui.cart.CartFragment

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T? = null, message: String): Resource<T> =
            Resource(status = Status.ERROR, data = data, message = message)

        fun <T> loading(data: T? = null): Resource<T> =
            Resource(status = Status.LOADING, data = data, message = null)
    }
}

enum class Status {
    ERROR,
    SUCCESS,
    LOADING
}

const val TAG = "Nike:ResourceUtil: "
fun <T> handleResource(
    data: LiveData<Resource<T>>,
    lifecycleOwner: LifecycleOwner,
    onSuccess: (T?) -> Unit = {},
    onLoading: () -> Unit = {},
    onError: (String?) -> Unit = {},
    isErrorInform: Boolean = false,
    context: Context,
) {
    data.observe(lifecycleOwner) { result ->
        when (result.status) {
            Status.LOADING -> {
                Log.i(TAG, "handleResource: loading...")
                onLoading()
            }

            Status.SUCCESS -> {
                Log.i(TAG, "handleResource: success ${result.data}")
                onSuccess(result.data)
            }

            Status.ERROR -> {
                Log.e(TAG, "handleResult: error ${result.message}")
                onError(result.message)
                if (isErrorInform) {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

fun <T> parseResult(res: NetworkResult<T>) : Resource<T> {
    return if(res.success) {
        Resource.success(res.data)
    } else {
        Resource.error(message = res.message)
    }
}
