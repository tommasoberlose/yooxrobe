package it.yoox.yooxrobe.components

import androidx.lifecycle.LiveData
import io.realm.*

class RealmLiveObject<T : RealmObject>(private var result: T) : LiveData<T>() {

    private val listener = RealmObjectChangeListener<T> { result, _ -> value = result }

    override fun onActive() {
        result.addChangeListener(listener)
    }

    override fun onInactive() {
        result.removeChangeListener(listener)
    }
}