package com.pairlix.dating.network


import okhttp3.Interceptor
import okhttp3.Response

class LanguageInterceptor(
    private val languageProvider: () -> String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()

        val newRequest = request.newBuilder()
            .header("language", languageProvider())
            .build()

        return chain.proceed(newRequest)
    }
}