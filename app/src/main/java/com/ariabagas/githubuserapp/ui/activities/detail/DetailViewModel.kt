package com.ariabagas.githubuserapp.ui.activities.detail;

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariabagas.githubuserapp.models.FavoriteUser
import com.ariabagas.githubuserapp.database.FavoriteUserDao
import com.ariabagas.githubuserapp.database.UserDB
import com.ariabagas.githubuserapp.models.UserDetailResponse
import com.ariabagas.githubuserapp.utils.services.API
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    val userDetail = MutableLiveData<UserDetailResponse>()

    private val userDao: FavoriteUserDao
    private val userDb: UserDB

    init {
        userDb = UserDB.getDatabase(application)!!
        userDao = userDb.favoriteUserDao()
    }


    fun setGithubDetail(query: String) {
        API.apiRequest
            .getUserDetail(query)
            .enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    response: Response<UserDetailResponse>
                ) {
                    if (response.code() == 200) {
                        Log.d("FYZ", "Response Success : ${response.body()}")
                        if (response.isSuccessful) {
                            userDetail.postValue(response.body())
                        }
                    }
                }

                override fun onFailure(call: Call<UserDetailResponse>, e: Throwable) {
                    Log.e("FYZ", "Response Error : $e.message")
                }

            })
    }

    fun getGithubDetail(): LiveData<UserDetailResponse> = userDetail

    fun addFavorite(username: String, id: Int, avatar_url: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                username,
                id,
                avatar_url
            )
            userDao.addFavoriteUser(user)
        }
    }

    suspend fun getUser(id: Int) = userDao.getFavoriteUser(id)

    fun removeFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.removeFavoriteUser(id)
        }
    }
}
