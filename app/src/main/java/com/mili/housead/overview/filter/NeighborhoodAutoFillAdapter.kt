package com.mili.housead.overview.filter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.mili.housead.R
import com.mili.housead.network.model.NeighborhoodProperty

class NeighborhoodAutoFillAdapter(val context: Context):BaseAdapter(),Filterable{
    private var neighborhoods:List<NeighborhoodProperty> = ArrayList()
    private var neighborhoodsFilter:List<NeighborhoodProperty> = ArrayList()
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: TextView =
            convertView as TextView? ?: LayoutInflater.from(context).inflate(R.layout.simple_drop_down, parent, false) as TextView
        view.text = neighborhoodsFilter[position].title
        return view
    }

    override fun getItem(position: Int): Any {
        return neighborhoodsFilter[position]
    }

    override fun getItemId(position: Int): Long {
        return  neighborhoodsFilter[position].id.toLong()
    }

    override fun getCount(): Int {
        return  neighborhoodsFilter.size
    }

    fun setNeighborhoods(neighborhoods:List<NeighborhoodProperty>){
        this.neighborhoods = neighborhoods
        this.neighborhoodsFilter = this.neighborhoods
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return   object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()?.toLowerCase()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    neighborhoods
                else
                    neighborhoods.filter {
                        it.title.contains(queryString)
                    }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                neighborhoodsFilter = results.values as List<NeighborhoodProperty>
                notifyDataSetChanged()
            }
        }
    }
}