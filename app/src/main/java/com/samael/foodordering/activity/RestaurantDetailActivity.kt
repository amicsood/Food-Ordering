package com.samael.foodordering.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.samael.foodordering.R
import com.samael.foodordering.adapter.RestaurantDetailRecyclerAdapter
import com.samael.foodordering.database.OrderEntity
import com.samael.foodordering.database.RestaurantDatabase
import com.samael.foodordering.model.Food
import com.samael.foodordering.util.ConnectionManager
import org.json.JSONException

class RestaurantDetailActivity : AppCompatActivity() {

    lateinit var recyclerRestaurantDetail: RecyclerView
    lateinit var btnProceedToCart: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    var foodList = arrayListOf<Food>()
    var orderList = arrayListOf<Food>()
    lateinit var recyclerRecyclerAdapter: RestaurantDetailRecyclerAdapter
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    var restaurantName: String? = null
    var restaurantId: String? = null
    lateinit var imgFav: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail)

        layoutManager = LinearLayoutManager(this@RestaurantDetailActivity)
        recyclerRestaurantDetail = findViewById(R.id.recyclerRestaurantDetail)
        btnProceedToCart = findViewById(R.id.btnProceedToCart)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)
        toolbar = findViewById(R.id.toolbar)
        imgFav = findViewById(R.id.imgFav)

        progressLayout.visibility = View.VISIBLE
        btnProceedToCart.visibility = View.GONE

        if (intent != null) {
            restaurantName = intent.getStringExtra("restaurant_name")
            restaurantId = intent.getStringExtra("restaurant_id")
        } else {
            finish()
            Toast.makeText(
                this@RestaurantDetailActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (restaurantId == null || restaurantName == null) {
            finish()
            Toast.makeText(
                this@RestaurantDetailActivity,
                "Some error occur!!",
                Toast.LENGTH_SHORT
            ).show()
        }

        setSupportActionBar(toolbar)
        supportActionBar?.title = restaurantName

        val async = GetAllFavAsyncTask(this).execute()
        val result = async.get()

        if (restaurantId in result) {
            imgFav.setImageResource(R.drawable.ic_red_fav)
        } else {
            imgFav.setImageResource(R.drawable.ic_red_fav_outline)
        }

        btnProceedToCart.setOnClickListener {
            proceedToCart()
        }

        val queue = Volley.newRequestQueue(this@RestaurantDetailActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$restaurantId"

        if (ConnectionManager().checkConnectivity(this@RestaurantDetailActivity)) {
            val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null,
                Response.Listener {
                    try {

                        val data = it.getJSONObject("data")

                        val success = data.getBoolean("success")

                        if (success) {
                            progressLayout.visibility = View.GONE
                            val foodData = data.getJSONArray("data")
                            for (i in 0 until foodData.length()) {
                                val foodJsonObject = foodData.getJSONObject(i)
                                val food = Food(
                                    foodJsonObject.getString("id"),
                                    foodJsonObject.getString("name"),
                                    foodJsonObject.getString("cost_for_one").toInt()
                                )
                                foodList.add(food)

                                recyclerRecyclerAdapter =
                                    RestaurantDetailRecyclerAdapter(this@RestaurantDetailActivity, foodList,
                                        object : RestaurantDetailRecyclerAdapter.OnItemClickListener {
                                            override fun onAddItemClick(food: Food) {
                                                orderList.add(food)
                                                if (orderList.size > 0) {
                                                    btnProceedToCart.visibility = View.VISIBLE
                                                    RestaurantDetailRecyclerAdapter.isCartEmpty = false
                                                }
                                            }
                                            override fun onRemoveItemClick(food: Food) {
                                                orderList.remove(food)
                                                if (orderList.size == 0) {
                                                    btnProceedToCart.visibility = View.GONE
                                                    RestaurantDetailRecyclerAdapter.isCartEmpty = true
                                                }

                                            }

                                        })

                                recyclerRestaurantDetail.adapter = recyclerRecyclerAdapter

                                recyclerRestaurantDetail.layoutManager = layoutManager

                            }

                        } else {
                            Toast.makeText(
                                this@RestaurantDetailActivity,
                                "Some error occur!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@RestaurantDetailActivity,
                            "Some error occur!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                },
                Response.ErrorListener {
                    Toast.makeText(
                        this@RestaurantDetailActivity,
                        "Volley error occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "c53fffb51ed23c"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)

        } else {
            val dialog = AlertDialog.Builder(this@RestaurantDetailActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("open setting")
            { text, listner ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                finish()
            }
            dialog.setNegativeButton("exit")
            { text, listner ->
                ActivityCompat.finishAffinity(this@RestaurantDetailActivity)
            }

            dialog.create()
            dialog.show()
        }
    }

    class GetAllFavAsyncTask(context: Context) : AsyncTask<Void, Void, List<String>>() {

        val db =
            Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurant-db").build()

        override fun doInBackground(vararg params: Void?): List<String> {

            val list = db.restaurantDao().getAllRestaurant()
            val listOfIds = arrayListOf<String>()
            for (i in list) {
                listOfIds.add(i.id.toString())
            }
            return listOfIds
        }
    }

    fun proceedToCart() {
        val gson = Gson()

        val orderItems = gson.toJson(orderList)

        val async=CartItems(this@RestaurantDetailActivity,restaurantId.toString(),orderItems,1).execute()
        val result=async.get()

        if(result)
        {
            val bundle=Bundle()
            bundle.putString("restaurant_name",restaurantName)
            bundle.putString("restaurant_id",restaurantId)
            val intent=Intent(this@RestaurantDetailActivity,CartActivity::class.java)
            intent.putExtra("bundle",bundle)
            startActivity(intent)

        }
        else{
            Toast.makeText(this@RestaurantDetailActivity, "Some unexpected error", Toast.LENGTH_SHORT)
                .show()
        }


    }

    class CartItems(val context: Context, val resId: String, val orderItem: String, val mode: Int) :
        AsyncTask<Void,Void, Boolean>() {

        val db=Room.databaseBuilder(context,RestaurantDatabase::class.java,"restaurant-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {
            when(mode)
            {
                1->{
                    db.orderDao().insertOrder(OrderEntity(resId,orderItem))
                    db.close()
                    return true
                }
                2->
                {
                    db.orderDao().deleteOrder(OrderEntity(resId,orderItem))
                    db.close()
                    return true
                }
            }

            return false
        }
    }
}