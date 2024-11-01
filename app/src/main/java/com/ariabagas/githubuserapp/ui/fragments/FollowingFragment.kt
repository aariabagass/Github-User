package com.ariabagas.githubuserapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ariabagas.githubuserapp.databinding.FragmentFollowingBinding
import com.ariabagas.githubuserapp.models.UserModel
import com.ariabagas.githubuserapp.themes.ViewModelFactory
import com.ariabagas.githubuserapp.ui.activities.detail.DetailScreenActivity
import com.ariabagas.githubuserapp.ui.activities.detail.FollowingViewModel
import com.ariabagas.githubuserapp.ui.adapters.UserListAdapter

class FollowingFragment : Fragment() {
    private lateinit var bindingFollowing: FragmentFollowingBinding
    private lateinit var viewModel: FollowingViewModel
    private lateinit var userAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingFollowing = FragmentFollowingBinding.inflate(inflater, container, false)
        return bindingFollowing.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(DetailScreenActivity.EXTRA_USERNAME).toString()
        userAdapter = UserListAdapter()
        userAdapter.setOnItemClickCallback(object : UserListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserModel) {
                Intent(context, DetailScreenActivity::class.java).also {
                    it.putExtra(DetailScreenActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailScreenActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailScreenActivity.EXTRA_AVATAR, data.avatar_url)
                    startActivity(it)
                }
            }

        })
        viewModel = getViewModel(context as AppCompatActivity)

        bindingFollowing.apply {
            rvFollowing.layoutManager = LinearLayoutManager(activity)
            rvFollowing.adapter = userAdapter
            bindingFollowing.rvFollowing.setHasFixedSize(true)
        }

        viewModel.setListFollowing(username)
        viewModel.getListfollowing().observe(viewLifecycleOwner, {
            it.let {
                if (it.isNotEmpty()) {
                    userAdapter.setData(it)
                    isFound(true)
                } else {
                    isFound(false)
                }
            }
        })
    }

    private fun getViewModel(appCompatActivity: AppCompatActivity): FollowingViewModel {
        val viewModelFactory = ViewModelFactory.getInstance(appCompatActivity.application)
        return ViewModelProvider(
            appCompatActivity,
            viewModelFactory
        )[FollowingViewModel::class.java]
    }

    private fun isFound(s: Boolean) {
        if (s) {
            bindingFollowing.pbFollowing.visibility = View.GONE
            bindingFollowing.tvFollowingNot.visibility = View.GONE
            bindingFollowing.ivFollowingNot.visibility = View.GONE
        } else {
            bindingFollowing.pbFollowing.visibility = View.GONE
            bindingFollowing.tvFollowingNot.visibility = View.VISIBLE
            bindingFollowing.ivFollowingNot.visibility = View.VISIBLE
        }
    }
}