package com.astutify.mvi.demo.presentation

import androidx.appcompat.app.AppCompatActivity
import com.astutify.mvi.demo.Mockable
import com.astutify.mvi.demo.presentation.model.IngredientViewModel
import android.app.Activity
import android.content.Intent
import com.astutify.mvi.demo.presentation.screen.editingredient.EditIngredientView

@Mockable
class Navigator (
    private val activity: AppCompatActivity
) {

    fun goToAddIngredient(ingredient: IngredientViewModel? = null) {
        val intent = EditIngredientView.getIntent(activity, ingredient)
        activity.startActivityForResult(intent, 0)
    }

    fun finishAddIngredient(ingredient: IngredientViewModel) {
        val data = Intent()
        data.putExtra(ingredient::class.java.name, ingredient)
        activity.setResult(Activity.RESULT_OK, data)
        activity.finish()
    }

    fun goBack() {
        activity.finish()
    }
}
