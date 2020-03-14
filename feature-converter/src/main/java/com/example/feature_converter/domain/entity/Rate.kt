package com.example.feature_converter.domain.entity

import java.math.BigDecimal

data class Rate(
    val currency: Currency,
    val value: BigDecimal
)