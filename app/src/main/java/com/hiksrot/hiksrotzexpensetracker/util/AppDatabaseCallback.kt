package com.hiksrot.hiksrotzexpensetracker.util

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class AppDatabaseCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        // ==== Insert User Dummy ====
        db.execSQL("INSERT INTO user (id, username, password, first_name, last_name) VALUES (1, 'ariel', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Ariel', 'Manek')")
        db.execSQL("INSERT INTO user (id, username, password, first_name, last_name) VALUES (2, 'nico', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Nico', 'Saputra')")
        db.execSQL("INSERT INTO user (id, username, password, first_name, last_name) VALUES (3, 'nadya', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Nadya', 'Eliana')")

        // ==== Insert Budget Dummy ====
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at, month, year) VALUES (1, 'Makan', 500000, 1, 1718300000, 7, 2025)")
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at, month, year) VALUES (2, 'Transport', 300000, 1, 1718300001, 7, 2025)")
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at, month, year) VALUES (3, 'Makan', 600000, 2, 1718300002, 7, 2025)")
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at, month, year) VALUES (4, 'Jajan', 200000, 2, 1718300003, 7, 2025)")
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at, month, year) VALUES (5, 'Hiburan', 400000, 3, 1718300004, 7, 2025)")

        // ==== Insert Expense Dummy ====
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (1, 'Nasi Goreng enak pedis pedas uyeaahhhhhhhhhhhhhhhhhhhh', 25000, 1749988800000, 1, 1)")
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (6, 'Beli Es Teh Jumbo', 50000, 1749996000000, 2, 1)")
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (2, 'Naik Gojek ngiiiiihhaaaaaaa', 18000, 1749992400000, 2, 1)")

        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (3, 'Ayam Geprek', 28000, 1749772800000, 3, 2)")
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (4, 'Boba Drink', 22000, 1749772800000, 4, 2)")

        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (5, 'Nonton Bioskop', 50000, 1749686400000, 5, 3)")

        // data tambahan bulan 6
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at, month, year) VALUES (6, 'Internet', 250000, 1, 1718300100, 6, 2025)")
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at, month, year) VALUES (7, 'Kopi', 150000, 1, 1718300200, 6, 2025)")

        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (7, 'Bayar Wifi Kost', 120000, 1719936000000, 6, 1)")
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (8, 'Beli Paket Data', 30000, 1720022400000, 6, 1)")

        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (9, 'Ngopi di Starbucks', 60000, 1719936000000, 7, 1)")
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (10, 'Beli Kopi Literan', 50000, 1720022400000, 7, 1)")

        // data tambahan bulan 7
        db.execSQL("INSERT INTO budgets (id, name, amount, user_id, created_at, month, year) VALUES (8, 'Buku', 100000, 1, 1718300300, 5, 2025)")

        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (11, 'Beli Novel', 60000, 1717267200000, 8, 1)")
        db.execSQL("INSERT INTO expenses (id, description, amount, date, budget_id, user_id) VALUES (12, 'Beli Buku Kuliah', 30000, 1717353600000, 8, 1)")


    }
}
