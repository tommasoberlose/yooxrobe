package it.yoox.yooxrobe.network

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import com.moczul.ok2curl.CurlInterceptor
import com.moczul.ok2curl.logger.Loggable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.net.HttpURLConnection
import io.reactivex.Observable
import io.reactivex.Single
import it.yoox.yooxrobe.BuildConfig
import it.yoox.yooxrobe.session.Profile
import retrofit2.Response
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import kotlin.collections.HashMap
import com.google.android.gms.common.util.IOUtils.toByteArray
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import com.here.sdk.analytics.a.e
import okhttp3.MultipartBody
import com.here.android.mpa.internal.f
import okhttp3.MediaType
import okhttp3.RequestBody




class NetworkAdapter(private val context: Context) {

    private val apiService: APIService

    init {
        val okHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
            val request = chain.request()
            val response = chain.proceed(request)
            if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
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

    fun upload(bitmap: Bitmap): Single<Response<HashMap<String, Any>>> {
        val file = File(context.cacheDir, "photo.jpeg")

        val fOut = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fOut)
        fOut.flush()
        fOut.close()

        val reqFile = RequestBody.create(MediaType.parse("image/jpeg"), file)
        val body = MultipartBody.Part.createFormData("image", "photo.jpeg", reqFile)

        return apiService.upload(body)
    }
}
