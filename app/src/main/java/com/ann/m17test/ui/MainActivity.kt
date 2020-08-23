package com.ann.m17test.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ann.m17test.R
import com.ann.m17test.data.model.User
import com.ann.m17test.ui.adapter.MainPagingAdapter
import com.ann.m17test.ui.viewModel.MainViewModel
import com.ann.m17test.utils.Status
import com.ann.m17test.utils.hideSoftKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var adapter: ConcatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupScrollListener()
        setupEditorActionListener()
        setupObserver()

        savedInstanceState?.getString(LAST_SEARCH)?.let {
            if (mainViewModel.users.value == null) {
                mainViewModel.queryLiveData.postValue(it)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchEditText.text?.let {
            outState.putString(LAST_SEARCH, it.trim().toString())
        }
    }

    private fun setupUI() {
        adapter = ConcatAdapter()
        adapter.addAdapter(0, MainPagingAdapter())

        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )

        recyclerView.adapter = adapter
    }

    private fun setupEditorActionListener() {
        searchEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideSoftKeyboard(this)
                v.text.trim().toString().let {
                    updateSearch(it)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        searchEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                hideSoftKeyboard(this)
                searchEditText.text?.trim().toString().let {
                    updateSearch(it)
                }
                true
            } else {
                false
            }
        }
    }

    private fun setupScrollListener() {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItemPos = layoutManager.findLastVisibleItemPosition()

                mainViewModel.listScrolled(visibleItemCount, lastVisibleItemPos, totalItemCount)
            }
        })
    }

    private fun setupObserver() {
        mainViewModel.users.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { list -> renderList(list) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    Toast.makeText(this, "LOADING", Toast.LENGTH_LONG).show()
                }
                Status.FULL -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun updateSearch(search:String){
        if (search.isNotBlank()){
            recyclerView.scrollToPosition(0)
            mainViewModel.queryLiveData.postValue(search)
        }
    }
    private fun renderList(users: List<User>) {
        //TODO
        val mainAdapter = adapter.adapters[0] as MainPagingAdapter
        mainAdapter.data.addAll(users)
        mainAdapter.notifyDataSetChanged()
        adapter.notifyDataSetChanged()
        Log.d("REPO", "adapter itemCount: ${mainAdapter.itemCount}")
    }

    companion object {
        private const val LAST_SEARCH: String = "last_search"
    }
}