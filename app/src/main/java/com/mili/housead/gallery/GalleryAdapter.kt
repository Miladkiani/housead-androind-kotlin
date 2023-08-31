package com.mili.housead.gallery

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.mili.housead.R
import com.mili.housead.databinding.ListItemGalleryBinding
import com.mili.housead.network.model.HouseImageProperty

class GalleryAdapter():ListAdapter<HouseImageProperty,GalleryAdapter.ViewHolder>(GalleryDiffCallback()){

    class ViewHolder private constructor(val binding:ListItemGalleryBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(imgUrl: String){
            binding.imgUrl = imgUrl
            binding.executePendingBindings()
        }
      companion object {
          fun from(parent: ViewGroup):ViewHolder {
              val layoutInflater = LayoutInflater.from(parent.context)
              val binding =
                  ListItemGalleryBinding.inflate(layoutInflater,parent,false)
              return  ViewHolder(binding)
          }
      }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder.from(parent)
    }

    override fun submitList(list: MutableList<HouseImageProperty>?) {
        super.submitList(list?.let{
            ArrayList(it)
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = getItem(position)
        holder.bind(imageUrl.original)
    }
}

class GalleryDiffCallback:DiffUtil.ItemCallback<HouseImageProperty>(){
    override fun areItemsTheSame(oldItem: HouseImageProperty, newItem: HouseImageProperty): Boolean {
        return oldItem == newItem;
    }

    override fun areContentsTheSame(oldItem: HouseImageProperty, newItem: HouseImageProperty): Boolean {
        return oldItem == newItem;
    }

}