package com.astutify.mviflow.demo.presentation.screen.editingredient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.astutify.mviflow.Feature
import com.astutify.mviflow.demo.R
import com.astutify.mviflow.demo.databinding.ViewEditIngredientBinding
import com.astutify.mviflow.demo.di.AppComponentProvider
import com.astutify.mviflow.demo.presentation.model.IngredientViewModel
import com.astutify.mviflow.demo.presentation.screen.editingredient.view.AfterTextChangeWatcher
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditIngredientView : AppCompatActivity() {

    @Inject
    lateinit var controller: EditIngredientViewFeature
    private val ingredient: IngredientViewModel? by lazy {
        intent.getParcelableExtra<IngredientViewModel>(INGREDIENT_EXTRA)
    }
    private lateinit var view: ViewEditIngredientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateView()
        injectView(savedInstanceState)
        initListeners()
        initController()
    }

    private fun inflateView() {
        view = ViewEditIngredientBinding.inflate(layoutInflater)
        setContentView(view.root)
    }

    private fun injectView(savedInstanceState: Bundle?) {
        (application as AppComponentProvider)
            .appComponent
            .editIngredientViewBuilder()
            .withActivity(this)
            .withIngredient(ingredient)
            .withInitialState(getInitialState(savedInstanceState))
            .build()
            .inject(this)
    }

    private fun initListeners() {
        view.ingredientName.addTextChangedListener(object : AfterTextChangeWatcher() {
            override fun afterTextChanged(text: Editable) {
                controller.accept(EditIngredientViewEvent.NameChange(text.toString()))
            }
        })
        view.saveButton.setOnClickListener {
            controller.accept(EditIngredientViewEvent.Save)
        }
        view.toolbar.setNavigationOnClickListener {
            controller.accept(EditIngredientViewEvent.ClickBack)
        }
    }

    private fun initController() {
        lifecycle.addObserver(controller)
        lifecycleScope.launch {
            controller.connect()
                .catch { showUnknownError() }
                .collect {
                    render(it)
                }
        }
    }

    private fun getInitialState(savedInstanceState: Bundle?): EditIngredientViewState? {
        return savedInstanceState?.getParcelable(Feature.FEATURE_SAVED_STATE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller.saveState(outState)
    }

    private fun render(viewState: EditIngredientViewState) {
        renderToolbar(viewState)
        renderInputText(viewState)
        renderSaveButton(viewState)
        renderLoading(viewState)
    }

    private fun renderToolbar(viewState: EditIngredientViewState) {
        if (view.toolbar.title.isNullOrBlank()) {
            view.toolbar.title = when (viewState.mode) {
                EditIngredientViewState.Mode.NEW -> getString(R.string.add_ingredient)
                EditIngredientViewState.Mode.EDIT -> getString(R.string.edit_ingredient)
            }
        }
    }

    private fun renderInputText(viewState: EditIngredientViewState) {
        if (view.ingredientName.text!!.isBlank() && viewState.ingredient.name.isNotBlank()) {
            view.ingredientName.setText(viewState.ingredient.name)
        }
    }

    private fun renderSaveButton(viewState: EditIngredientViewState) {
        if (viewState.saveEnabled) enableSaveButton() else disableSaveButton()
    }

    private fun renderLoading(viewState: EditIngredientViewState) {
        when (viewState.status) {
            EditIngredientViewState.Status.LOADING -> showLoading()
            EditIngredientViewState.Status.ERROR_SAVE -> showSaveError()
            else -> hideLoading()
        }
    }

    private fun showLoading() {
        disableNameInputText()
        view.saveButton.showLoading()
    }

    private fun hideLoading() {
        enableNameInputText()
        view.saveButton.hideLoading()
    }

    private fun enableSaveButton() {
        view.saveButton.isEnabled = true
    }

    private fun disableSaveButton() {
        view.saveButton.isEnabled = false
    }

    private fun disableNameInputText() {
        view.ingredientName.isEnabled = false
    }

    private fun enableNameInputText() {
        view.ingredientName.isEnabled = true
    }

    private fun showSaveError() {
        Snackbar.make(view.root, R.string.generic_network_error, Snackbar.LENGTH_SHORT).show()
    }

    private fun showUnknownError() {
        Snackbar.make(view.root, R.string.unknown_error, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val INGREDIENT_EXTRA = "extra:ingredient"
        fun getIntent(context: Context, ingredient: IngredientViewModel?): Intent {
            val intent = Intent(context, EditIngredientView::class.java)
            ingredient?.let {
                intent.putExtra(INGREDIENT_EXTRA, it)
            }
            return intent
        }
    }
}
