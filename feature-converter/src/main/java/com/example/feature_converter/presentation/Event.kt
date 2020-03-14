package com.example.feature_converter.presentation

import com.example.feature_converter.domain.entity.Currency
import java.math.BigDecimal

internal sealed class Event {
    data class SetSourceCurrencyValue(val value: BigDecimal) : Event()
    data class SetDestCurrencyValue(val value: BigDecimal) : Event()
    data class ShowSourceCurrencySelector(val codes: List<Currency>) : Event()
    data class ShowDestCurrencySelector(val codes: List<Currency>) : Event()
}