package com.pairlix.dating.di


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.pairlix.dating.LanguageManager.AppLanguageManager
import com.pairlix.dating.network.ApiServices
import com.pairlix.dating.network.LanguageInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkingModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }


    @Provides
    @Singleton
    fun provideLanguageInterceptor(): LanguageInterceptor {
        return LanguageInterceptor {
            AppLanguageManager.currentLanguage // "en" or "ar"
        }
    }
    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        languageInterceptor: LanguageInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            // ⭐ language header automatically
            .addInterceptor(languageInterceptor)
            // logging
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://43.205.51.214:3000")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }
}
