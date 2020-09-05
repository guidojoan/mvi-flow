package com.astutify.mvi.demo.presentation.screen.ingredients.list

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.astutify.mvi.demo.presentation.model.IngredientViewModel

class IngredientsListView(context: Context, attrs: AttributeSet?) : RecyclerView(context, attrs) {

    private var manager: LinearLayoutManager = LinearLayoutManager(context)
    private var eventsListener: ((Event) -> Unit)? = null

    init {
        manager.orientation = VERTICAL
        layoutManager = manager
        adapter = IngredientsListAdapter()
    }

    fun clear() {
        getListAdapter().clear()
    }

    fun render(ingredients: List<IngredientViewModel>) {
        getListAdapter().setItems(ingredients)
    }

    fun bind(
        eventsListener: (Event) -> Unit
    ) {
        this.eventsListener = eventsListener
        getListAdapter().setEventsListener(eventsListener)
    }

    private fun getListAdapter(): IngredientsListAdapter {
        return adapter as IngredientsListAdapter
    }

    sealed class Event {
        data class IngredientClicked(val ingredient: IngredientViewModel) : Event()
    }
}
