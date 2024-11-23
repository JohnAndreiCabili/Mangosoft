package com.example.mangosoft.api
import com.example.mangosoft.utils.Constants.Companion.BASE_URL_RFR
import com.example.mangosoft.utils.Constants.Companion.BASE_URL_YOLO
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val gson = GsonBuilder()
        .setLenient()
        .setPrettyPrinting()
        .create()

    private val retrofit_cnn by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_YOLO)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private val retrofit_rfr by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_RFR)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val cnnApi: CNNApi by lazy {
        retrofit_cnn.create(CNNApi::class.java)
    }

    val rfrApi: RFRApi by lazy {
        retrofit_rfr.create(RFRApi::class.java)
    }
}