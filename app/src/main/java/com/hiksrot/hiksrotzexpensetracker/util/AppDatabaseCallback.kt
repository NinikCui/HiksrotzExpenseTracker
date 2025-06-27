package com.hiksrot.hiksrotzexpensetracker.util

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class AppDatabaseCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // ==== Insert User Dummy ====
        db.execSQL("INSERT INTO user (id, username, password, first_name, last_name) VALUES (1, 'ariel', 'pass123', 'Ariel', 'Manek')")
        db.execSQL("INSERT INTO user (id, username, password, first_name, last_name) VALUES (2, 'nico', 'pass123', 'Nico', 'Saputra')")
        db.execSQL("INSERT INTO user (id, username, password, first_name, last_name) VALUES (3, 'nadya', 'pass123', 'Nadya', 'Eliana')")

        // ==== Insert Budget Dummy ====
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at) VALUES (1, 'Makan', 500000, 1, 1718300000)")
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at) VALUES (2, 'Transport', 300000, 1, 1718300001)")
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at) VALUES (3, 'Makan', 600000, 2, 1718300002)")
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at) VALUES (4, 'Jajan', 200000, 2, 1718300003)")
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at) VALUES (5, 'Hiburan', 400000, 3, 1718300004)")

        // ==== Insert Expense Dummy ====
        // 2025-06-14 00:00 UTC → 1749859200000
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (1, 'Nasi Goreng', 25000, 1749859200000, 1, 1)")
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (2, 'Naik Gojek', 18000, 1749859200000, 2, 1)")

// 2025-06-13 00:00 UTC → 1749772800000
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (3, 'Ayam Geprek', 28000, 1749772800000, 3, 2)")
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (4, 'Boba Drink', 22000, 1749772800000, 4, 2)")

// 2025-06-12 00:00 UTC → 1749686400000
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (5, 'Nonton Bioskop', 50000, 1749686400000, 5, 3)")    }
}
