package it.yoox.yooxrobe.components

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import io.realm.Realm
import io.realm.RealmConfiguration
import it.yoox.yooxrobe.constants.GlobalConstants.TAG
import it.yoox.yooxrobe.models.DbMigrations


class CustomApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()


        /*Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .migration(DbMigrations())
                .schemaVersion(1)
                .build())*/
    }
}
