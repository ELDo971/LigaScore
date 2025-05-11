package com.example.ligascore

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("v4/competitions/PD/matches")
    suspend fun getLaLigaMatches(
        @Query("dateFrom") dateFrom: String,
        @Query("dateTo") dateTo: String,
        @Header("X-Auth-Token") apiKey: String
    ): Response<MatchResponse>
}
