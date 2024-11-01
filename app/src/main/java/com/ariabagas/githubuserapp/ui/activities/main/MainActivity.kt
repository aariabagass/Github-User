package com.ariabagas.githubuserapp.ui.activities.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariabagas.githubuserapp.R
import com.ariabagas.githubuserapp.ui.adapters.UserListAdapter
import com.ariabagas.githubuserapp.databinding.ActivityMainBinding
import com.ariabagas.githubuserapp.models.UserModel
import com.ariabagas.githubuserapp.ui.activities.detail.DetailScreenActivity
import com.ariabagas.githubuserapp.ui.activities.favorites.FavoriteScreenActivity
import com.ariabagas.githubuserapp.ui.activities.setting.SettingScreenActivity
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.ariabagas.githubuserapp.themes.ViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var bindingMain: ActivityMainBinding
    private lateinit var userAdapter: UserListAdapter
    private lateinit var viewModel: MainViewModel
    private var pressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        searchViewClick()
        setupView()
        themeHandler()
    }

    private fun searchViewClick() {
        bindingMain.svSearch.setIconifiedByDefault(false)
        bindingMain.svSearch.setOnClickListener(View.OnClickListener {
            bindingMain.svSearch.isIconified = false
        })
    }

    private fun setupView() {
        bindingMain.bottomNavigation.selectedItemId = R.id.menu_home

        bindingMain.bottomNavigation.setOnItemSelectedListener { it ->
            when (it.itemId) {
                R.id.menu_home -> {
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
                    Intent(this, SettingScreenActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                    return@setOnItemSelectedListener true
                }
            }
            return@setOnItemSelectedListener false
        }

        userAdapter = UserListAdapter()
        userAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                Intent(this@MainActivity, DetailScreenActivity::class.java).also {
                    it.putExtra(DetailScreenActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailScreenActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailScreenActivity.EXTRA_AVATAR, data.avatar_url)
                    startActivity(it)
                }
            }

        })
        viewModel = getViewModel(this)

        bindingMain.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.adapter = userAdapter
            bindingMain.rvUser.setHasFixedSize(true)
        }
        searchHandler()
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity): MainViewModel {
        val viewModelFactory = ViewModelFactory.getInstance(appCompatActivity.application)
        return ViewModelProvider(appCompatActivity, viewModelFactory)[MainViewModel::class.java]
    }

    private fun searchHandler() {
        bindingMain.svSearch.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.equals("")) {
                    bindingMain.pbMain.visibility = View.GONE
                    bindingMain.rvUser.visibility = View.GONE
                    bindingMain.tvHome.text = resources.getText(R.string.find_github_user)
                    bindingMain.ivHome.setImageResource(R.drawable.new_found)
                    bindingMain.tvHome.visibility = View.VISIBLE
                    bindingMain.ivHome.visibility = View.VISIBLE
                } else if (query != null) {
                    bindingMain.tvHome.visibility = View.GONE
                    bindingMain.ivHome.visibility = View.GONE
                    searchQuery(query)
                    bindingMain.rvUser.visibility = View.VISIBLE
                }
                bindingMain.pbMain.visibility = View.VISIBLE
                val input = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                input.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                bindingMain.svSearch.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }

            private fun searchQuery(query: String) {
                bindingMain.pbMain.visibility = View.VISIBLE
                viewModel.setSearchQuery(query)
                viewModel.getSearchQuery().observe(this@MainActivity, { it ->
                    it.let {
                        userAdapter.setData(it)
                        bindingMain.pbMain.visibility = View.GONE
                        val dataCount: Int = userAdapter.itemCount
                        if (dataCount == 0) isFound(false) else isFound(true)
                    }
                })
            }

        })
    }

    private fun isFound(s: Boolean) {
        if (!s) {
            bindingMain.tvHome.text = resources.getText(R.string.nothing_found)
            bindingMain.ivHome.setImageResource(R.drawable.nothing_found)
            bindingMain.tvHome.visibility = View.VISIBLE
            bindingMain.ivHome.visibility = View.VISIBLE
        } else {
            bindingMain.tvHome.visibility = View.GONE
            bindingMain.ivHome.visibility = View.GONE
        }
    }

    private fun themeHandler() {
        viewModel.getThemeSettings().observe(this, {
            if (it) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        })
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