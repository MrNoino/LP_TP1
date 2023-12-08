package com.lp.tp1.backend.requests

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

object RetroBuilder {

    object RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            println("Outgoing request to ${request.url}")
            return chain.proceed(request)
        }
    }
}

fun getSwitchboardRetrofit(link:String): RetrofitRequests {
    val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(RetroBuilder.RequestInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(link)
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okHttpClient)
        .build()


    return retrofit.create(RetrofitRequests::class.java)
}

interface RetrofitRequests {
    @Headers("Content-Type: application/json; charset=utf-8")
    @GET(".")
    fun getSwitchboard(): Call<String>
}