package com.example.myrecipebook.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import okhttp3.internal.Version

class DBManager(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    companion object
    {
        const val DATABASE_VERSION =1
        const val DATABASE_NAME= "Comments.db"

        private const val  SQL_CREATE_TABLE =
            "CREATE TABLE TASK (" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "RECIPE_ID INTEGER," +
                    "COMMENT TEXT)"

        private const val SQL_DELETE_TABLE= "DROP TABLE IF EXISTS TASK"

    }
    override fun onCreate(db: SQLiteDatabase?) {

            db?.execSQL(SQL_CREATE_TABLE)





    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_TABLE)
        db?.execSQL(SQL_CREATE_TABLE)
    }


}