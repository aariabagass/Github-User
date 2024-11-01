package com.ariabagas.githubuserapp.ui.activities.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.ariabagas.githubuserapp.models.FavoriteUser
import com.ariabagas.githubuserapp.database.FavoriteUserDao
import com.ariabagas.githubuserapp.database.UserDB

class FavoriteViewModel(application: Application): ViewModel() {

    private val userDao: FavoriteUserDao
    private val userDb: UserDB

    init {
        userDb = UserDB.getDatabase(application)!!
        userDao = userDb.favoriteUserDao()
    }

    fun getListFavoriteUser(): LiveData<List<FavoriteUser>> {
        return userDao.getListFavoriteUser()
    }
}