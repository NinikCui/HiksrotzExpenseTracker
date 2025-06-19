package com.hiksrot.hiksrotzexpensetracker.util

import android.content.Context
import com.hiksrot.hiksrotzexpensetracker.model.database.AppDatabase

fun buildDb(context: Context): AppDatabase {
    val db = AppDatabase.buildDatabase(context)
    return db
}