package com.example.roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MyDao {
    @Query("SELECT * FROM MyDb")
    suspend fun getData():List<MyTable>

    @Insert
    fun insertData(myTable: MyTable)

    @Query("Delete from MyDb where id=:id")
    suspend fun deleteData(id:Long)
}