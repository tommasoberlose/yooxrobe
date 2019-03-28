package it.fancypixel.boilerplate.util

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson
import it.fancypixel.boilerplate.R
import it.fancypixel.boilerplate.components.Errors
import okhttp3.ResponseBody

object NetworkUtil {
    fun handleNetworkError(context: Context, result: ResponseBody?) {
        try {
            val errors = Gson().fromJson<Errors>(result!!.string(), Errors::class.java)
            var e = ""
            for (error in errors.errors) {
                e = "$e$error, "
            }
            if (!e.isBlank()) {
                e = e.substring(0, e.length - 2)
            }
            Toast.makeText(context, e, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(context, context.getString(R.string.server_error_message), Toast.LENGTH_SHORT).show()
        }
    }
}