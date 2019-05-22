package com.example.myweather.city

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Filter
import android.widget.Filterable
import com.example.myweather.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.city_item.*
import kotlinx.android.synthetic.main.city_item.view.*

class CityAdapter(val itemCheckedListener: CompoundButton.OnCheckedChangeListener): RecyclerView.Adapter<CityAdapter.ViewHolder>(),Filterable {

    private var citiesFiltered = listOf<CityDetail>()
    var cities = mutableListOf<CityDetail>()
        set(value) {
            field.clear()
            field.addAll(value)

            citiesFiltered = cities

            notifyDataSetChanged()
        }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val brandName = charSequence.toString()
                if (brandName.isEmpty()) {
                    citiesFiltered = cities
                } else {
                    val filteredList = ArrayList<CityDetail>()
                    for (city in cities) {
                        if (city.name.toString().toLowerCase().contains(brandName.toLowerCase())) {
                            filteredList.add(city)
                        }

                    }
                    citiesFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = citiesFiltered
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                citiesFiltered = filterResults.values as ArrayList<CityDetail>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return citiesFiltered.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = citiesFiltered[position]
        holder.bindTo(city,itemCheckedListener)
    }

    inner class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bindTo(city: CityDetail,clickListener: CompoundButton.OnCheckedChangeListener) {
            cityName.text = city.name
            containerView.checkboxCity.setOnCheckedChangeListener { buttonView, isChecked ->
                clickListener.onCheckedChanged(checkboxCity,true)
            }

        }

    }
    interface OnItemCheckListener{
        fun onItemChecked(city:CityDetail)
    }
}