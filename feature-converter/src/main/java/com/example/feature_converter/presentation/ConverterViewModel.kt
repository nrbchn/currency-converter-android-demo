package com.example.feature_converter.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.feature_converter.BuildConfig
import com.example.feature_converter.Converter
import com.example.feature_converter.domain.ConverterApi
import com.example.feature_converter.domain.entity.Currency
import java.math.BigDecimal

internal class ConverterViewModel(
    private val converterApi: ConverterApi
) : ViewModel() {
    companion object {
        private val INITIAL_SOURCE_VALUE = BigDecimal.ONE
        private val INITIAL_SOURCE_CURRENCY = Currency.USD
        private val INITIAL_DEST_CURRENCY = Currency.RUB
    }

    private var sourceValue: BigDecimal
    private var destValue: BigDecimal

    private val _viewData: MutableLiveData<ConverterViewData> = MutableLiveData()
    val viewData: LiveData<ConverterViewData> get() = _viewData
    private val viewDataValue get() = _viewData.value!!

    val eventData: EventLiveData = EventLiveData()

    init {
        sourceValue = INITIAL_SOURCE_VALUE.also {
            eventData.setEvent(Event.SetSourceCurrencyValue(it))
        }
        destValue = converterApi.convert(
            INITIAL_SOURCE_CURRENCY,
            INITIAL_DEST_CURRENCY,
            INITIAL_SOURCE_VALUE
        ).also {
            eventData.setEvent(Event.SetDestCurrencyValue(it))
        }
        _viewData.value = ConverterViewData(
            sourceCurrency = INITIAL_SOURCE_CURRENCY,
            destCurrency = INITIAL_DEST_CURRENCY,
            actualDate = converterApi.actualDate,
            isRefreshing = false
        )
        update()
    }

    private fun update() {
        _viewData.value = viewDataValue.copy(isRefreshing = true)
        converterApi.update(
            onSuccess = {
                destValue = converterApi.convert(
                    source = viewDataValue.sourceCurrency,
                    dest = viewDataValue.destCurrency,
                    value = sourceValue
                ).also {
                    eventData.setEvent(Event.SetDestCurrencyValue(it))
                }
                _viewData.value = viewDataValue.copy(
                    actualDate = converterApi.actualDate,
                    isRefreshing = false
                )
            },
            onFailure = { throwable ->
                if (BuildConfig.DEBUG) {
                    Log.d(Converter.FEATURE_TAG, throwable.message, throwable)
                }
                _viewData.value = viewDataValue.copy(isRefreshing = false)
            }
        )
    }

    fun onUpdatePressed() {
        update()
    }

    fun onSourceValueChanged(value: BigDecimal) {
        sourceValue = value
        destValue = converterApi.convert(
            source = viewDataValue.sourceCurrency,
            dest = viewDataValue.destCurrency,
            value = value
        ).also {
            eventData.setEvent(Event.SetDestCurrencyValue(it))
        }
    }

    fun onDestValueChanged(value: BigDecimal) {
        destValue = value
        sourceValue = converterApi.convert(
            source = viewDataValue.destCurrency,
            dest = viewDataValue.sourceCurrency,
            value = value
        ).also {
            eventData.setEvent(Event.SetSourceCurrencyValue(it))
        }
    }

    fun onSourceCurrencyPressed() {
        val currencies = Currency.values()
            .filter { it != viewDataValue.destCurrency }
            .toList()

        eventData.setEvent(Event.ShowSourceCurrencySelector(currencies))
    }

    fun onDestCurrencyPressed() {
        val currencies = Currency.values()
            .filter { it != viewDataValue.sourceCurrency }
            .toList()

        eventData.setEvent(Event.ShowDestCurrencySelector(currencies))
    }

    fun onSourceCurrencySelected(currency: Currency) {
        _viewData.value = viewDataValue.copy(sourceCurrency = currency)

        destValue = converterApi.convert(
            source = currency,
            dest = viewDataValue.destCurrency,
            value = sourceValue
        ).also {
            eventData.setEvent(Event.SetDestCurrencyValue(it))
        }
    }

    fun onDestCurrencySelected(currency: Currency) {
        _viewData.value = viewDataValue.copy(destCurrency = currency)

        sourceValue = converterApi.convert(
            source = viewDataValue.sourceCurrency,
            dest = currency,
            value = destValue
        ).also {
            eventData.setEvent(Event.SetSourceCurrencyValue(it))
        }
    }

    override fun onCleared() {
        converterApi.dispose()
        super.onCleared()
    }
}