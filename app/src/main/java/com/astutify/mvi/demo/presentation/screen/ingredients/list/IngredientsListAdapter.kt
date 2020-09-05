package com.astutify.mvi.demo.presentation.screen.ingredients.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mvi.demo.R
import com.astutify.mvi.demo.presentation.model.IngredientViewModel

class IngredientsListAdapter :
    ListAdapter<IngredientViewModel, RecyclerView.ViewHolder>(diffCallback) {

    private var eventListener: ((IngredientsListView.Event) -> Unit)? = null

    companion object {
        private val diffCallback =
            object : DiffUtil.ItemCallback<IngredientViewModel>() {
                override fun areItemsTheSame(
                    oldItem: IngredientViewModel,
                    newItem: IngredientViewModel
                ): Boolean = oldItem.id == newItem.id

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(
                    oldItem: IngredientViewModel,
                    newItem: IngredientViewModel
                ): Boolean = oldItem == newItem
            }
    }

    fun setEventsListener(eventListener: (IngredientsListView.Event) -> Unit) {
        this.eventListener = eventListener
    }

    fun setItems(data: List<IngredientViewModel>) {
        submitList(data)
    }

    fun clear() {
        submitList(emptyList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return IngredientItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_ingredient, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as IngredientItemViewHolder).bind(getItem(position), eventListener)
    }
}
