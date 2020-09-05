package com.astutify.mvi.demo.presentation.screen.editingredient.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import com.astutify.mvi.demo.R

class LoadingButton(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val text: TextView
    private val loading: ContentLoadingProgressBar
    private val button: ImageView

    init {
        LayoutInflater.from(context).inflate(R.layout.view_loading_button, this, true)
        text = findViewById(R.id.text)
        loading = findViewById(R.id.loadingProgress)
        button = findViewById(R.id.button)
        if (attrs != null) {
            loadAttributes(context, attrs)
        }
    }

    private fun loadAttributes(context: Context, attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton)
        val buttonText = attributeArray.getString(R.styleable.LoadingButton_buttonText)
        text.text = buttonText
        attributeArray.recycle()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        button.isEnabled = enabled
    }

    fun showLoading() {
        text.visibility = View.GONE
        loading.visibility = View.VISIBLE
        isClickable = false
    }

    fun hideLoading() {
        text.visibility = View.VISIBLE
        loading.visibility = View.GONE
        isClickable = true
    }
}
