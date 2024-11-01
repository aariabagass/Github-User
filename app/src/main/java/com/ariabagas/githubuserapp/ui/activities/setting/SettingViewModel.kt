package com.ariabagas.githubuserapp.ui.activities.setting

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ariabagas.githubuserapp.themes.SettingPreferences
import kotlinx.coroutines.launch

class SettingViewModel(application: Application) : ViewModel() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val pref: SettingPreferences = SettingPreferences.getInstance(application.dataStore)

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkMode)
        }
    }
}