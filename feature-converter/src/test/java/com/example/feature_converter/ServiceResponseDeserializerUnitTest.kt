package com.example.feature_converter

import com.example.feature_converter.network.ServiceResponse
import com.example.feature_converter.network.adapter.ServiceResponseDeserializer
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ServiceResponseDeserializerUnitTest {
    private val gson = GsonBuilder()
        .registerTypeAdapter(ServiceResponse::class.java, ServiceResponseDeserializer())
        .create()

    private val json =
        "{\"rates\":{\"CAD\":1.3858969741,\"HKD\":7.7679214697,\"ISK\":135.0864553314,\"PHP\":50.8402377522,\"DKK\":6.7301873199,\"HUF\":305.1873198847,\"CZK\":23.4528097983,\"GBP\":0.8021433718,\"RON\":4.3419488473,\"SEK\":9.7670208934,\"IDR\":14770.0018011527,\"INR\":73.7360410663,\"BRL\":4.6867795389,\"RUB\":72.7111851585,\"HRK\":6.8110590778,\"JPY\":107.2676512968,\"THB\":31.7399135447,\"CHF\":0.9553314121,\"EUR\":0.9005763689,\"MYR\":4.2784582133,\"BGN\":1.7613472622,\"TRY\":6.2905259366,\"CNY\":6.9873018732,\"NOK\":9.9933357349,\"NZD\":1.6318443804,\"ZAR\":16.1414805476,\"USD\":1.0,\"MXN\":21.4188580692,\"SGD\":1.4124639769,\"AUD\":1.5925792507,\"ILS\":3.6803854467,\"KRW\":1208.015129683,\"PLN\":3.9238112392},\"base\":\"USD\",\"date\":\"2020-03-13\"}"

    @Test
    fun correctDeserialization() {
        try {
            assertTrue(gson.fromJson(json, ServiceResponse::class.java) != null)
        } catch (e: JsonSyntaxException) {
            assertFalse(true)
        }
    }
}
