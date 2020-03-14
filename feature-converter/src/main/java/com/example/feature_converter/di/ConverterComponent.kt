package com.example.feature_converter.di

import com.example.feature_converter.di.annotation.ConverterScope
import com.example.feature_converter.domain.ConverterApi
import com.example.feature_converter.network.CurrencyService
import dagger.Component

@ConverterScope
@Component(
    dependencies = [ConverterDependencies::class],
    modules = [ConverterModule::class]
)
internal interface ConverterComponent {
    val converterApi: ConverterApi
}