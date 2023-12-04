package com.example.github_search.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager

import com.example.github_search.data.adapter.ReposAdapter
import com.example.github_search.data.model.generic.ContributorItem
import com.example.github_search.data.model.generic.Item
import com.example.github_search.data.viewmodel.ContributorViewModel
import com.example.github_search.data.viewmodel.SearchViewModel
import com.example.github_search.databinding.ActivityCountributorBinding
import com.example.github_search.databinding.ActivityDetailsBinding
import com.example.github_search.databinding.SearchResultsLayoutGridBinding
import com.example.github_search.module.utils.ObjectWrapperForBinder
import com.example.github_search.module.utils.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountributorActivity : AppCompatActivity(),ReposAdapter.OnServiceclickListner {
    private lateinit var binding: ActivityCountributorBinding
    private var repoModel: ContributorItem? = null
    private var page: Int = 0
    private val viewModel by viewModels<ContributorViewModel>()
    private lateinit var adapter: ReposAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountributorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repoModel =
            ((intent.extras!!.getBinder("Model") as ObjectWrapperForBinder?)!!.data) as ContributorItem

        Log.d(CountributorActivity::class.java.name, "model= $repoModel")

        val mLayoutManager = GridLayoutManager(this, 2)
        binding.contryRepoRecycler.layoutManager = mLayoutManager
        setRepoAdapter()

        viewModel.contributorsRepo.observe(
            this
        ) { value ->
            if (page == 1) {
                value.data?.let { ArrayList(it) }?.let { adapter.setdatalist(it) }
            } else {
                value.data?.let { ArrayList(it) }?.let { adapter.updateList(it) }
            }


        };
        viewModel.getContributersList(
           repoModel!!.login,
            "$page",
            "10"
        )
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

        viewModel.getContributersList(repoModel!!.login, "$page", "10")


    }

    private fun setRepoAdapter() {
        adapter= ReposAdapter(this)
        adapter.setOnServiceClickListner(this)

        binding.contryRepoRecycler.adapter = adapter
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