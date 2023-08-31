package com.mili.housead.detail



import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mili.housead.databinding.ListItemFeatureBinding
import com.mili.housead.network.model.FeatureProperty

class FeaturePropertyAdapter(): ListAdapter<FeatureProperty,FeaturePropertyAdapter.ViewHolder>(FeaturePropertyDiffCallBack())
{
    class ViewHolder private constructor(val binding:ListItemFeatureBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(feature: FeatureProperty){
            binding.feature = feature
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup):ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemFeatureBinding.inflate(layoutInflater,parent,false)
                return ViewHolder(binding)
            }
        }
    }

    override fun submitList(list: MutableList<FeatureProperty>?) {
        super.submitList(list?.let{
            ArrayList(list)
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feature = getItem(position)
        holder.bind(feature)
    }
}

class FeaturePropertyDiffCallBack : DiffUtil.ItemCallback<FeatureProperty>(){
    override fun areItemsTheSame(oldItem: FeatureProperty, newItem: FeatureProperty): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FeatureProperty, newItem: FeatureProperty): Boolean {
        return oldItem == newItem
    }

}