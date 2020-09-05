package com.astutify.mvi.demo.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IngredientViewModel(
    val id: String = "",
    val name: String = ""
) : Parcelable
