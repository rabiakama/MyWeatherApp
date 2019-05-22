package com.example.myweather.city

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class CityHelper(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME, factory,
        DATABASE_VERSION
    ) {

    var dbhandler: SQLiteOpenHelper? = null
    var db: SQLiteDatabase? = null

    companion object {
        private val DATABASE_VERSION = 2
        private val DATABASE_NAME = "city.db"
        val TABLE_CITY = "CITY"

        val _ID = "_id"
        val TABLE_NAME = "city"
        val COLUMN_CITYID = "city_id"
        val COLUMN_CITY_NAME = "name"
        val COLUMN_COORD_LAT = "lat"
        val COLUMN_COORD_LONG = "long"
        val CONTENT_AUTHORITY = "com.example.myweather.city"
    }
    fun open() {
        Log.i(TABLE_CITY, "Database Opened")
        db = dbhandler?.writableDatabase
    }

    override fun close() {
        Log.i(TABLE_CITY, "Database Closed")
        dbhandler?.close()
    }
    override fun onCreate(db: SQLiteDatabase?) {

        val SQL_CREATE_CITY_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CITYID + " INTEGER, " +
                COLUMN_CITY_NAME + " TEXT NOT NULL, " +
                COLUMN_COORD_LAT + " REAL NOT NULL, " +
                COLUMN_COORD_LONG + " REAL NOT NULL " +


                ");"
        if (db != null) {
            db.execSQL(SQL_CREATE_CITY_TABLE)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME)
        }
        onCreate(db)
    }

    fun addCity(city: CityDetail) {
        val db = this.writableDatabase

        val values= ContentValues()
        values.put(COLUMN_CITYID,city.getId())
        values.put(COLUMN_CITY_NAME,city.getName())
        values.put(COLUMN_COORD_LAT,city.getLat())
        values.put(COLUMN_COORD_LONG,city.getLon())
        //values.put(COLUMN_TEMP,city.)

        db.insert(TABLE_NAME,"",values)
        db.close()
    }

    fun deleteCity(id:Int){
        val db=this.writableDatabase
        db.delete(
            TABLE_NAME,
            COLUMN_CITYID + "=" + id,
            null
        )
    }

    fun getAddAll():ArrayList<CityDetail>{
        val columns= arrayOf(
            _ID,
            COLUMN_CITYID,
            COLUMN_CITY_NAME,
            COLUMN_COORD_LAT,
            COLUMN_COORD_LONG
        )
        val sortOrder:String= _ID + " " +"ASC"
        val cityList:ArrayList<CityDetail> = arrayListOf()
        val db=this.readableDatabase
        val cursor=db.query(
            TABLE_NAME,
            columns,
            null,null,null,null,
            sortOrder
        )
        if(cursor.moveToFirst()){
            do{
                val city=CityDetail()
                city.setId((cursor.getString(cursor.getColumnIndex(COLUMN_CITYID))))
                city.setName(cursor.getString(cursor.getColumnIndex(COLUMN_CITY_NAME)))
                city.setLat(cursor.getFloat(cursor.getColumnIndex(COLUMN_COORD_LAT)).toString())
                city.setLon(cursor.getFloat(cursor.getColumnIndex(COLUMN_COORD_LONG)).toString())

                cityList.add(city)
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return cityList
    }

}