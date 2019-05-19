package com.example.myweather.data

import android.app.SearchManager
import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.myweather.CityAdapter
import com.example.myweather.R
import com.android.volley.Response
import kotlinx.android.synthetic.main.activity_city.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.InputStream

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken





class CityActivity : AppCompatActivity() {

    private var cityArray:ArrayList<City>?=null
    private var cityAdapter:CityAdapter?=null
    private lateinit var searchView:SearchView
    private var postList=StringBuffer()

    private var lat: String? = null
    private var lon: String? = null

    private var name: String? = null
    private var id: String? = null
    private var lang: String? = null

    private var mRequestQueue:RequestQueue?=null
    private var mExampleList:ArrayList<JsonData> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city)

        city_recycler.setHasFixedSize(true)
        city_recycler.layoutManager=LinearLayoutManager(this)
        //readFromLocal()
        getAllCity()
        //mRequestQueue = Volley.newRequestQueue(this)
       // parseJson()





    }

    private fun parseJson() {

            val url = "https://api.myjson.com/bins/jtttq"
            //val data: JsonData = gson.fromJson(this.parseJson(),JsonData::class.java)
            val request = JsonObjectRequest(Request.Method.POST,url,null,
                Response.Listener { response ->
                    // Process the json
                    try {
                        val jsonArray=response.getJSONArray("cityDetail")
                        for ( i in  0..jsonArray.length() ){
                            val cdetail=jsonArray.getJSONObject(i)
                            val creatorName=cdetail.getString("name")

                           // mExampleList.add(CityList(creatorName))

                        }

                        cityAdapter=CityAdapter(this,mExampleList)
                        city_recycler.adapter=cityAdapter

                    }catch (e:JSONException){
                        e.printStackTrace()
                    }

                }, Response.ErrorListener{
                    // Error in request

                })
        mRequestQueue!!.add(request)
    }


    private fun readFromLocal(): ArrayList<JsonData> {
        val loclist : ArrayList<CityList> = arrayListOf()
        var json= String()
        val context:Context?=null

        try {

            val inputStream: InputStream = context!!.assets.open("document.json")
            val size= inputStream.available()
            val buffer=ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            //json=inputStream.buffered().use { it.readText() }

        }catch (ex:Exception){
            ex.printStackTrace()
        }
        try {
            val obj =  JSONObject(json)
            val m_jArry: JSONArray = obj.getJSONArray("cityDetail")
            for ( i in  0..m_jArry.length() ){
                val cdetail=m_jArry.getJSONObject(i)
                val creatorName=cdetail.getString("name")

                mExampleList.clear()
                mExampleList.add(JsonData(creatorName))

            }
            cityAdapter=CityAdapter(this,mExampleList)
            city_recycler.adapter=cityAdapter

        }catch (e:JSONException){
            e.printStackTrace()
        }
        return mExampleList

    }


    private fun readJsonFromAsset():String{
        var json: String?
        try {
            val inputStream:InputStream = assets.open("yourFile.json")
            json = inputStream.bufferedReader().use{it.readText()}
            val size= inputStream.available()
            val buffer=ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()



        }catch (ex:Exception){
            ex.printStackTrace()
            return null.toString()
        }
        return json

    }

    fun getAllCity() {
        class FavoritesTask : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                val listType = object : TypeToken<ArrayList<JsonData>>() {

                }.type
                mExampleList = GsonBuilder().create().fromJson(readJsonFromAsset(), listType)
                postList = StringBuffer()
                val obj =  JSONObject()
                val m_jArry: JSONArray = obj.getJSONArray("cityDetail")
                for (i in 0..mExampleList.size) {
                    val cdetail=m_jArry.getJSONObject(i)
                    val creatorName=cdetail.getString("name")
                }
                return null
            }


        }

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Int.MAX_VALUE


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //moviesAdapter?.filter?.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                cityAdapter?.filter?.filter(newText)
                cityAdapter?.notifyDataSetChanged()
                return true

            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return if (id == R.id.action_search) {
            true
        } else
            return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!searchView.isIconified) {
            searchView.isIconified=true
        }else
            super.onBackPressed()
    }


   /* private fun getCurrentData(latitude: String, longitude: String,lang:String,name:String,id:String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(GoogleServiceApi::class.java)
        val call = service.getPlaces(latitude, longitude, BuildConfig.API_KEY,lang,id,name)
        call.enqueue(object : Callback<CityResponse> {
            override fun onFailure(call: Call<CityResponse>, t: Throwable) {
                Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<CityResponse>, response: Response<CityResponse>) {
                if (response.code() == 200) {
                    if (response.isSuccessful && response.body() != null) {
                        cityArray!!.addAll(response.body()!!.results)
                        cityAdapter= CityAdapter(cityArray!!)
                        city_recycler.adapter=cityAdapter
                        city_recycler.smoothScrollToPosition(0)

                        val weatherResponse = response.body()

                    weatherResponse?.let {
                        with(it) {
                            val stringBuilder = "Country: " +
                                    sys?.country +
                                    "\n" +
                                    "City: " +
                                    name

                        }
                    }
                }
            }
        }
    })
    }*/



}
