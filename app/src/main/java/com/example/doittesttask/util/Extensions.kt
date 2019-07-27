package com.example.doittesttask.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.MutableLiveData

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

fun Long.fromMillisToUnix() = this / 1000
fun Long.fromUnixToMillis() = this * 1000