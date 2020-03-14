package com.example.feature_converter.network

import com.example.feature_converter.domain.entity.Rate
import java.util.*

internal data class ServiceResponse(
    val rates: List<Rate>,
    val date: Date?
)