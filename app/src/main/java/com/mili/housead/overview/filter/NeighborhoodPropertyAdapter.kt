package com.mili.housead.overview.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mili.housead.databinding.ListItemOptionEntryChipBinding
import com.mili.housead.network.model.NeighborhoodProperty

class NeighborhoodPropertyAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<NeighborhoodProperty, NeighborhoodPropertyAdapter.ViewHolder>
        (NeighborhoodPropertyDiff()) {

    class ViewHolder private constructor(val binding: ListItemOptionEntryChipBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemOptionEntryChipBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }

        fun bind(property: NeighborhoodProperty) {
            binding.neighborhood = property
            binding.executePendingBindings()
        }
    }

    override fun submitList(list: MutableList<NeighborhoodProperty>?) {
        super.submitList(list?.let {
            ArrayList(it)
        })
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val neighborhood = getItem(position)
        holder.bind(neighborhood)
        holder.binding.neighborhoodChip.setOnCloseIconClickListener {
            onClickListener.onClose(neighborhood)
        }
    }

    interface OnClickListener {
        fun onClose(property: NeighborhoodProperty)
    }


    class NeighborhoodPropertyDiff : DiffUtil.ItemCallback<NeighborhoodProperty>() {
        override fun areItemsTheSame(
            oldItem: NeighborhoodProperty,
            newItem: NeighborhoodProperty
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: NeighborhoodProperty,
            newItem: NeighborhoodProperty
        ): Boolean {
            return oldItem == newItem
        }

    }
}