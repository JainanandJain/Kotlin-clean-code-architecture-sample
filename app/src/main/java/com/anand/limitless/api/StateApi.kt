package com.anand.limitless.api

import android.util.Log
import com.anand.limitless.vo.StateName
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * API communication setup
 */
interface StateApi {

    @GET("v2/all")
    fun getTop(
            @Query("limit") limit: Int): Call<List<StateName>>

    class ListingResponse(val data: ListingData)

    class ListingData(
            val children: List<RedditChildrenResponse>,
            val after: String?,
            val before: String?
    )

    data class RedditChildrenResponse(val data: StateName)

    companion object {
        private const val BASE_URL = "https://restcountries.eu/rest/"
        fun create(): StateApi = create(HttpUrl.parse(BASE_URL)!!)
        fun create(httpUrl: HttpUrl): StateApi {
            val logger = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                Log.d("API", it)
            })
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(httpUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(StateApi::class.java)
        }
    }
}