package com.ariabagas.githubuserapp.ui.activities.favorites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariabagas.githubuserapp.R
import com.ariabagas.githubuserapp.models.FavoriteUser
import com.ariabagas.githubuserapp.databinding.ActivityFavoriteScreenBinding
import com.ariabagas.githubuserapp.models.UserModel
import com.ariabagas.githubuserapp.themes.ViewModelFactory
import com.ariabagas.githubuserapp.ui.activities.detail.DetailScreenActivity
import com.ariabagas.githubuserapp.ui.activities.main.MainActivity
import com.ariabagas.githubuserapp.ui.activities.setting.SettingScreenActivity
import com.ariabagas.githubuserapp.ui.adapters.UserListAdapter

class FavoriteScreenActivity : AppCompatActivity() {
    private lateinit var bindingFavorite: ActivityFavoriteScreenBinding
    private lateinit var userAdapter: UserListAdapter
    private lateinit var viewModel: FavoriteViewModel
    private var pressedTime: Long = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingFavorite = ActivityFavoriteScreenBinding.inflate(layoutInflater)
        setContentView(bindingFavorite.root)
        setupView()
    }

    private fun setupView() {
        bindingFavorite.bottomNavigation.selectedItemId = R.id.menu_favorite

        bindingFavorite.bottomNavigation.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.menu_home -> {
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.menu_favorite -> {
                    return@setOnItemSelectedListener true
                }
                R.id.menu_setting -> {
                    Intent(this, SettingScreenActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
        userAdapter = UserListAdapter()

        viewModel = getViewModel(this)

        userAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                Intent(this@FavoriteScreenActivity, DetailScreenActivity::class.java).also {
                    it.putExtra(DetailScreenActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailScreenActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailScreenActivity.EXTRA_AVATAR, data.avatar_url)
                    startActivity(it)
                }
            }

        })
        bindingFavorite.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(this@FavoriteScreenActivity)
            rvUser.adapter = userAdapter
        }
        viewModel.getListFavoriteUser().observe(this, {
            if (it != null) {
                val list = listFav(it)
                userAdapter.setData(list)
                val dataCount: Int = userAdapter.itemCount
                if (dataCount == 0) isFound(false) else isFound(true)
            }
        })
    }

    private fun isFound(s: Boolean) {
        if (s) {
            bindingFavorite.ivFavoritesNot.visibility = View.GONE
            bindingFavorite.tvFavoritesNot.visibility = View.GONE
        } else {
            bindingFavorite.ivFavoritesNot.visibility = View.VISIBLE
            bindingFavorite.tvFavoritesNot.visibility = View.VISIBLE
        }
    }


    private fun getViewModel(appCompatActivity: AppCompatActivity): FavoriteViewModel {
        val viewModelFactory = ViewModelFactory.getInstance(appCompatActivity.application)
        return ViewModelProvider(
            appCompatActivity,
            viewModelFactory
        )[FavoriteViewModel::class.java]

    }

    private fun listFav(it: List<FavoriteUser>): ArrayList<UserModel> {
        val listUsers = ArrayList<UserModel>()
        for (user in it) {
            val item = UserModel(
                user.login,
                user.id,
                user.avatar_url,
            )
            listUsers.add(item)
        }
        return listUsers
    }


    override fun onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            finish()
        } else {
            Toast.makeText(baseContext, "Tekan sekali lagi untuk keluar", Toast.LENGTH_SHORT)
                .show()
        }
        pressedTime = System.currentTimeMillis()
    }
}