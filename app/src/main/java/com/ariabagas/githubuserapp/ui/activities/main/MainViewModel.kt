package com.ariabagas.githubuserapp.ui.activities.main

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ariabagas.githubuserapp.models.UserResponse
import com.ariabagas.githubuserapp.models.UserModel
import com.ariabagas.githubuserapp.themes.SettingPreferences
import com.ariabagas.githubuserapp.utils.services.API
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(application: Application) : ViewModel() {
    val listUser = MutableLiveData<ArrayList<UserModel>>()

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val pref: SettingPreferences = SettingPreferences.getInstance(application.dataStore)

    fun setSearchQuery(query: String) {
        API.apiRequest
            .getSearchUsers(query)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.code() == 200) {
                        Log.d("FYZ", "Response Success : ${response.body()}")
                        if (response.isSuccessful) {
                            listUser.postValue(response.body()?.items)
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponse>, e: Throwable) {
                    Log.e("FYZ", "Response Error : $e.message")
                }
            })
    }

    fun getSearchQuery(): LiveData<ArrayList<UserModel>> = listUser

    fun getThemeSettings(): LiveData<Boolean> = pref.getThemeSetting().asLiveData()
}