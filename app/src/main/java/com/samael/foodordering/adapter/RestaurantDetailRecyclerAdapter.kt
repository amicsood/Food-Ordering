package com.samael.foodordering.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.samael.foodordering.R
import com.samael.foodordering.activity.RestaurantDetailActivity
import com.samael.foodordering.model.Food

class RestaurantDetailRecyclerAdapter(
    val context: Context,
    private val itemList: ArrayList<Food>,
    private val listner: OnItemClickListener
) : RecyclerView.Adapter<RestaurantDetailRecyclerAdapter.RestaurantDetailViewHolder>() {

    companion object {
        var isCartEmpty = true
    }

    interface OnItemClickListener {
        fun onAddItemClick(food: Food)
        fun onRemoveItemClick(food: Food)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_restaurant_detail_single_row, parent, false)

        return RestaurantDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RestaurantDetailViewHolder, position: Int) {
        val food = itemList[position]
        holder.txtNo.text = "${position + 1}"
        holder.txtFoodName.text = food.name
        holder.txtFoodPrice.text = "Rs " + food.cost

        holder.btnAdd.setOnClickListener {
            holder.btnAdd.visibility = View.GONE
            holder.btnRemove.visibility = View.VISIBLE
            listner.onAddItemClick(food)
        }

        holder.btnRemove.setOnClickListener {
            holder.btnAdd.visibility = View.VISIBLE
            holder.btnRemove.visibility = View.GONE
            listner.onRemoveItemClick(food)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class RestaurantDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNo: TextView = view.findViewById(R.id.txtNo)
        val txtFoodName: TextView = view.findViewById(R.id.txtFoodName)
        val txtFoodPrice: TextView = view.findViewById(R.id.txtFoodPrice)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)
        val btnRemove: Button = view.findViewById(R.id.btnRemove)
    }
}