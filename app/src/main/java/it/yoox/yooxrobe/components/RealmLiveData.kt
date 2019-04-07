package it.yoox.yooxrobe.components

import io.realm.RealmResults
import io.realm.RealmChangeListener
import androidx.lifecycle.LiveData
import io.realm.RealmModel

class RealmLiveData<T : RealmModel>(private var results: RealmResults<T>) : LiveData<RealmResults<T>>() {

    private val listener = RealmChangeListener<RealmResults<T>> { results -> value = results }
    fun RealmLiveData(realmResults: RealmResults<T>) {
        results = realmResults
    }
    override fun onActive() {
        results.addChangeListener(listener)
    }

    override fun onInactive() {
        results.removeChangeListener(listener)
    }
}