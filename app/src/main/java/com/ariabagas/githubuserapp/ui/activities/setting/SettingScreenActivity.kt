package com.ariabagas.githubuserapp.ui.activities.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.ariabagas.githubuserapp.R
import com.ariabagas.githubuserapp.databinding.ActivitySettingScreenBinding
import com.ariabagas.githubuserapp.themes.ViewModelFactory
import com.ariabagas.githubuserapp.ui.activities.favorites.FavoriteScreenActivity
import com.ariabagas.githubuserapp.ui.activities.main.MainActivity

class SettingScreenActivity : AppCompatActivity() {
    private lateinit var bindingSetting: ActivitySettingScreenBinding
    private lateinit var viewModel: SettingViewModel
    private var pressedTime: Long = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingSetting = ActivitySettingScreenBinding.inflate(layoutInflater)
        setContentView(bindingSetting.root)
        setupView()
        switchTheme()
    }

    private fun setupView() {
        bindingSetting.bottomNavigation.selectedItemId = R.id.menu_setting
        bindingSetting.bottomNavigation.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.menu_home -> {
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.menu_favorite -> {
                    Intent(this, FavoriteScreenActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.menu_setting -> {
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        viewModel = getViewModel(this)
    }

    private fun switchTheme() {
        bindingSetting.switchTheme.apply {
            viewModel.getThemeSettings().observe(this@SettingScreenActivity, {
                if (it) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    this.isChecked = true
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    this.isChecked = false
                }
            })
            setOnCheckedChangeListener { _: CompoundButton?, isCheck: Boolean ->
                viewModel.saveThemeSetting(isCheck)
            }
        }
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity): SettingViewModel {
        val viewModelFactory = ViewModelFactory.getInstance(appCompatActivity.application)
        return ViewModelProvider(appCompatActivity, viewModelFactory)[SettingViewModel::class.java]
    }

    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            finish()
        } else {
            Toast.makeText(baseContext, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
        pressedTime = System.currentTimeMillis()
    }
}