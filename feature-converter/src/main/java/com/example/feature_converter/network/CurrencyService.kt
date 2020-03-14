package com.example.feature_converter.network

import io.reactivex.Single
import retrofit2.http.GET

internal interface CurrencyService {
    @GET("latest?base=USD")
    fun fetchRates(): Single<ServiceResponse>
}