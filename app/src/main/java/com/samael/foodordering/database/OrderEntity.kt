package com.samael.foodordering.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samael.foodordering.model.Food

@Entity(tableName = "order_table")
data class OrderEntity(
    @PrimaryKey val resId: String,
    @ColumnInfo(name = "food") val food: String
)