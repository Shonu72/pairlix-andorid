package com.pairlix.dating.helper

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.dp
import com.pairlix.dating.R
import com.pairlix.dating.databinding.ErrorLayBinding
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/*object ErrorUtil {
    fun handlerGeneralError(context: Context, throwable: Throwable) {
        throwable.printStackTrace()
        when (throwable) {
            is ConnectException -> ErrorUtil.showErrorDialog(context,
                context.getString(R.string.internal_server_error))
            is SocketTimeoutException -> ErrorUtil.showErrorDialog(context,
                context.getString(R.string.no_internet_connection))
            is UnknownHostException -> ErrorUtil.showErrorDialog(context,   context.getString(R.string.no_internet_connection))
            is InternalError -> ErrorUtil.showErrorDialog(context, context.getString(R.string.internal_server_error))
            is HttpException -> {
                val errorMessage = parseApiError(throwable.response()?.errorBody())
                    if (!errorMessage.isNullOrEmpty()) {
                        ErrorUtil.showErrorDialog(context,errorMessage)
                        //showToast(context, errorMessage.toString())
                    }
            }
            else -> {
                ErrorUtil.showErrorDialog(context, context.getString(R.string.something_went_wrong))
            }
        }
    }

    fun ErrorUtil.showErrorDialog(context: Context, msg: String) {
        val dialogBinding = ErrorLayBinding.inflate(LayoutInflater.from(context))
        val background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 24 * context.resources.displayMetrics.density
        }
        dialogBinding.root.background = background

        val dialog = AlertDialog.Builder(context)
            .setView(dialogBinding.root)
            .create()

        dialogBinding.errorMessage.text = msg

        dialogBinding.okBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


}*/


object ErrorUtil {

    // ✅ Global Compose state
    var errorMessage: MutableState<String?> = mutableStateOf(null)

    fun handlerGeneralError(context: Context, throwable: Throwable) {
        throwable.printStackTrace()

        when (throwable) {
            is ConnectException -> ErrorUtil.showErrorDialog(
                context,
                context.getString(R.string.internal_server_error)
            )

            is SocketTimeoutException,
            is UnknownHostException -> ErrorUtil.showErrorDialog(
                context,
                context.getString(R.string.no_internet_connection)
            )

            is InternalError -> ErrorUtil.showErrorDialog(
                context,
                context.getString(R.string.internal_server_error)
            )

            is HttpException -> {
                val errorMessage = parseApiError(throwable.response()?.errorBody())
                if (!errorMessage.isNullOrEmpty()) {
                    ErrorUtil.showErrorDialog(context, errorMessage)
                }
            }

            else -> {
                ErrorUtil.showErrorDialog(
                    context,
                    context.getString(R.string.something_went_wrong)
                )
            }
        }
    }

    // ✅ SAME FUNCTION (no change anywhere else)
    fun ErrorUtil.showErrorDialog(context: Context, msg: String) {
        errorMessage.value = msg
    }

    fun clearError() {
        errorMessage.value = null
    }

}