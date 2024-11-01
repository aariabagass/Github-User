package com.ariabagas.githubuserapp.ui.activities.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.ariabagas.githubuserapp.R
import com.ariabagas.githubuserapp.databinding.ActivityDetailScreenBinding
import com.ariabagas.githubuserapp.themes.ViewModelFactory
import com.ariabagas.githubuserapp.ui.adapters.SectionsPagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailScreenActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var bindingDetail: ActivityDetailScreenBinding
    private lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingDetail = ActivityDetailScreenBinding.inflate(layoutInflater)
        setContentView(bindingDetail.root)
        bindingDetail.btnBackDetail.setOnClickListener(this)
        setupView()
    }

    private fun setupView() {
        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = getViewModel(this)
        viewModel.setGithubDetail(username.toString())
        viewModel.getGithubDetail().observe(this, { it ->
            it.let {
                if (username != null) {
                    bindingDetail.pbDetail.visibility = View.GONE
                    Glide.with(this@DetailScreenActivity)
                        .load(it.avatar_url)
                        .into(bindingDetail.ivDetail)
                    bindingDetail.tvUsername.text = it.login
                    bindingDetail.tvName.text = it.name
                    bindingDetail.tvDetailFollowing.text = it.following.toString()
                    bindingDetail.tvDetailFollowers.text = it.followers.toString()
                    bindingDetail.tvDetailRepository.text = it.public_repos.toString()
                }
            }
        })

        var isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.getUser(id)
            withContext(Dispatchers.Main) {
                if (count != null) {
                    if (count > 0) {
                        bindingDetail.toggleFavorite.isChecked = true
                        isChecked = true
                    } else {
                        bindingDetail.toggleFavorite.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        bindingDetail.toggleFavorite.setOnClickListener {
            isChecked = !isChecked
            if (isChecked) {
                viewModel.addFavorite(username!!, id, avatar!!)
            } else {
                viewModel.removeFavorite(id)
            }
            bindingDetail.toggleFavorite.isChecked = isChecked
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        val viewPager: ViewPager2 = bindingDetail.vpFollow
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = bindingDetail.tlFollow
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
            tab.icon = resources.getDrawable(TAB_ICONS[position])
        }.attach()
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity): DetailViewModel {
        val viewModelFactory = ViewModelFactory.getInstance(appCompatActivity.application)
        return ViewModelProvider(appCompatActivity, viewModelFactory)[DetailViewModel::class.java]
    }

    override fun onClick(v: View?) {
        when (v) {
            bindingDetail.btnBackDetail -> onBackPressed()
        }
    }

    companion object {
        var EXTRA_USERNAME = "extra_username"
        var EXTRA_ID = "extra_id"
        var EXTRA_AVATAR = "extra_avatar"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_following,
            R.string.tab_followers
        )

        @DrawableRes
        private val TAB_ICONS = intArrayOf(
            R.drawable.following_icon,
            R.drawable.follower_icon
        )
    }
}