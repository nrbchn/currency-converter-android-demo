package com.example.feature_converter.data.repository

import android.content.Context
import com.example.feature_converter.network.ServiceResponse
import com.example.feature_converter.domain.entity.Rate
import com.example.feature_converter.domain.entity.Currency
import com.example.feature_converter.domain.repository.CurrencyRepository
import com.google.gson.Gson
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

internal class CurrencyRepositoryImpl(
    private val context: Context,
    private val gson: Gson
) : CurrencyRepository {
    companion object {
        private const val CURRENCY_STORAGE = "CURRENCY_STORAGE"
        private const val KEY_CURRENCIES = "KEY_CURRENCIES"
    }

    private val DEFAULT_DATA =
        ServiceResponse(
            rates = listOf(
                Rate(Currency.CAD, BigDecimal.valueOf(1.38113879)),
                Rate(Currency.HKD, BigDecimal.valueOf(7.7816725979)),
                Rate(Currency.ISK, BigDecimal.valueOf(132.2419928826)),
                Rate(Currency.PHP, BigDecimal.valueOf(51.2855871886)),
                Rate(Currency.DKK, BigDecimal.valueOf(6.6483096085)),
                Rate(Currency.HUF, BigDecimal.valueOf(301.0409252669)),
                Rate(Currency.CZK, BigDecimal.valueOf(23.3122775801)),
                Rate(Currency.GBP, BigDecimal.valueOf(0.7884608541)),
                Rate(Currency.RON, BigDecimal.valueOf(4.2894128114)),
                Rate(Currency.SEK, BigDecimal.valueOf(9.6926156584)),
                Rate(Currency.IDR, BigDecimal.valueOf(14620.9964412811)),
                Rate(Currency.INR, BigDecimal.valueOf(74.2597864769)),
                Rate(Currency.BRL, BigDecimal.valueOf(4.9004448399)),
                Rate(Currency.RUB, BigDecimal.valueOf(74.7583629893)),
                Rate(Currency.HRK, BigDecimal.valueOf(6.7615658363)),
                Rate(Currency.JPY, BigDecimal.valueOf(103.9501779359)),
                Rate(Currency.THB, BigDecimal.valueOf(31.6601423488)),
                Rate(Currency.CHF, BigDecimal.valueOf(0.9385231317)),
                Rate(Currency.EUR, BigDecimal.valueOf(0.8896797153)),
                Rate(Currency.MYR, BigDecimal.valueOf(4.265480427)),
                Rate(Currency.BGN, BigDecimal.valueOf(1.7400355872)),
                Rate(Currency.TRY, BigDecimal.valueOf(6.2598754448)),
                Rate(Currency.CNY, BigDecimal.valueOf(7.0175266904)),
                Rate(Currency.NOK, BigDecimal.valueOf(10.1140569395)),
                Rate(Currency.NZD, BigDecimal.valueOf(1.6168149466)),
                Rate(Currency.ZAR, BigDecimal.valueOf(16.4098754448)),
                Rate(Currency.USD, BigDecimal.valueOf(1.0)),
                Rate(Currency.MXN, BigDecimal.valueOf(22.0665480427)),
                Rate(Currency.SGD, BigDecimal.valueOf(1.4038256228)),
                Rate(Currency.AUD, BigDecimal.valueOf(1.5724199288)),
                Rate(Currency.ILS, BigDecimal.valueOf(3.6395907473)),
                Rate(Currency.KRW, BigDecimal.valueOf(1209.4306049822)),
                Rate(Currency.PLN, BigDecimal.valueOf(3.8789145907))
            ),
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse("2020-03-12")
        )

    private val sharedPreferences by lazy {
        context.getSharedPreferences(CURRENCY_STORAGE, Context.MODE_PRIVATE)
    }

    override fun getCurrenciesFromCache(): ServiceResponse {
        val jsonString = sharedPreferences.getString(KEY_CURRENCIES, null) ?: return DEFAULT_DATA
        return gson.fromJson(jsonString, ServiceResponse::class.java)
    }

    override fun saveCurrenciesInCache(rates: ServiceResponse) {
        val jsonString = gson.toJson(rates)
        sharedPreferences.edit().putString(KEY_CURRENCIES, jsonString).apply()
    }
}