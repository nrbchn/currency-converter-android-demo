package com.example.feature_converter.domain

import com.example.feature_converter.domain.entity.Currency
import com.example.feature_converter.domain.entity.Rate
import java.math.BigDecimal
import java.util.*

interface ConverterApi {
    val actualDate: Date
    val rates: List<Rate>
    fun update(onSuccess: () -> Unit, onFailure: (Throwable) -> Unit)
    fun dispose()
    fun convert(
        source: Currency,
        dest: Currency,
        value: BigDecimal
    ): BigDecimal
}