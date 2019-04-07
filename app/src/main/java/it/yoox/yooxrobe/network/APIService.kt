package it.yoox.yooxrobe.network

import retrofit2.http.*
import io.reactivex.Observable
import io.reactivex.Single
import it.yoox.yooxrobe.constants.NetworkConstants
import okhttp3.MultipartBody
import retrofit2.Response
import kotlin.collections.HashMap

interface APIService {
    @Headers("Content-Type: multipart/form-data", "Accept: application/json")
    @Multipart
    @POST(NetworkConstants.UPLOAD)
    fun upload(@Part filePart: MultipartBody.Part): Single<Response<HashMap<String, Any>>>
}
