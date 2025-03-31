package com.mobile.group.tlucontact.services

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitCommunication {
    companion object {
        private val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        private val objectMapper = ObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // Bỏ qua field không xác định
            configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) // Chấp nhận "" thành null
            configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true) // Chấp nhận array có 1 phần tử
        }

        fun <T> build(classType: Class<T>, urlKey: String): T {
//            val remoteConfig = FirebaseRemoteConfig.getInstance()
//            val apiUrl = remoteConfig.getString(urlKey)
//            remoteConfig.getBoolean("feature_x_enabled")

            return Retrofit.Builder()
//                .baseUrl(apiUrl)
                .baseUrl("https://tlu-contact-1-0-0.onrender.com/")
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(buildCommunication())
                .build()
                .create(classType)
        }

        private fun buildCommunication() : OkHttpClient {
            return OkHttpClient.Builder()
                .addInterceptor(logger)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()
        }
    }
}