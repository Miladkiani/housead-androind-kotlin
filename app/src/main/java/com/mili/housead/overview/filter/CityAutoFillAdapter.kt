package com.mili.housead.overview.filter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mili.housead.R
import com.mili.housead.network.model.CityProperty

class CityAutoFillAdapter(val context: Context):
    BaseAdapter(),Filterable {
    private var allCities:List<CityProperty> = ArrayList()
    private var citiesFilter:List<CityProperty> = allCities
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: TextView =
            convertView as TextView? ?: LayoutInflater.from(context)
                .inflate(R.layout.simple_drop_down, parent, false) as TextView
        view.text = citiesFilter[position].name
        return view
    }

    override fun getItem(position: Int): Any {
        return citiesFilter[position]
    }

    override fun getItemId(position: Int): Long {
       return citiesFilter[position].id.toLong()
    }

    override fun getCount(): Int {
        return citiesFilter.size
    }

    fun setCities(cities:List<CityProperty>){
        this.allCities = cities
        this.citiesFilter = this.allCities
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return   object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val queryString = constraint?.toString()

                val filterResults = Filter.FilterResults()
                filterResults.values = if (queryString==null || queryString.isEmpty())
                    allCities
                else
                    allCities.filter {
                        it.name.contains(queryString)
                    }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                 citiesFilter = results.values as List<CityProperty>
                notifyDataSetChanged()
            }
        }
    }

}