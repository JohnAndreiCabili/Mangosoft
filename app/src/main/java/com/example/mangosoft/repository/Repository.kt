package com.example.mangosoft.repository

import com.android.volley.Response
import com.example.mangosoft.api.RetrofitInstance
import com.example.mangosoft.model.RFRInput
import com.example.mangosoft.model.YOLOResponse
import okhttp3.MultipartBody

class Repository {
    suspend fun processRFR(input: RFRInput): retrofit2.Response<Array<String>> {
        return RetrofitInstance.rfrApi.predict(input)
    }

    // New method for CNN API
    suspend fun processCNN(imagePart: MultipartBody.Part): retrofit2.Response<YOLOResponse> {
        return RetrofitInstance.cnnApi.predict(imagePart)
    }
}