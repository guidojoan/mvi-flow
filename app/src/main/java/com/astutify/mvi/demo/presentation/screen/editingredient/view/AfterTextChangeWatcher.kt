package com.astutify.mvi.demo.presentation.screen.editingredient.view

import android.text.TextWatcher

abstract class AfterTextChangeWatcher : TextWatcher {

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}
