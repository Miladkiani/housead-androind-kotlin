package com.mili.housead.overview.filter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mili.housead.databinding.ListItemOptionChoiceChipBinding
import com.mili.housead.overview.filter.model.OptionProperty

class OptionPropertyAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<OptionProperty, OptionPropertyAdapter.ViewHolder>
        (OptionPropertyDiffCallBack()) {

    class ViewHolder private constructor(val binding: ListItemOptionChoiceChipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: OptionProperty) {
            binding.option = property
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemOptionChoiceChipBinding.inflate(
                    layoutInflater, parent, false
                )
                return ViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

//    override fun submitList(list: MutableList<OptionProperty>?) {
//        super.submitList(list?.let {
//            ArrayList(it)
//        })
//    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = getItem(position)
        holder.bind(option)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(option)
        }
    }


    interface OnClickListener {
        fun onClick(option: OptionProperty)
    }
}

class OptionPropertyDiffCallBack : DiffUtil.ItemCallback<OptionProperty>() {
    override fun areItemsTheSame(
        oldItem: OptionProperty,
        newItem: OptionProperty
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: OptionProperty,
        newItem: OptionProperty
    ): Boolean {
        return (oldItem.id == newItem.id &&
                oldItem.title == newItem.title)
    }
}