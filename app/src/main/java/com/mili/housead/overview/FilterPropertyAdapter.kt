package com.mili.housead.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mili.housead.databinding.ListItemFilterEntryChipBinding
enum class FilterType{
    CATEGORY,
    NEIGHBORHOOD,
    FEATURE,
    ROOM,
    SELL,
    PREPAYMENT,
    RENT
}
class FilterPropertyAdapter(private val onClickListener: OnClickListener):ListAdapter<FilterType,FilterPropertyAdapter.ViewHolder>(FilterPropertyDiffCallback()){

    class ViewHolder private constructor(val binding: ListItemFilterEntryChipBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(type: FilterType){
            binding.filter = type
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup):ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemFilterEntryChipBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    override fun submitList(list: MutableList<FilterType>?) {
        super.submitList(list?.let {
            ArrayList(it)
        })
    }

    class FilterPropertyDiffCallback: DiffUtil.ItemCallback<FilterType>(){
        override fun areItemsTheSame(oldItem: FilterType, newItem: FilterType): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: FilterType, newItem: FilterType): Boolean {
            return  oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.binding.filterChip.setOnClickListener {
            onClickListener.onClose(item)
        }
    }

    interface OnClickListener{
        fun onClose(filterType: FilterType)
    }
}

