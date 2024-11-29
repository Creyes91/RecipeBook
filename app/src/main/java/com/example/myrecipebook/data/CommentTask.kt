package com.example.myrecipebook.data

class CommentTask(var id: Long ,var recipeid: Long,var comment: String) {

    companion object{

        const val TABLE_NAME = "COMMENTS"
        const val COLUMN_ID = "ID"
        const val COLUMN_RECIPE_ID = "RECIPE_ID"
        const val COLUMN_COMMENT = "COMMENT"

    }
}