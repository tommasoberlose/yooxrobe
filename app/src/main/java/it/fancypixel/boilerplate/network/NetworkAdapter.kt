package it.fancypixel.boilerplate.network

import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Loggable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.net.HttpURLConnection
import io.reactivex.Observable
import it.fancypixel.boilerplate.BuildConfig
import it.fancypixel.boilerplate.session.Profile
import retrofit2.Response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import kotlin.collections.HashMap

class NetworkAdapter(private val context: Context) {

    private val apiService: APIService

    init {

        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()

            val response = chain.proceed(request)
            if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                Profile(context).logoutUser()
            }
            response
        }.addInterceptor(CurlInterceptor(Loggable { message -> Log.d("Ok2Curl", message) })).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.URL_BASE_API)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()


        apiService = retrofit.create(APIService::class.java)
    }

    fun login(email: String, password: String): Observable<Response<HashMap<String, String>>> {
        return apiService.login(HashMap<String, String>().apply {
            this["email"] = email
            this["password"] = password
        })
    }
}
