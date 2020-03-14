package com.example.feature_converter.presentation

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.feature_converter.R
import com.example.feature_converter.di.ConverterDagger
import kotlinx.android.synthetic.main.fragment_converter.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

internal class ConverterFragment : Fragment() {
    private lateinit var viewModel: ConverterViewModel

    private val sourceTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            var text = s?.toString() ?: ""
            var format = false
            if (text == "0" && before == 0) {
                format = true
                text = "0."
            }
            while (text != "0." && text.firstOrNull() == '0') {
                format = true
                text = text.drop(1)
            }
            val sections = text.split(".")
            if (text != "0." && sections.size > 2) {
                format = true
                sections.forEachIndexed { index, section ->
                    if (index == 0) {
                        text = "$section."
                    } else {
                        text += section
                    }
                }
            }
            if (text.firstOrNull() == '.') {
                format = true
                text = "0$text"
            }
            if (format) {
                converter_edittext_source_value.removeTextChangedListener(this)
                converter_edittext_source_value.setText(text)
                converter_edittext_source_value.setSelection(text.length)
                converter_edittext_source_value.addTextChangedListener(this)
            }
            val value = text.toBigDecimalOrNull() ?: BigDecimal.ZERO
            viewModel.onSourceValueChanged(value)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable?) {}
    }

    private val destTextWatcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            var text = s?.toString() ?: ""
            var format = false
            if (text == "0" && before == 0) {
                format = true
                text = "0."
            }
            while (text != "0." && text.firstOrNull() == '0') {
                format = true
                text = text.drop(1)
            }
            val sections = text.split(".")
            if (text != "0." && sections.size > 2) {
                format = true
                sections.forEachIndexed { index, section ->
                    if (index == 0) {
                        text = "$section."
                    } else {
                        text += section
                    }
                }
            }
            if (text.firstOrNull() == '.') {
                format = true
                text = "0$text"
            }
            if (format) {
                converter_edittext_dest_value.removeTextChangedListener(this)
                converter_edittext_dest_value.setText(text)
                converter_edittext_dest_value.setSelection(text.length)
                converter_edittext_dest_value.addTextChangedListener(this)
            }
            val value = text.toBigDecimalOrNull() ?: BigDecimal.ZERO
            viewModel.onDestValueChanged(value)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun afterTextChanged(s: Editable?) {}
    }

    private val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(ConverterViewModel::class.java)) {
                    return ConverterViewModel(ConverterDagger.component.converterApi) as T
                }
                return modelClass.newInstance()
            }
        }
        viewModel = ViewModelProvider(viewModelStore, factory).get(ConverterViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_converter, null)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        converter_textview_source_currency.setOnClickListener { viewModel.onSourceCurrencyPressed() }
        converter_textview_dest_currency.setOnClickListener { viewModel.onDestCurrencyPressed() }
        converter_edittext_source_value.addTextChangedListener(sourceTextWatcher)
        converter_edittext_dest_value.addTextChangedListener(destTextWatcher)
        converter_textview_update.setOnClickListener { viewModel.onUpdatePressed() }
        viewModel.viewData.observe(viewLifecycleOwner, Observer { viewData ->
            if (viewData == null) {
                return@Observer
            }
            converter_textview_source_currency.text = viewData.sourceCurrency.name
            converter_textview_dest_currency.text = viewData.destCurrency.name
            val actualDate = formatter.format(viewData.actualDate)
            converter_textview_date.text = getString(R.string.converter_actual_date, actualDate)
            converter_textview_update.visibility =
                if (viewData.isRefreshing) View.INVISIBLE else View.VISIBLE
            converter_progressbar_update.visibility =
                if (viewData.isRefreshing) View.VISIBLE else View.GONE
        })
        viewModel.eventData.observe(viewLifecycleOwner) { event ->
            when (event) {
                is Event.SetSourceCurrencyValue -> {
                    with(converter_edittext_source_value) {
                        removeTextChangedListener(sourceTextWatcher)
                        val text = event.value.toString()
                        setText(text)
                        setSelection(text.length)
                        addTextChangedListener(sourceTextWatcher)
                    }
                }
                is Event.SetDestCurrencyValue -> {
                    with(converter_edittext_dest_value) {
                        removeTextChangedListener(destTextWatcher)
                        val text = event.value.toString()
                        setText(text)
                        setSelection(text.length)
                        addTextChangedListener(destTextWatcher)
                    }
                }
                is Event.ShowSourceCurrencySelector -> {
                    AlertDialog.Builder(context)
                        .setItems(event.codes.map { it.name }.toTypedArray()) { dialog, index ->
                            viewModel.onSourceCurrencySelected(event.codes[index])
                            dialog.dismiss()
                        }
                        .show()
                }
                is Event.ShowDestCurrencySelector -> {
                    AlertDialog.Builder(context)
                        .setItems(event.codes.map { it.name }.toTypedArray()) { dialog, index ->
                            viewModel.onDestCurrencySelected(event.codes[index])
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
    }
}