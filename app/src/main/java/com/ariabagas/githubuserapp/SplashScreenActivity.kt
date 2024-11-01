package com.ariabagas.githubuserapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.ariabagas.githubuserapp.databinding.ActivitySplashScreenBinding
import com.ariabagas.githubuserapp.themes.ViewModelFactory
import com.ariabagas.githubuserapp.ui.activities.main.MainActivity
import com.ariabagas.githubuserapp.ui.activities.main.MainViewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var bindingSplash: ActivitySplashScreenBinding
    private val delayMillis = 2000L
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSplash = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(bindingSplash.root)
        viewModel = getViewModel(this)
        goHome()
        themeHandler()
    }

    private fun themeHandler() {
        viewModel.getThemeSettings().observe(this, { darkMode ->
            if (darkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity): MainViewModel {
        val viewModelFactory = ViewModelFactory.getInstance(appCompatActivity.application)
        return ViewModelProvider(appCompatActivity, viewModelFactory)[MainViewModel::class.java]
    }

    private fun goHome() {

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, delayMillis)
    }
}