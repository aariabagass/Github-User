package com.ariabagas.githubuserapp.utils.services

import com.ariabagas.githubuserapp.BuildConfig
import com.ariabagas.githubuserapp.models.UserResponse
import com.ariabagas.githubuserapp.models.UserDetailResponse
import com.ariabagas.githubuserapp.models.UserModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("search/users")
    @Headers("Authorization: ${BuildConfig.GITHUB_TOKEN}")
    fun getSearchUsers(
        @Query("q") query: String
    ) : Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: ${BuildConfig.GITHUB_TOKEN}")
    fun getUserDetail(
        @Path("username") username: String
    ): Call<UserDetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: ${BuildConfig.GITHUB_TOKEN}")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<ArrayList<UserModel>>

    @GET("users/{username}/following")
    @Headers("Authorization: ${BuildConfig.GITHUB_TOKEN}")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<ArrayList<UserModel>>
}