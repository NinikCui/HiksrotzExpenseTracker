package com.hiksrot.hiksrotzexpensetracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hiksrot.hiksrotzexpensetracker.model.entities.UserEntity
import com.hiksrot.hiksrotzexpensetracker.util.buildDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class LoginRegisterViewModel(application: Application): AndroidViewModel(application),
    CoroutineScope {
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess


    fun Register(user: UserEntity) {
        launch {
            val db = buildDb(getApplication())

            db.userDao().register(user)
        }
    }

    fun Login(username:String, password:String) {
        launch {
            val db = buildDb(getApplication())

            val user = db.userDao().login(username, password)
            _loginSuccess.postValue(user != null)
        }
    }
    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}