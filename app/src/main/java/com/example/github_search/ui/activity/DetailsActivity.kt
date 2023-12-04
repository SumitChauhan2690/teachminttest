package com.example.github_search.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.github_search.data.adapter.ItemGenericAdapter
import com.example.github_search.data.adapter.ItemGenericCallback
import com.example.github_search.data.adapter.ItemGenericViewHolder
import com.example.github_search.data.model.generic.ContributorItem
import com.example.github_search.data.model.generic.GenericApiResponse
import com.example.github_search.data.model.generic.Item
import com.example.github_search.data.viewmodel.DetailsViewModel
import com.example.github_search.data.viewmodel.SearchViewModel
import com.example.github_search.databinding.ActivityDetailsBinding
import com.example.github_search.databinding.ContributorsLayoutBinding
import com.example.github_search.databinding.SearchResultsLayoutGridBinding
import com.example.github_search.module.utils.ObjectWrapperForBinder
import com.example.github_search.module.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.util.Objects


@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var repoModel: Item? = null
    private val viewModel by viewModels<DetailsViewModel>()
    private lateinit var adapter: ItemGenericAdapter<ContributorItem, ContributorsLayoutBinding>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repoModel = ((intent.extras!!.getBinder("Model") as ObjectWrapperForBinder?)!!.data) as Item

        Log.d(DetailsActivity::class.java.name, "model= $repoModel")

        binding.projectLink.text = repoModel!!.cloneUrl.toString()
        binding.description.text = repoModel!!.description.toString()
        binding.name.text = repoModel!!.name.toString()
        binding.projectLink.movementMethod = LinkMovementMethod.getInstance();
        Glide.with(this).load(repoModel!!.owner?.avatarUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.repoimage)

        val mLayoutManager = GridLayoutManager(this, 2)
        binding.contriRecycler.layoutManager = mLayoutManager
        setRepoAdapter()

        viewModel.contributor.observe(
            this,
            object : Observer<Resource<List<ContributorItem>>> {
                override fun onChanged(value: Resource<List<ContributorItem>>) {
                    adapter.submitList(value.data)
                }
            });

        val contri_url = repoModel!!.contributorsUrl.subSequence(22, repoModel!!.contributorsUrl.length).toString()
        Log.d(DetailsActivity::class.java.name, "Country URL: " + contri_url)

        repoModel!!.owner?.let { viewModel.getContributor(it.login, repoModel!!.name) }
    }

    private fun setRepoAdapter() {
        adapter = ItemGenericAdapter(object :
            ItemGenericCallback<ContributorItem, ContributorsLayoutBinding> {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): ItemGenericViewHolder<ContributorItem, ContributorsLayoutBinding> {
                return ItemGenericViewHolder(
                    ContributorsLayoutBinding.inflate(
                        LayoutInflater.from(
                            parent.context
                        ), parent, false
                    )
                )
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onNotifyDataChange(
                previousList: MutableList<ContributorItem>,
                currentList: MutableList<ContributorItem>,
                itemGenericAdapter: ItemGenericAdapter<ContributorItem, ContributorsLayoutBinding>
            ) {
                itemGenericAdapter.notifyDataSetChanged()
            }

            override fun areContentsTheSame(
                oldItem: ContributorItem,
                newItem: ContributorItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(
                oldItem: ContributorItem,
                newItem: ContributorItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun onItemDataBind(
                model: ContributorItem,
                position: Int,
                itemCount: Int,
                viewDataBinding: ContributorsLayoutBinding
            ) {
                viewDataBinding.apply {
                    Glide.with(this@DetailsActivity)
                        .load(model.avatarUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(viewDataBinding.userImage)

                    this.contributorName.text = model.login

                }
            }

            override fun onItemClick(
                model: ContributorItem,
                position: Int,
                viewDataBinding: ContributorsLayoutBinding
            ) {
                val handler = Handler()
                handler.postDelayed({
                    val bundle = Bundle()
                    bundle.putBinder("Model", ObjectWrapperForBinder(model))
                    startActivity(
                        Intent(
                            this@DetailsActivity,
                            CountributorActivity::class.java
                        ).putExtras(bundle)
                    )

                    val handler1 = Handler()
                    handler1.postDelayed({ adapter.notifyItemChanged(position) }, 50)
                }, 100)
                Log.d(SearchActivity::class.java.name, "model= $model position= $position")
            }
        })

        binding.contriRecycler.adapter = adapter
    }
}