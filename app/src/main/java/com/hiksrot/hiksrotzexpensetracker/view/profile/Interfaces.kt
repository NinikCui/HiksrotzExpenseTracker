package com.hiksrot.hiksrotzexpensetracker.view.profile

import android.view.View

interface EditPassListener {
    fun onEditPasswordClicked(v: View)
    fun btnLogOutClicked(v:View)
}

interface  ProfileListener{
    fun onButtonEditClicked(v:View)
}