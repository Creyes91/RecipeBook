package com.example.myrecipebook.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.myrecipebook.data.CommentTask


class CommentsDAO(var context: Context) {
    lateinit var db: SQLiteDatabase

    private fun open() {
        db = DBManager(context).writableDatabase

    }

    private fun close() {

        db.close()
    }

     fun insert(commentTask: CommentTask) {
        open()

        val values = ContentValues().apply {
            put(CommentTask.COLUMN_RECIPE_ID, commentTask.recipeid)
            put(CommentTask.COLUMN_COMMENT, commentTask.comment)

        }
        try {
            val id = db.insert(CommentTask.TABLE_NAME, null, values)
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        } finally {
            close()
        }

    }

    fun findByID(id: Int): List<CommentTask> {
        open()
        var list: MutableList<CommentTask> = mutableListOf()
        val projection = arrayOf(CommentTask.COLUMN_RECIPE_ID, CommentTask.COLUMN_COMMENT)

        try {
            val cursor = db.query(
                CommentTask.TABLE_NAME,
                projection,
                "${CommentTask.COLUMN_RECIPE_ID}=$id",
                null,
                null,
                null,
                null)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(CommentTask.COLUMN_RECIPE_ID))
                val comment =
                    cursor.getString(cursor.getColumnIndexOrThrow(CommentTask.COLUMN_COMMENT))


                val task = CommentTask(id, comment)
                list.add(task)
            }
        } catch (e: Exception) {
            Log.e("DB", e.stackTraceToString())
        }
        return list
    }



}