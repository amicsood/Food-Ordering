package com.samael.foodordering.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samael.foodordering.R
import com.samael.foodordering.activity.CartActivity
import com.samael.foodordering.model.Food

class CartRecyclerAdapter(
    val context: CartActivity,
    val itemList: ArrayList<Food>
):RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>(){

    class CartViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val txtNameOfFood:TextView=view.findViewById(R.id.txtNameOfFood)
        val txtPriceOfFood: TextView =view.findViewById(R.id.txtPriceOfFood)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart_single_row,parent,false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val order=itemList[position]
        holder.txtNameOfFood.text=order.name
        holder.txtPriceOfFood.text="Rs "+order.cost
    }
}