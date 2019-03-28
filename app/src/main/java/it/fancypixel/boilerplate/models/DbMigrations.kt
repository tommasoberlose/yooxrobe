package it.fancypixel.boilerplate.models

import io.realm.*


class DbMigrations : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        var oldVersion = oldVersion
        val schema = realm.schema

        if (oldVersion == 0L) {

            oldVersion++
        }
    }
}