package com.astutify.mvi.demo.presentation.screen.editingredient

import android.os.Parcelable
import com.astutify.mvi.demo.presentation.model.IngredientViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EditIngredientViewState(
    val ingredient: IngredientViewModel = IngredientViewModel(),
    val saveEnabled: Boolean = false,
    val mode: Mode = Mode.NEW,
    val status: Status? = null
) : Parcelable {

    fun copyState(
        ingredient: IngredientViewModel = this.ingredient,
        saveEnabled: Boolean = this.saveEnabled,
        mode: Mode = this.mode,
        status: Status? = null
    ) =
        EditIngredientViewState(
            ingredient,
            saveEnabled,
            mode,
            status
        )

    enum class Status {
        LOADING, ERROR_SAVE
    }

    enum class Mode {
        NEW, EDIT
    }
}
