package com.example.feature_converter

import com.example.feature_converter.data.ConverterApiImpl
import com.example.feature_converter.domain.entity.Currency
import com.example.feature_converter.domain.entity.Rate
import com.example.feature_converter.domain.repository.CurrencyRepository
import com.example.feature_converter.network.CurrencyService
import com.example.feature_converter.network.ServiceResponse
import io.mockk.every
import io.mockk.mockkClass
import org.junit.Assert
import org.junit.Test
import java.math.BigDecimal

class ConverterApiImplUnitTest {
    private val rates = listOf<Rate>(
        Rate(Currency.USD, BigDecimal.valueOf(1.0)),
        Rate(Currency.RUB, BigDecimal.valueOf(12.3456789))
    )

    private val repository = mockkClass(CurrencyRepository::class) {
        every { getCurrenciesFromCache() } returns ServiceResponse(rates, null)
    }

    private val service = mockkClass(CurrencyService::class)

    private val api = ConverterApiImpl(repository, service)

    @Test
    fun correctConvert() {
        val value = api.convert(rates[0].currency, rates[1].currency, rates[0].value)
        Assert.assertTrue(value.toString() == "12.35")
    }
}