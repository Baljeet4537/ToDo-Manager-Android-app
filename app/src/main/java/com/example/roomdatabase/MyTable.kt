package com.example.roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MyDb")
data class MyTable(
    @PrimaryKey(autoGenerate = true)
    val id:Long,
    val name:String,
)
