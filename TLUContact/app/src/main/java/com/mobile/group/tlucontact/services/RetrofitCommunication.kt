package com.mobile.group.tlucontact.services

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitCommunication {
    companion object {
        private val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        fun <T> build(classType: Class<T>, urlKey: String): T {
            val remoteConfig = FirebaseRemoteConfig.getInstance()
            val apiUrl = remoteConfig.getString(urlKey)
            remoteConfig.getBoolean("feature_x_enabled")

            return Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(JacksonConverterFactory.create())
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