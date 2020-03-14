package com.example.feature_converter.data

import com.example.feature_converter.domain.ConverterApi
import com.example.feature_converter.domain.entity.Currency
import com.example.feature_converter.domain.entity.Rate
import com.example.feature_converter.domain.repository.CurrencyRepository
import com.example.feature_converter.network.CurrencyService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

internal class ConverterApiImpl(
    private val repository: CurrencyRepository,
    private val service: CurrencyService
) : ConverterApi {
    private var disposable: Disposable? = null

    override val actualDate: Date get() = repository.getCurrenciesFromCache().date!!

    override val rates: List<Rate> get() = repository.getCurrenciesFromCache().rates

    override fun update(onSuccess: () -> Unit, onFailure: (Throwable) -> Unit) {
        disposable = service.fetchRates()
            .flatMapCompletable { currencies ->
                Completable.fromAction {
                    repository.saveCurrenciesInCache(currencies)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { onSuccess() },
                { onFailure(it) }
            )
    }

    override fun dispose() {
        disposable?.dispose()
    }

    override fun convert(
        source: Currency,
        dest: Currency,
        value: BigDecimal
    ): BigDecimal {
        val currencies = repository.getCurrenciesFromCache().rates
        val sourceCurrency = currencies.find { it.currency == source }!!
        val destCurrency = currencies.find { it.currency == dest }!!
        return (value.setScale(2, RoundingMode.HALF_UP) / sourceCurrency.value * destCurrency.value)
            .setScale(2, RoundingMode.HALF_UP)
    }
}