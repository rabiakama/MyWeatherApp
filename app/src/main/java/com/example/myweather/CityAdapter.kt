package com.example.myweather

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.example.myweather.data.JsonData

class CityAdapter(private val context:Context,private var cityList: ArrayList<JsonData>) : RecyclerView.Adapter<CityAdapter.ViewHolder>(),Filterable {

    //private var cityList:ArrayList<CityList> = arrayListOf()
    private var city:List<JsonData>?=null



    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) {
                    cityList = city as ArrayList<JsonData>
                } else {
                    val filteredList = ArrayList<JsonData>()
                    for (row in city!!) {

                            filteredList.add(row)

                    }
                    cityList = filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = cityList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, filterResult: FilterResults) {
                cityList = filterResult.values as ArrayList<JsonData>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): CityAdapter.ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    fun setCity(cty: ArrayList<JsonData>) {
        cityList.clear()
        this.cityList.addAll(cty)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CityAdapter.ViewHolder, position: Int) {
        val city=cityList[position]
        holder.bindTo(city)

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val mTextViewCreator by lazy { itemView.findViewById<TextView>(R.id.cityName) }
        val checkBox by lazy { itemView.findViewById<TextView>(R.id.checkboxCity) }


        fun bindTo(city: JsonData) {

            mTextViewCreator.text= city.getCity().toString()
        }




    }
}