package com.example.feature_converter

import androidx.fragment.app.Fragment
import com.example.feature_converter.di.ConverterDagger
import com.example.feature_converter.di.ConverterDependencies
import com.example.feature_converter.domain.ConverterApi
import com.example.feature_converter.presentation.ConverterFragment

class Converter(dependencies: ConverterDependencies) {
    companion object {
        internal val FEATURE_TAG = "Converter"
    }

    init {
        ConverterDagger.init(dependencies)
    }

    fun createFragment(): Fragment = ConverterFragment()

    fun getApi(): ConverterApi = ConverterDagger.component.converterApi

    fun release() {
        ConverterDagger.release()
    }
}