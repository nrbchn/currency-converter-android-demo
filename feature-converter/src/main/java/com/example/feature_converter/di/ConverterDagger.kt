package com.example.feature_converter.di

internal object ConverterDagger {
    private var _component: ConverterComponent? = null

    fun init(dependencies: ConverterDependencies) {
        _component = DaggerConverterComponent
            .builder()
            .converterDependencies(dependencies)
            .converterModule(ConverterModule())
            .build()
    }

    val component: ConverterComponent
        get() = _component ?: throw IllegalStateException("Component is not initialized")

    fun release() {
        _component = null
    }
}