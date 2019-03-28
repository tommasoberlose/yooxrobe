package it.fancypixel.boilerplate.network

import retrofit2.http.*
import io.reactivex.Observable
import it.fancypixel.boilerplate.constants.NetworkConstants
import retrofit2.Response
import kotlin.collections.HashMap

interface APIService {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST(NetworkConstants.LOGIN)
    fun login(@Body body: HashMap<String, String>): Observable<Response<HashMap<String, String>>>
}
