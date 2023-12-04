package com.example.github_search.data.repo

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.github_search.data.model.generic.Demo
import com.example.github_search.data.model.generic.Item
import com.example.github_search.data.model.generic.ItemAndOwnerAndLicense
import com.example.github_search.data.model.generic.License
import com.example.github_search.data.model.generic.Owner
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Singleton
@Dao
interface LocalSource {

    @Query("SELECT * FROM demo")
    fun getData() : Flow<List<Demo>>

    @Query("SELECT * FROM repo WHERE name LIKE '%' || (:searchKeyWord) || '%'")
    fun getRepository(searchKeyWord: String) : List<Item>


    @Query("SELECT * FROM owner")
    fun getRepositoryOwner() : Flow<Owner>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: List<Item>)

}