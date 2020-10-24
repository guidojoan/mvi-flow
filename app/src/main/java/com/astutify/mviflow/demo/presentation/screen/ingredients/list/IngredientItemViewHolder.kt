package com.astutify.mviflow.demo.presentation.screen.ingredients.list

import androidx.recyclerview.widget.RecyclerView
import com.astutify.mviflow.demo.databinding.ItemIngredientBinding
import com.astutify.mviflow.demo.presentation.model.IngredientViewModel

class IngredientItemViewHolder(private val view: ItemIngredientBinding) : RecyclerView.ViewHolder(view.root) {

    fun bind(
        item: IngredientViewModel,
        onClick: ((IngredientsListView.Event) -> Unit)?
    ) {
        view.name.text = item.name
        view.letterContent.text = item.name.take(1)
        view.card.setOnClickListener { onClick?.invoke(IngredientsListView.Event.IngredientClicked(item)) }
    }
}
