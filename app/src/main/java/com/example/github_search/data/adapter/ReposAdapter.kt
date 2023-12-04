package com.example.github_search.data.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

import com.example.github_search.R
import com.example.github_search.data.model.generic.Item
import com.example.github_search.databinding.SearchResultsLayoutGridBinding
import com.example.github_search.module.utils.ObjectWrapperForBinder
import com.example.github_search.ui.activity.DetailsActivity

class ReposAdapter(private val con: Context) :
    RecyclerView.Adapter<ReposAdapter.ArticlesViewHolder>() {
    private var datalist: ArrayList<Item> = ArrayList()

    private var mListner: OnServiceclickListner? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        val binding = DataBindingUtil.inflate<SearchResultsLayoutGridBinding>(
            LayoutInflater.from(con),
            R.layout.search_results_layout_grid,
            parent,
            false
        )
        return ArticlesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size

    }
    //override fun getItemCount(): Int = 20


    override fun onBindViewHolder(holder: ArticlesViewHolder, position: Int) {
        holder.bind(datalist[position])

    }

    fun setdatalist(newlist: ArrayList<Item>) {
        datalist = newlist
        notifyDataSetChanged()
    }

    fun updateList(newlist: ArrayList<Item>) {
        var oldssize = datalist.size
        datalist.addAll(newlist)
        notifyItemRangeChanged(oldssize, datalist.size)

    }


    inner class ArticlesViewHolder(private val rowBinding: SearchResultsLayoutGridBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {
        fun bind(model: Item) {
            Glide.with(con)
                .load(model.owner?.avatarUrl)
                .into(rowBinding.avatar)

            rowBinding.name.text = model.name
            rowBinding.fullName.text = model.fullName
            rowBinding.fork.text = model.forks.toString()
            rowBinding.watchers.text = model.watchers.toString()

            rowBinding.root.setOnClickListener(View.OnClickListener {
                    mListner?.onserviceclicked(model)

            })
        }
    }

    fun setOnServiceClickListner(listner: OnServiceclickListner) {
        this.mListner = listner
    }

    interface OnServiceclickListner {
        fun onserviceclicked(model: Item)

    }
}