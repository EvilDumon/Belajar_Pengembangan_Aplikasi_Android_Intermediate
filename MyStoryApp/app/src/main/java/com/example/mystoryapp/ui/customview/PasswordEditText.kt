package com.example.mystoryapp.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class PasswordEditText : AppCompatEditText {
    private val errorMessage = "The amount of character at least 8 characters long"
    private var isError = false

    constructor(context: Context) : super(context) {
        showErrorPassword()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        showErrorPassword()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        showErrorPassword()
    }

    private fun showErrorPassword() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (text.length < 8) {
                    error = errorMessage
                    isError = true
                } else{
                    error = null
                    isError = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}