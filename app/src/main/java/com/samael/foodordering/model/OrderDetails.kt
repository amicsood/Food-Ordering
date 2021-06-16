package com.samael.foodordering.model

import org.json.JSONArray

data class OrderDetails(

    val order_id: Int,
    val restaurant_name: String,
    val total_cost: Int,
    val order_placed_at: String,
    val food: JSONArray
)