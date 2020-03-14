package com.example.feature_converter.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.*

internal class EventLiveData : MutableLiveData<LinkedList<Event>>() {

    fun setEvent(event: Event) {
        value = (value ?: LinkedList()).apply { add(event) }
    }

    @MainThread
    fun observe(
        owner: LifecycleOwner,
        block: (Event) -> Unit
    ) {
        this.observe(owner, Observer { events ->
            if (events == null) {
                return@Observer
            }
            var event: Event?
            do {
                event = events.poll()
                if (event != null) {
                    block.invoke(event)
                }
            } while (event != null)
        })
    }
}