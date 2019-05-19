package com.example.myweather

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.myweather.data.City

class CityHelper(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int)
    :SQLiteOpenHelper(context,
    DATABASE_NAME, factory,
    DATABASE_VERSION) {

    var dbhandler: SQLiteOpenHelper? = null
    var db: SQLiteDatabase? = null

    companion object {

        private val DATABASE_VERSION = 2
        private val DATABASE_NAME = "city.db"
        val TABLE_CITY = "CITY"

         val _ID="_id"
        val TABLE_NAME="city"
        val COLUMN_CITYID = "city_id"
         val COLUMN_CITY_NAME="name"
         val COLUMN_COORD_LAT="lat"
        val COLUMN_COORD_LONG="long"
        val CONTENT_AUTHORITY = "com.example.myweather"

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
                COLUMN_COORD_LONG + " REAL NOT NULL, " +
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


    fun addCity(city: City) {
        val db = this.writableDatabase

        /*val values = ContentValues()
        values.put(COLUMN_CITYID, city.getId())
        values.put(COLUMN_CITY_NAME, city.getName())
        values.put(COLUMN_RELEASE_DATE,movie.getReleaseDate())
        values.put(COLUMN_USERRATING, movie.getVoteAverage().toString())
        values.put(COLUMN_POSTER_PATH, movie.getPosterPath())*/

       // db.insert(TABLE_NAME, "", values)
        db.close()
    }























}