package com.astutify.mvi.demo.presentation.screen.ingredients

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.astutify.mvi.demo.AppComponentProvider
import com.astutify.mvi.demo.R
import com.astutify.mvi.demo.presentation.model.IngredientViewModel
import com.astutify.mvi.demo.presentation.screen.ingredients.list.IngredientsListView
import com.astutify.mvi.demo.presentation.screen.ingredients.view.StatusView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class IngredientsView : AppCompatActivity(), SearchView.OnQueryTextListener {

    @Inject
    lateinit var controller: IngredientsViewFeature

    private val disposable = CompositeDisposable()

    private val statusView : StatusView by lazy {
        findViewById<StatusView>(R.id.statusView)
    }
    private val toolbar : Toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar)
    }
    private val swipeToRefresh: SwipeRefreshLayout by lazy {
        findViewById<SwipeRefreshLayout>(R.id.swipeToRefresh)
    }
    private val ingredients: IngredientsListView by lazy {
        findViewById<IngredientsListView>(R.id.ingredients)
    }
    private val buttonAddIngredient : FloatingActionButton by lazy {
        findViewById<FloatingActionButton>(R.id.buttonAddIngredient)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inflateView()
        injectView()
        initToolbar()
        initSearchView()
        initViews()
        initController()
    }

    private fun injectView() {
        (application as AppComponentProvider)
            .appComponent
            .ingredientsViewBuilder()
            .withActivity(this)
            .build()
            .inject(this)
    }

    private fun inflateView() {
        setContentView(R.layout.view_list_ingredients)
    }

    private fun initController() {
        disposable.add(
            Observable.wrap(controller)
                .distinctUntilChanged()
                .subscribe(
                    ::render
                ) { throw RuntimeException(it) }
        )
    }

    private fun initToolbar() {
        toolbar.inflateMenu(R.menu.menu_ingredients)
        toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
    }

    private fun initViews() {
        swipeToRefresh.setOnRefreshListener {
            ingredients.clear()
            controller.accept(IngredientsViewEvent.ClickRefresh)
        }
        buttonAddIngredient.setOnClickListener {
            controller.accept(IngredientsViewEvent.ClickAddIngredient)
        }
        ingredients.bind {
            when (it) {
                is IngredientsListView.Event.IngredientClicked -> controller.accept(
                    IngredientsViewEvent.IngredientClicked(it.ingredient)
                )
            }
        }
    }

    private fun initSearchView() {
        val searchItem: MenuItem = toolbar.menu.findItem(R.id.searchBar)
        val searchView: SearchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_ingredient)
        searchView.setOnCloseListener {
            ingredients.clear()
            controller.accept(IngredientsViewEvent.ClickCloseSearch)
            false
        }
        searchView.setOnQueryTextListener(this)
    }

    private fun render(viewState: IngredientsViewState) {
        ingredients.render(viewState.ingredients)
        renderStatus(viewState)
    }

    private fun renderStatus(viewState: IngredientsViewState) {
        swipeToRefresh.isRefreshing = false
        when (viewState.status) {
            IngredientsViewState.Status.LOADING -> showLoading()
            IngredientsViewState.Status.LOADING_ERROR -> renderNetworkError()
            IngredientsViewState.Status.NO_RESULTS -> renderEmptyState()
            else -> hideStatusView()
        }
    }

    private fun hideStatusView() {
        statusView.hide()
    }

    private fun showLoading() {
        statusView.renderLoading()
    }

    private fun renderEmptyState() {
        statusView.renderMessage(
            getString(R.string.empty_state_ingredients),
            ActivityCompat.getDrawable(this, R.drawable.img_huevo)
        )
    }

    private fun renderNetworkError() {
        statusView.renderMessage(
            getString(R.string.generic_network_error),
            ContextCompat.getDrawable(this, R.drawable.img_huevo)
        ) {
            controller.accept(IngredientsViewEvent.LoadData)
        }
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

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        controller.dispose()
    }

    companion object {
        private val INGREDIENT_EXTRA = IngredientViewModel::class.java.name
    }
}
