package com.ariabagas.githubuserapp.themes

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ariabagas.githubuserapp.ui.activities.detail.DetailViewModel
import com.ariabagas.githubuserapp.ui.activities.detail.FollowersViewModel
import com.ariabagas.githubuserapp.ui.activities.detail.FollowingViewModel
import com.ariabagas.githubuserapp.ui.activities.favorites.FavoriteViewModel
import com.ariabagas.githubuserapp.ui.activities.main.MainViewModel
import com.ariabagas.githubuserapp.ui.activities.setting.SettingViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory constructor(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(application) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(application) as T
            }

            modelClass.isAssignableFrom(FollowersViewModel::class.java) -> {
                FollowersViewModel(application) as T
            }

            modelClass.isAssignableFrom(FollowingViewModel::class.java) -> {
                FollowingViewModel(application) as T
            }

            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(application) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(application) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}