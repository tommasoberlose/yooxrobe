package it.yoox.yooxrobe.session

import android.accounts.Account
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import it.yoox.yooxrobe.components.events.MessageEvent
import it.yoox.yooxrobe.constants.MessageEventCode
import org.greenrobot.eventbus.EventBus

class Profile internal constructor(private val mContext: Context) {

    private val SP: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)

    val token: String
        get() = SP.getString(KEY_TOKEN, "")!!

    val account: Account
        get() = Gson().fromJson(SP.getString(KEY_ACCOUNT, ""), Account::class.java)

    @SuppressLint("ApplySharedPref")
    private fun resetData() {
        SP.edit().clear().commit()
    }

    fun logoutUser() {
        resetData()
        EventBus.getDefault().post(MessageEvent(MessageEventCode.LOGGED_OUT))
    }

    fun isUserLogged(): Boolean {
        return token.isNotBlank()
    }

    class Editor @SuppressLint("CommitPrefEdits")
    constructor(context: Context) {
        private val editor: SharedPreferences.Editor = PreferenceManager.getDefaultSharedPreferences(context).edit()

        fun setToken(token: String): Editor {
            editor.putString(KEY_TOKEN, token)
            return this
        }

        fun saveAccountJson(account: Account): Editor {
            editor.putString(KEY_ACCOUNT, Gson().toJson(account))
            return this
        }

        fun apply() {
            editor.apply()
        }

        fun commit() {
            editor.commit()
        }
    }

    companion object {
        private val KEY_TOKEN = "KEY_TOKEN"
        private val KEY_ACCOUNT = "KEY_ACCOUNT"
    }
}
