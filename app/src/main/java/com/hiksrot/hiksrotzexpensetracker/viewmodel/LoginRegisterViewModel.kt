package com.hiksrot.hiksrotzexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hiksrot.hiksrotzexpensetracker.model.entities.UserEntity
import com.hiksrot.hiksrotzexpensetracker.util.buildDb
import kotlinx.coroutines.*
import java.security.MessageDigest
import kotlin.coroutines.CoroutineContext

class LoginRegisterViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO


    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    private val _loggedInUser = MutableLiveData<UserEntity?>()
    val loggedInUser: LiveData<UserEntity?> get() = _loggedInUser

    fun login(username: String, password: String) {
        launch {
            val db = buildDb(getApplication())
            val user = db.userDao().login(username, password)
            _loggedInUser.postValue(user)
            _loginSuccess.postValue(user != null)
        }
    }

    fun fetchUserByUsername(username: String) {
        launch {
            val db = buildDb(getApplication())
            val user = db.userDao().getUserByUsername(username)
            _loggedInUser.postValue(user)
        }
    }


    fun Register(user: UserEntity) {
        launch {
            val db = buildDb(getApplication())
            db.userDao().register(user)
        }
    }


    private val _userCheckResult = MutableLiveData<UserEntity?>()
    val userCheckResult: LiveData<UserEntity?> get() = _userCheckResult

    fun checkIfUsernameExists(username: String) {
        launch {
            val db = buildDb(getApplication())
            val user = db.userDao().getUserByUsername(username)
            _userCheckResult.postValue(user)
        }
    }
    fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
