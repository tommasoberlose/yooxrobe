package it.yoox.yooxrobe.network

import retrofit2.http.*
import io.reactivex.Observable
import io.reactivex.Single
import it.yoox.yooxrobe.constants.NetworkConstants
import retrofit2.Response
import kotlin.collections.HashMap

interface APIService {
    @Headers("Content-Type: multipart/form-data", "Accept: application/json")
    @POST(NetworkConstants.UPLOAD)
    fun upload(@Body body: HashMap<String, Any>): Single<Response<HashMap<String, Any>>>
}
