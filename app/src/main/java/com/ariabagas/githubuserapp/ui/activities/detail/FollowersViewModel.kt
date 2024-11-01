package com.ariabagas.githubuserapp.ui.activities.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ariabagas.githubuserapp.models.UserModel
import com.ariabagas.githubuserapp.utils.services.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel(application: Application) : ViewModel() {
    val listFollower = MutableLiveData<ArrayList<UserModel>>()

    fun setListFollower(query: String) {
        API.apiRequest
            .getUserFollowers(query)
            .enqueue(object : Callback<ArrayList<UserModel>> {
                override fun onResponse(
                    call: Call<ArrayList<UserModel>>,
                    response: Response<ArrayList<UserModel>>
                ) {
                    if (response.code() == 200) {
                        Log.d("FYZ", "Response Success : ${response.body()}")
                        if (response.isSuccessful) {
                            listFollower.postValue(response.body())
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserModel>>, e: Throwable) {
                    Log.e("FYZ", "Response Error : $e.message")
                }

            })
    }

    fun getListfollower(): LiveData<ArrayList<UserModel>> = listFollower
}

