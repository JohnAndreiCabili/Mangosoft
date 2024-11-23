package com.example.mangosoft.api

import com.example.mangosoft.model.RFRInput
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RFRApi {
    @POST("predict")
    suspend fun predict(
        @Body request: RFRInput
    ): Response<Array<String>>
}