package com.mili.housead.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mili.housead.databinding.ListItemHouseBinding
import com.mili.housead.network.model.HouseProperty
import kotlinx.android.synthetic.main.fragment_detail.view.*

class HousePropertyAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<HouseProperty, HousePropertyAdapter.ViewHolder>(HousePropertyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val houseProperty = getItem(position)
        val houseBg = holder.itemView.house_bg
        holder.itemView.setOnClickListener {
            onClickListener.onClick(houseProperty, houseBg)
        }
        holder.bind(houseProperty)
    }

    override fun submitList(list: MutableList<HouseProperty>?) {
        super.submitList(list?.let {
            ArrayList(it)
        })
    }

    override fun getItemViewType(position: Int): Int {
        val houseProperty = getItem(position)
        return houseProperty.lease_type
    }


    class ViewHolder private constructor(val binding: ListItemHouseBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(house: HouseProperty) {
            binding.house = house
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemHouseBinding.inflate(
                    layoutInflater, parent, false
                )
                return ViewHolder(binding)
            }
        }
    }


    class OnClickListener(
        val clickListener: (
            houseProperty: HouseProperty, houseBg: ImageView
        ) -> Unit
    ) {
        fun onClick(houseProperty: HouseProperty, houseBg: ImageView) =
            clickListener(houseProperty, houseBg)
    }
}


class HousePropertyDiffCallback : DiffUtil.ItemCallback<HouseProperty>() {
    override fun areItemsTheSame(oldItem: HouseProperty, newItem: HouseProperty): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: HouseProperty, newItem: HouseProperty): Boolean {
        return oldItem == newItem
    }

}

