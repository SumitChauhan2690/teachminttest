package com.example.github_search.data.adapter

import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.FragmentScoped

@FragmentScoped
class ItemGenericAdapter<DM, VB>(
    private val itemGenericRecyclerCallback: ItemGenericCallback<DM, VB>
) : ListAdapter<DM, ItemGenericViewHolder<DM, VB>>(
    ItemGenericDiffCallback(
        itemGenericRecyclerCallback
    )
) {
    private var scroller: Scroller? = null

    /** Create and Return ViewHolder  */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemGenericViewHolder<DM, VB> {
        return itemGenericRecyclerCallback.onCreateViewHolder(parent, viewType)
    }

    /** Data Bind with ViewHolder */
    override fun onBindViewHolder(holder: ItemGenericViewHolder<DM, VB>, position: Int) {
        holder.bind(getItem(position), position, itemCount, itemGenericRecyclerCallback)
    }

    /** Notify on Change */
    override fun onCurrentListChanged(previousList: MutableList<DM>, currentList: MutableList<DM>) {
        itemGenericRecyclerCallback.onNotifyDataChange(previousList, currentList, this)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        scroller = Scroller(recyclerView.context, DecelerateInterpolator())
        super.onAttachedToRecyclerView(recyclerView)
    }
}

@Suppress("UNCHECKED_CAST")
class ItemGenericViewHolder<DM, VB>(
    private val viewDataBinding: ViewDataBinding
) : RecyclerView.ViewHolder(viewDataBinding.root) {

    /** Bind Data */
    fun bind(
        model: DM,
        position: Int,
        itemCount: Int,
        itemGenericCallback: ItemGenericCallback<DM, VB>
    ) {
        itemGenericCallback.onItemDataBind(model, position, itemCount, viewDataBinding as VB)
        viewDataBinding.root.setOnClickListener {
            itemGenericCallback.onItemClick(
                model,
                position,
                viewDataBinding as VB
            )
        }
    }
}

class ItemGenericDiffCallback<DM, VB>(
    private val itemGenericCallback: ItemGenericCallback<DM, VB>
) : DiffUtil.ItemCallback<DM>() {
    override fun areItemsTheSame(oldItem: DM, newItem: DM): Boolean {
        return itemGenericCallback.areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: DM, newItem: DM): Boolean {
        return itemGenericCallback.areContentsTheSame(oldItem, newItem)
    }
}

interface ItemGenericCallback<DM, VB> {

    /** Create ViewHolder  */
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemGenericViewHolder<DM, VB>

    /** Callback on Items Click */
    fun onItemClick(model: DM, position: Int, viewDataBinding: VB)

    /** Data Bind on ViewHolder */
    fun onItemDataBind(model: DM, position: Int, itemCount: Int, viewDataBinding: VB)

    /**
     * Check Item are same or not.
     * If Item are same return true else false.
     * */
    fun areItemsTheSame(oldItem: DM, newItem: DM): Boolean

    /** Check Item Content are same or not.
     * If Item are same return true else false.
     * */
    fun areContentsTheSame(oldItem: DM, newItem: DM): Boolean

    /** Notify on Data Change. */
    fun onNotifyDataChange(
        previousList: MutableList<DM>,
        currentList: MutableList<DM>,
        itemGenericAdapter: ItemGenericAdapter<DM, VB>
    )
}

