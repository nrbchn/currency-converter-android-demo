package com.example.feature_converter.presentation

import com.example.feature_converter.domain.entity.Currency
import java.util.*

internal data class ConverterViewData(
    val sourceCurrency: Currency,
    val destCurrency: Currency,
    val actualDate: Date,
    val isRefreshing: Boolean
)