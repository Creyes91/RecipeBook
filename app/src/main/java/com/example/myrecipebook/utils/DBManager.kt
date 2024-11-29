package com.example.myrecipebook.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myrecipebook.data.CommentTask
import okhttp3.internal.Version

class DBManager(context: Context): SQLiteOpenHelper(context, DATABASE_NAME,null, DATABASE_VERSION) {

    companion object
    {
        const val DATABASE_VERSION =1
        const val DATABASE_NAME= "Comments.db"

        private const val  SQL_CREATE_TABLE =
            "CREATE TABLE ${CommentTask.TABLE_NAME} (" +
                    "${CommentTask.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${CommentTask.COLUMN_RECIPE_ID} INTEGER," +
                    "${CommentTask.COLUMN_COMMENT} TEXT)"

        private const val SQL_DELETE_TABLE= "DROP TABLE IF EXISTS ${CommentTask.TABLE_NAME}"

    }
    override fun onCreate(db: SQLiteDatabase?) {

            db?.execSQL(SQL_CREATE_TABLE)





    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_TABLE)
        db?.execSQL(SQL_CREATE_TABLE)
    }


}