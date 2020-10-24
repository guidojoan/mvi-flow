package com.astutify.mviflow.demo.presentation.screen.ingredients

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.astutify.mviflow.demo.R
import com.astutify.mviflow.demo.databinding.ViewListIngredientsBinding
import com.astutify.mviflow.demo.di.AppComponentProvider
import com.astutify.mviflow.demo.presentation.model.IngredientViewModel
import com.astutify.mviflow.demo.presentation.screen.ingredients.list.IngredientsListView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class IngredientsView : AppCompatActivity(), SearchView.OnQueryTextListener {

    @Inject
    lateinit var controller: IngredientsViewFeature

    private lateinit var view: ViewListIngredientsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateView()
        injectView()
        initToolbar()
        initSearchView()
        initListeners()
        initController()
    }

    private fun inflateView() {
        view = ViewListIngredientsBinding.inflate(layoutInflater)
        setContentView(view.root)
    }

    private fun injectView() {
        (application as AppComponentProvider)
            .appComponent
            .ingredientsViewBuilder()
            .withActivity(this)
            .build()
            .inject(this)
    }

    private fun initToolbar() {
        view.toolbar.inflateMenu(R.menu.menu_ingredients)
        view.toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
    }

    private fun initSearchView() {
        val searchItem: MenuItem = view.toolbar.menu.findItem(R.id.searchBar)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_ingredient)
        searchView.setOnCloseListener {
            view.ingredients.clear()
            controller.accept(IngredientsViewEvent.ClickCloseSearch)
            false
        }
        searchView.setOnQueryTextListener(this)
    }

    private fun initListeners() {
        view.swipeToRefresh.setOnRefreshListener {
            view.ingredients.clear()
            controller.accept(IngredientsViewEvent.ClickRefresh)
        }
        view.buttonAddIngredient.setOnClickListener {
            controller.accept(IngredientsViewEvent.ClickAddIngredient)
        }
        view.ingredients.bind {
            when (it) {
                is IngredientsListView.Event.IngredientClicked -> controller.accept(
                    IngredientsViewEvent.IngredientClicked(it.ingredient)
                )
            }
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

    private fun render(viewState: IngredientsViewState) {
        view.ingredients.render(viewState.ingredients)
        renderStatus(viewState)
    }

    private fun renderStatus(viewState: IngredientsViewState) {
        view.swipeToRefresh.isRefreshing = false
        when (viewState.status) {
            IngredientsViewState.Status.LOADING -> showLoading()
            IngredientsViewState.Status.LOADING_ERROR -> renderNetworkError()
            IngredientsViewState.Status.NO_RESULTS -> renderEmptyState()
            else -> hideStatusView()
        }
    }

    private fun hideStatusView() {
        view.statusView.hide()
    }

    private fun showLoading() {
        view.statusView.renderLoading()
    }

    private fun renderEmptyState() {
        view.statusView.renderMessage(
            getString(R.string.empty_state_ingredients),
            ActivityCompat.getDrawable(this, R.drawable.img_empty)
        )
    }

    private fun renderNetworkError() {
        view.statusView.renderMessage(
            getString(R.string.generic_network_error),
            ContextCompat.getDrawable(this, R.drawable.img_error)
        ) {
            controller.accept(IngredientsViewEvent.LoadData)
        }
    }

    private fun showUnknownError() {
        Snackbar.make(view.root, R.string.unknown_error, Snackbar.LENGTH_SHORT).show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        controller.accept(
            IngredientsViewEvent.Search(newText!!)
        )
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (data.hasExtra(INGREDIENT_EXTRA)) {
                data.getParcelableExtra<IngredientViewModel>(INGREDIENT_EXTRA)?.let {
                    controller.accept(IngredientsViewEvent.IngredientAdded(it))
                }
            }
        }
    }

    companion object {
        private val INGREDIENT_EXTRA = IngredientViewModel::class.java.name
    }
}
