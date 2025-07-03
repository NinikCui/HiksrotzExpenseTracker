package com.hiksrot.hiksrotzexpensetracker.viewmodel

import android.app.Application
import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hiksrot.hiksrotzexpensetracker.R
import com.hiksrot.hiksrotzexpensetracker.model.entities.UserEntity
import com.hiksrot.hiksrotzexpensetracker.model.daos.UserDao
import com.hiksrot.hiksrotzexpensetracker.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao: UserDao = buildDb(application).userDao()
    private val sharedPreferences = application.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

    private val _userLD = MutableLiveData<UserEntity>()
    val userLD: LiveData<UserEntity> = _userLD

    private val _changePasswordResult = MutableLiveData<String>()
    val changePasswordResult: LiveData<String> = _changePasswordResult

    private val _signOutResult = MutableLiveData<Boolean>()
    val signOutResult: LiveData<Boolean> = _signOutResult

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val username = sharedPreferences.getString("username", "") ?: ""
        if (username.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                val user = userDao.getUserByUsername(username)
                user?.let {
                    _userLD.postValue(it)
                }
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String, repeatPassword: String) {
        when {
            oldPassword.isEmpty() -> {
                _changePasswordResult.value = "Old password cannot be empty"
                return
            }
            newPassword.isEmpty() -> {
                _changePasswordResult.value = "New password cannot be empty"
                return
            }
            repeatPassword.isEmpty() -> {
                _changePasswordResult.value = "Repeat password cannot be empty"
                return
            }
            newPassword != repeatPassword -> {
                _changePasswordResult.value = "New password and repeat password don't match"
                return
            }
            newPassword.length < 6 -> {
                _changePasswordResult.value = "New password must be at least 6 characters"
                return
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val currentUser = _userLD.value
            if (currentUser != null) {
                if (currentUser.password == oldPassword) {
                    userDao.updateUser(
                        currentUser.id,
                        currentUser.username,
                        newPassword,
                        currentUser.firstName,
                        currentUser.lastName
                    )

                    // Update SharedPreferences
                    sharedPreferences.edit().apply {
                        putString("password", newPassword)
                        apply()
                    }

                    // Update current user data
                    currentUser.password = newPassword
//                    ERROR
                    _userLD.postValue(currentUser)
                    _changePasswordResult.postValue("Password changed successfully")
                } else {
                    _changePasswordResult.postValue("Old password is incorrect")
                }
            }
        }
    }

//    ERROR
    fun signOut() {
        sharedPreferences.edit().clear().apply()
        _userLD.value = null
        _signOutResult.value = true
    }

    fun togglePasswordVisibility(editText: EditText, toggleIcon: ImageView) {
        val isPasswordVisible = editText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

        if (isPasswordVisible) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            toggleIcon.setImageResource(android.R.drawable.ic_menu_view)
        } else {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            toggleIcon.setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
        }

        editText.setSelection(editText.text.length)
    }
}