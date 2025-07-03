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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ProfileViewModel (application: Application) : AndroidViewModel(application), CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO



    private val userDao: UserDao = buildDb(application).userDao()
    private val sharedPreferences = application.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    private val _userLD = MutableLiveData<UserEntity?>()
    val userLD: MutableLiveData<UserEntity?> = _userLD

    private val _changePasswordResult = MutableLiveData<String>()
    val changePasswordResult: LiveData<String> = _changePasswordResult

    private val _signOutResult = MutableLiveData<Boolean>()
    val signOutResult: LiveData<Boolean> = _signOutResult



     fun loadUserData() {
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

    val oldPassword = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val repeatPassword = MutableLiveData<String>()

    private fun hashPassword(password: String): String {
        val digest = java.security.MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray(Charsets.UTF_8))
        return hash.joinToString("") { "%02x".format(it) }
    }

    fun changePassword() {
        val oldPass = oldPassword.value ?: ""
        val newPass = newPassword.value ?: ""
        val repeatPass = repeatPassword.value ?: ""

        if (oldPass.isBlank() || newPass.isBlank() || repeatPass.isBlank()) {
            _changePasswordResult.postValue("Semua kolom harus diisi")
            return
        }

        if (newPass != repeatPass) {
            _changePasswordResult.postValue("Password baru tidak cocok")
            return
        }

        val username = sharedPreferences.getString("username", "") ?: return
        launch {
            val user = userDao.getUserByUsername(username)
            if (user != null) {
                val hashedOld = hashPassword(oldPass)
                if (hashedOld == user.password) {
                    user.password = hashPassword(newPass)
                    userDao.updateUser(user)
                    _changePasswordResult.postValue("Password changed successfully")
                } else {
                    _changePasswordResult.postValue("Password lama salah")
                }
            } else {
                _changePasswordResult.postValue("User tidak ditemukan")
            }
        }
    }


    fun signOut() {
        sharedPreferences.edit().clear().apply()
        _userLD.value =null
        _signOutResult.value = true
    }

}