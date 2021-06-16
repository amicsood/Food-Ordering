package com.samael.foodordering.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface OrderDao {
    @Insert
    fun insertOrder(orderEntity: OrderEntity)

    @Delete
    fun deleteOrder(orderEntity: OrderEntity)

    @Query("SELECT * FROM order_table")
    fun getAllOrders(): List<OrderEntity>

    @Query("DELETE FROM order_table WHERE resId = :resId")
    fun deleteOrders(resId: String)
}