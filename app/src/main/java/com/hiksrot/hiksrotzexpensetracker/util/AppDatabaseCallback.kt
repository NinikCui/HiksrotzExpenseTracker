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
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id) VALUES (1, 'Nasi Goreng', 25000, '2025-06-14', 1)")
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id) VALUES (2, 'Naik Gojek', 18000, '2025-06-14', 2)")
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id) VALUES (3, 'Ayam Geprek', 28000, '2025-06-13', 3)")
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id) VALUES (4, 'Boba Drink', 22000, '2025-06-13', 4)")
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id) VALUES (5, 'Nonton Bioskop', 50000, '2025-06-12', 5)")
    }
}
