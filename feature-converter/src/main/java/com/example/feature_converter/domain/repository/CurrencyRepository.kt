package com.example.feature_converter.domain.repository

import com.example.feature_converter.network.ServiceResponse

internal interface CurrencyRepository {
    fun getCurrenciesFromCache(): ServiceResponse
    fun saveCurrenciesInCache(rates: ServiceResponse)
}