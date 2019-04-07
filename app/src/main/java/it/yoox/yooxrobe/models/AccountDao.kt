package it.yoox.yooxrobe.models

import android.content.Context
import io.realm.Realm
import io.realm.Sort
import it.yoox.yooxrobe.components.RealmLiveData

class AccountDao(val realm: Realm) {

    fun getUsersList(): RealmLiveData<Account> {
        return RealmLiveData(
            realm.where(Account::class.java).equalTo("deletedAt", 0.toLong()).sort(
                "startTime",
                Sort.DESCENDING
            ).findAllAsync()
        )
    }

    fun createUser(context: Context, name: String) : Int {
        val user = Account()
        realm.executeTransaction {

            // ID
            val currentIDNumber = it.where(Account::class.java).max("id")
            user.id = if (currentIDNumber == null) {
                1
            } else {
                currentIDNumber.toInt() + 1
            }

            // TODO("set name")
            it.insertOrUpdate(user)
        }
        return user.id
    }

    fun updateUser(user: Account?) {
        realm.executeTransactionAsync {realm ->
            user?.let { user -> realm.insertOrUpdate(user) }
        }
    }

}