package com.ann.m17test.ui

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ann.m17test.R
import com.ann.m17test.data.model.User
import com.ann.m17test.data.model.UserSearchResult
import com.ann.m17test.utils.hideSoftKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private lateinit var adapter: MainPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupScrollListener()
        setupEditorActionListener()
        setupObserver()

        savedInstanceState?.getString(LAST_SEARCH)?.let {
            if (mainViewModel.users.value == null) {
                mainViewModel.searchUser(it)
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
        adapter = MainPagingAdapter()
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
                    if (it.isNotBlank()) {
                        recyclerView.scrollToPosition(0)
                        mainViewModel.searchUser(it)
                    }
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        searchEditText.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                hideSoftKeyboard(this)
                searchEditText.text?.trim().toString().let {
                    if (it.isNotEmpty()) {
                        recyclerView.scrollToPosition(0)
                        mainViewModel.searchUser(it)
                    }
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
            //TODO:progressbar
            when (it) {
                is UserSearchResult.Success -> {
                    renderList(it.data)
                    recyclerView.visibility = View.VISIBLE
                }
                is UserSearchResult.Error -> {
                    Toast.makeText(this, it.error.message, Toast.LENGTH_LONG).show()
                }
            }
        })
//        mainViewModel.users.observe(this, Observer {
//            when (it.status) {
//                Status.SUCCESS -> {
//                    progressBar.visibility = View.GONE
//                    it.data.let { users -> renderList(users.items) }
//                    recyclerView.visibility = View.VISIBLE
//                }
//                Status.LOADING -> {
//                    progressBar.visibility = View.VISIBLE
//                    recyclerView.visibility = View.GONE
//                }
//                Status.ERROR -> {
//                    //Handle Error
//                    progressBar.visibility = View.GONE
//                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
//                }
//            }
//        })
    }

    private fun renderList(users: List<User>) {
        adapter.submitList(users)
        Log.d("REPO","adapter itemCount: ${adapter.itemCount}")
//        adapter.notifyDataSetChanged()
    }

    companion object {
        private const val LAST_SEARCH: String = "last_search"
    }
}