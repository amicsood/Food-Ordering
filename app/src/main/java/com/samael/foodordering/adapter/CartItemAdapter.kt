package com.samael.foodordering.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samael.foodordering.R
import com.samael.foodordering.model.Food

class CartItemAdapter(private val cartList: ArrayList<Food>, val context: Context) :
    RecyclerView.Adapter<CartItemAdapter.CartViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart_item_single_row, parent, false)
        return CartViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cartList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class CartViewHolder(view: View):RecyclerView.ViewHolder(view){

        val txtItemName: TextView =view.findViewById(R.id.txtCartItemName)
        val txtItemPrice: TextView =view.findViewById(R.id.txtCartPrice)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartObject = cartList[position]
        holder.txtItemName.text = cartObject.name
        val cost = "Rs. ${cartObject.cost}"
        holder.txtItemPrice.text = cost
    }
}