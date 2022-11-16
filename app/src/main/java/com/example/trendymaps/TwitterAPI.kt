package com.example.trendymaps

import retrofit2.http.*

interface TwitterAPI {
    @GET("/1.1/trends/closest.json")
    suspend fun getWOEID(
        @Query("lat") latitude: Double,
        @Query("long") longitude: Double,
        @Header("Authorization") auth: String
    ): ArrayList<WOEID>

    @GET("/1.1/trends/place.json")
    suspend fun getTopics(
        @Query("id") woeid: Int,
        @Header("Authorization") auth: String
    ): ArrayList<Trends>
}