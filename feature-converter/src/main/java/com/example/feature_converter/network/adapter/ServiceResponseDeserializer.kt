package com.example.feature_converter.network.adapter

import com.example.feature_converter.domain.entity.Currency
import com.example.feature_converter.domain.entity.Rate
import com.example.feature_converter.network.ServiceResponse
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

internal class ServiceResponseDeserializer : JsonDeserializer<ServiceResponse> {
    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ServiceResponse {
        val date = formatter.parse(json.asJsonObject.get("date").asString)
        val currencies = mutableListOf<Rate>().apply {
            Currency.values().forEach { code ->
                val value = json.asJsonObject
                    .get("rates").asJsonObject
                    .get(code.name)?.asBigDecimal

                value?.let { add(Rate(code, value)) }
            }
        }
        return ServiceResponse(currencies, date)
    }
}