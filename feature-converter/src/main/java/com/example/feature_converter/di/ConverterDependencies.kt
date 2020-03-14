package com.example.feature_converter.di

import android.content.Context
import com.google.gson.Gson

interface ConverterDependencies {
    val context: Context
    val gson: Gson
}