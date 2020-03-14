package com.example.converter_demo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.feature_converter.Converter
import com.example.feature_converter.di.ConverterDependencies
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private val converter by lazy {
        Converter(
            object : ConverterDependencies {
                override val context: Context get() = applicationContext
                override val gson: Gson get() = Gson()
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, converter.createFragment())
                .commitNow()
        }
    }
}
