package it.fancypixel.boilerplate.components

import android.util.Log
import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import io.realm.Realm
import io.realm.RealmConfiguration
import it.fancypixel.boilerplate.constants.GlobalConstants.TAG
import it.fancypixel.boilerplate.models.DbMigrations


class CustomApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            Log.d(TAG, "TOKEN: " + it.result?.token)
        }


        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .migration(DbMigrations())
                .schemaVersion(1)
                .build())
    }
}
