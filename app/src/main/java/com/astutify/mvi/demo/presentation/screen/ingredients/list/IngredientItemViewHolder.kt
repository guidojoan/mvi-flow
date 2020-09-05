package com.astutify.mvi.demo.presentation.screen.ingredients.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mvi.demo.R
import com.astutify.mvi.demo.presentation.model.IngredientViewModel
import com.google.android.material.card.MaterialCardView

class IngredientItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val name : TextView = view.findViewById(R.id.name)
    private val letterContent : TextView = view.findViewById(R.id.letterContent)
    private val card : MaterialCardView = view.findViewById(R.id.card)

    fun bind(
        item: IngredientViewModel,
        onClick: ((IngredientsListView.Event) -> Unit)?
    ) {
        name.text = item.name
        letterContent.text = item.name.take(1)
        card.setOnClickListener { onClick?.invoke(IngredientsListView.Event.IngredientClicked(item)) }
    }
}
