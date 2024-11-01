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
import com.ariabagas.githubuserapp.databinding.FragmentFollowersBinding
import com.ariabagas.githubuserapp.models.UserModel
import com.ariabagas.githubuserapp.themes.ViewModelFactory
import com.ariabagas.githubuserapp.ui.activities.detail.DetailScreenActivity
import com.ariabagas.githubuserapp.ui.activities.detail.FollowersViewModel
import com.ariabagas.githubuserapp.ui.adapters.UserListAdapter

class FollowersFragment : Fragment() {
    private lateinit var bindingFollowers: FragmentFollowersBinding
    private lateinit var viewModel: FollowersViewModel
    private lateinit var userAdapter: UserListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bindingFollowers = FragmentFollowersBinding.inflate(inflater, container, false)
        return bindingFollowers.root
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

        bindingFollowers.apply {
            rvFollowers.layoutManager = LinearLayoutManager(activity)
            rvFollowers.adapter = userAdapter
            bindingFollowers.rvFollowers.setHasFixedSize(true)
        }

        viewModel.setListFollower(username)
        viewModel.getListfollower().observe(viewLifecycleOwner, {
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

    private fun getViewModel(appCompatActivity: AppCompatActivity): FollowersViewModel {
        val viewModelFactory = ViewModelFactory.getInstance(appCompatActivity.application)
        return ViewModelProvider(
            appCompatActivity,
            viewModelFactory
        )[FollowersViewModel::class.java]
    }


    private fun isFound(s: Boolean) {
        if (s) {
            bindingFollowers.pbFollowers.visibility = View.GONE
            bindingFollowers.tvFollowersNot.visibility = View.GONE
            bindingFollowers.ivFollowersNot.visibility = View.GONE
        } else {
            bindingFollowers.pbFollowers.visibility = View.GONE
            bindingFollowers.tvFollowersNot.visibility = View.VISIBLE
            bindingFollowers.ivFollowersNot.visibility = View.VISIBLE
        }
    }
}