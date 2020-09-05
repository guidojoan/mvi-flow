package com.astutify.mvi.demo.presentation.screen.editingredient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.astutify.mvi.Feature
import com.astutify.mvi.demo.AppComponentProvider
import com.astutify.mvi.demo.R
import com.astutify.mvi.demo.presentation.model.IngredientViewModel
import com.astutify.mvi.demo.presentation.screen.editingredient.view.AfterTextChangeWatcher
import com.astutify.mvi.demo.presentation.screen.editingredient.view.LoadingButton
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class EditIngredientView : AppCompatActivity(){

    @Inject
    lateinit var controller: EditIngredientViewFeature
    private val ingredient: IngredientViewModel? by lazy {
        intent.getParcelableExtra<IngredientViewModel>(INGREDIENT_EXTRA)
    }
    private val disposable = CompositeDisposable()

    private val toolbar : MaterialToolbar by lazy {
        findViewById<MaterialToolbar>(R.id.toolbar)
    }
    private val ingredientName : TextInputEditText by lazy {
        findViewById<TextInputEditText>(R.id.ingredientName)
    }
    private val saveButton : LoadingButton by lazy {
        findViewById<LoadingButton>(R.id.saveButton)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateView()
        injectView(savedInstanceState)
        setupListeners()
        initController()
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

    private fun inflateView() {
        setContentView(R.layout.view_edit_ingredient)
    }

    private fun initController() {
        disposable.add(
            Observable.wrap(controller)
                .subscribe(
                    ::render
                ) { throw RuntimeException(it) }
        )
    }

    private fun getInitialState(savedInstanceState: Bundle?): EditIngredientViewState? {
        return savedInstanceState?.getParcelable(Feature.FEATURE_SAVED_STATE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller.saveState(outState)
    }

    private fun setupListeners() {
        ingredientName.addTextChangedListener(object : AfterTextChangeWatcher() {
            override fun afterTextChanged(text: Editable) {
                controller.accept(EditIngredientViewEvent.NameChange(text.toString()))
            }
        })
        saveButton.setOnClickListener {
            controller.accept(EditIngredientViewEvent.Save)
        }
        toolbar.setNavigationOnClickListener {
            controller.accept(EditIngredientViewEvent.ClickBack)
        }
    }

    private fun render(viewState: EditIngredientViewState) {
        renderView(viewState)
        renderLoading(viewState)
    }

    private fun renderLoading(viewState: EditIngredientViewState) {
        when (viewState.status) {
            EditIngredientViewState.Status.LOADING -> showLoading()
            EditIngredientViewState.Status.ERROR_SAVE -> showSaveError()
            else -> hideLoading()
        }
    }

    private fun showLoading(){
        disableNameInputText()
        saveButton.showLoading()
    }

    private fun hideLoading() {
        enableNameInputText()
        saveButton.hideLoading()
    }

    private fun enableSaveButton(){
        saveButton.isEnabled = true
    }

    private fun disableSaveButton(){
      saveButton.isEnabled = false
    }

    private fun disableNameInputText() {
        ingredientName.isEnabled = false
    }

    private fun enableNameInputText(){
        ingredientName.isEnabled = true
    }

    private fun showSaveError(){
        Snackbar.make(ingredientName, R.string.generic_network_error, Snackbar.LENGTH_SHORT).show()
    }

    private fun renderView(viewState: EditIngredientViewState) {
        if (toolbar.title.isNullOrBlank()) {
            toolbar.title = when (viewState.mode) {
                EditIngredientViewState.Mode.NEW -> getString(R.string.add_ingredient)
                EditIngredientViewState.Mode.EDIT -> getString(R.string.edit_ingredient)
            }
        }
        if (ingredientName.text!!.isBlank() && viewState.ingredient.name.isNotBlank()) {
            ingredientName.setText(viewState.ingredient.name)
        }
        if (viewState.saveEnabled) enableSaveButton() else disableSaveButton()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        controller.dispose()
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
