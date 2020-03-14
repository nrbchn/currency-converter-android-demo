package com.example.feature_converter.di

import android.content.Context
import com.example.feature_converter.data.ConverterApiImpl
import com.example.feature_converter.data.repository.CurrencyRepositoryImpl
import com.example.feature_converter.di.annotation.ConverterScope
import com.example.feature_converter.domain.ConverterApi
import com.example.feature_converter.network.ServiceResponse
import com.example.feature_converter.domain.repository.CurrencyRepository
import com.example.feature_converter.network.CurrencyService
import com.example.feature_converter.network.adapter.ServiceResponseDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
internal class ConverterModule {
    private val gson = GsonBuilder()
        .registerTypeAdapter(ServiceResponse::class.java, ServiceResponseDeserializer())
        .create()

    @ConverterScope
    @Provides
    fun provideCurrencyService(): CurrencyService =
        Retrofit.Builder()
            .baseUrl("https://api.exchangeratesapi.io")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(CurrencyService::class.java)

    @ConverterScope
    @Provides
    fun provideCurrencyRepository(context: Context, gson: Gson): CurrencyRepository =
        CurrencyRepositoryImpl(context, gson)

    @ConverterScope
    @Provides
    fun provideConverterApi(
        repository: CurrencyRepository,
        service: CurrencyService
    ): ConverterApi = ConverterApiImpl(repository, service)
}