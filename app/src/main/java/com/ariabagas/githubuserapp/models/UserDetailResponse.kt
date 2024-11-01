package com.ariabagas.githubuserapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDetailResponse (
    val login: String,
    val id: Int,
    val avatar_url: String,
    val name: String,
    val followers: Int,
    val following: Int,
    val public_repos: Int,
): Parcelable
