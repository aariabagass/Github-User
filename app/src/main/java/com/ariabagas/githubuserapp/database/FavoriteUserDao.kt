package com.ariabagas.githubuserapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ariabagas.githubuserapp.models.FavoriteUser

@Dao
interface FavoriteUserDao {
    @Insert
    suspend fun addFavoriteUser(favoriteUser: FavoriteUser)

    @Query("SELECT * FROM favorite_user")
    fun getListFavoriteUser(): LiveData<List<FavoriteUser>>

    @Query("SELECT count(*) FROM favorite_user WHERE favorite_user.id = :id")
    suspend fun getFavoriteUser(id: Int): Int

    @Query("DELETE FROM favorite_user WHERE favorite_user.id = :id")
    suspend fun removeFavoriteUser(id: Int): Int
}