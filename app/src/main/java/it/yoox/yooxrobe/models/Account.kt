package it.yoox.yooxrobe.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Account : RealmObject() {
    @PrimaryKey
    var id: Int = -1

    var deletedAt: Long = 0
}