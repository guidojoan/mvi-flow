package com.astutify.mviflow.demo.presentation.screen.ingredients.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.astutify.mviflow.demo.databinding.ViewLoadingBinding

class StatusView(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var view: ViewLoadingBinding =
        ViewLoadingBinding.inflate(LayoutInflater.from(context), this, true)

    fun renderLoading() {
        showLoading(1f)
    }

    fun renderMessage(messageValue: String, imageDrawable: Drawable?, onClick: (() -> Unit)? = null) {
        with(view) {
            background.alpha = 1f
            view.content.message.text = messageValue
            view.content.message.visibility = View.VISIBLE
            view.content.image.setImageDrawable(imageDrawable)
            view.content.image.visibility = View.VISIBLE
            view.content.loading.visibility = View.GONE
        }
        visibility = View.VISIBLE
        setOnClickListener { onClick?.invoke() }
    }

    fun hide() {
        visibility = View.GONE
    }

    private fun showLoading(alpha: Float) {
        removeListener()
        with(view) {
            background.alpha = alpha
            view.content.loading.visibility = View.VISIBLE
            view.content.message.visibility = View.GONE
            view.content.image.visibility = View.GONE
        }
        visibility = View.VISIBLE
    }

    private fun removeListener() {
        setOnClickListener {}
    }
}
