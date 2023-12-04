package com.example.github_search.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager

import com.example.github_search.data.adapter.ReposAdapter
import com.example.github_search.data.model.generic.Item
import com.example.github_search.data.viewmodel.SearchViewModel
import com.example.github_search.databinding.ActivitySearchBinding
import com.example.github_search.databinding.SearchResultsLayoutGridBinding
import com.example.github_search.module.utils.ObjectWrapperForBinder
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchActivity : AppCompatActivity(), ReposAdapter.OnServiceclickListner {
    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    private var page: Int = 0
    private lateinit var adapter: ReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mLayoutManager = GridLayoutManager(this@SearchActivity, 2)
        binding.repoRecycler.layoutManager = mLayoutManager
        setRepoAdapter()

        viewModel.searchedRepo.observe(
            this
        ) { value ->

           /* val dao = DatabaseClient.getInstance(this)?.appDatabase
                ?.taskDao()*/

            if (page == 1) {
                value.data?.items?.let { ArrayList(it) }?.let {
                    adapter.setdatalist(it)

                /*    for (data in it){
                        data.search_key=binding.searchEdt.text.toString().trim()
                        dao?.insert(data)
                    }*/

                }


            } else {
                value.data?.items?.let { ArrayList(it) }?.let { adapter.updateList(it) }
            }

            binding.repoRecycler.visibility = View.VISIBLE
            binding.progressCircular.visibility = View.GONE
        };

        binding.searchEdt.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> // Check if the event is the Enter key
            Log.d(SearchActivity::class.java.name, "You Pressed")
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                // Handle the Enter key press
                // You can perform any action you want here
                // For example, you can hide the keyboard
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchEdt.windowToken, 0)
                Log.d(SearchActivity::class.java.name, "You Pressed")

                if (binding.searchEdt.text.toString().trim().isNotEmpty()) {
                    page = 1;
                    viewModel.getSearchedRepository(
                        binding.searchEdt.text.toString().trim(),
                        "$page",
                        "10"
                    )
                    binding.progressCircular.visibility = View.VISIBLE
                    binding.repoRecycler.visibility = View.GONE
                }
                // Return true to indicate that you have handled the key press
                return@OnKeyListener true
            }
            // Return false to let the system handle other key events
            false
        })
        binding.nestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            // on scroll change we are checking when users scroll as bottom.
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                // in this method we are incrementing page number,
                // making progress bar visible and calling get data method.
                page++
                getDataFromAPI(page)
            }
        })

    }

    private fun getDataFromAPI(page: Int) {

        viewModel.getSearchedRepository(binding.searchEdt.text.toString().trim(), "$page", "10")


    }

    private fun setRepoAdapter() {
        adapter = ReposAdapter(this@SearchActivity)
        adapter.setOnServiceClickListner(this)

        binding.repoRecycler.adapter = adapter
    }

    override fun onserviceclicked(model: Item) {
        val bundle = Bundle()
        bundle.putBinder("Model", ObjectWrapperForBinder(model))
        startActivity(
            Intent(
                this,
                DetailsActivity::class.java
            ).putExtras(bundle)
        )
    }
}