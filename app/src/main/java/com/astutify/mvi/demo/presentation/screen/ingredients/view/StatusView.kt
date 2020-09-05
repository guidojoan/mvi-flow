package com.astutify.mvi.demo.presentation.screen.ingredients.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ContentLoadingProgressBar
import com.astutify.mvi.demo.R

class StatusView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private val background: ConstraintLayout by lazy {
        findViewById<ConstraintLayout>(R.id.background)
    }
    private val message: TextView by lazy {
        findViewById<TextView>(R.id.message)
    }
    private val image: ImageView by lazy {
        findViewById<ImageView>(R.id.image)
    }
    private val loading: ContentLoadingProgressBar by lazy {
        findViewById<ContentLoadingProgressBar>(R.id.loading)
    }

    fun renderLoading() {
        showLoading(1f)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_loading, this, true)
    }

    fun renderMessage(
        messageValue: String,
        imageDrawable: Drawable?,
        onClick: (() -> Unit)? = null
    ) {
        background.alpha = 1f
        message.text = messageValue
        message.visibility = View.VISIBLE
        image.setImageDrawable(imageDrawable)
        image.visibility = View.VISIBLE
        loading.visibility = View.GONE
        visibility = View.VISIBLE
        setOnClickListener { onClick?.invoke() }
    }

    fun hide() {
        visibility = View.GONE
    }

    private fun showLoading(alpha: Float) {
        removeListener()
        background.alpha = alpha
        loading.visibility = View.VISIBLE
        message.visibility = View.GONE
        image.visibility = View.GONE
        visibility = View.VISIBLE
    }

    private fun removeListener() {
        setOnClickListener {}
    }
}
