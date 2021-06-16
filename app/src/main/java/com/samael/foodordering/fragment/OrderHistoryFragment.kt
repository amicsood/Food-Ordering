package com.samael.foodordering.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.samael.foodordering.R
import com.samael.foodordering.adapter.OrderHistoryAdapter
import com.samael.foodordering.model.OrderDetails
import com.samael.foodordering.util.ConnectionManager

import kotlinx.android.synthetic.main.activity_cart.*
import org.json.JSONException


class OrderHistoryFragment : Fragment() {


    lateinit var orderHistoryAdapter: OrderHistoryAdapter
    lateinit var sharedPreferences: SharedPreferences
    lateinit var recyclerOrder1: RecyclerView
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    val orderHistoryList = arrayListOf<OrderDetails>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_history, container, false)
        sharedPreferences = activity!!.getSharedPreferences(
            getString(R.string.preference_file_name),
            Context.MODE_PRIVATE
        )

        recyclerOrder1 = view.findViewById(R.id.recyclerOrder1)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        val userId = sharedPreferences.getString("user_id", "user_id")
        layoutManager = LinearLayoutManager(activity as Context)

        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/orders/fetch_result/$userId"

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonObjectRequest = object : JsonObjectRequest(
                Request.Method.GET, url, null, Response.Listener {
                    try {
                        progressLayout.visibility = View.GONE
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")

                        if (success) {
                            val data = data.getJSONArray("data")

                            for (i in 0 until data.length()) {

                                val OrderJsonObject = data.getJSONObject(i)
                                val food = OrderJsonObject.getJSONArray("food_items")
                                val orderDetails = OrderDetails(
                                    OrderJsonObject.getInt("order_id"),
                                    OrderJsonObject.getString("restaurant_name"),
                                    OrderJsonObject.getString("total_cost").toInt(),
                                    OrderJsonObject.getString("order_placed_at"),
                                    food
                                )

                                orderHistoryList.add(orderDetails)
                                orderHistoryAdapter =
                                    OrderHistoryAdapter(
                                        activity as Context,
                                        orderHistoryList
                                    )
                                recyclerOrder1.adapter = orderHistoryAdapter
                                recyclerOrder1.layoutManager = layoutManager
                                if (orderHistoryList.isEmpty()) {

                                } else {

                                    if (activity != null) {
                                        orderHistoryAdapter = OrderHistoryAdapter(
                                            activity as Context,
                                            orderHistoryList
                                        )
                                        val mLayoutManager =
                                            LinearLayoutManager(activity as Context)
                                        recyclerOrder1.layoutManager = mLayoutManager
                                        recyclerOrder1.itemAnimator = DefaultItemAnimator()
                                        recyclerOrder1.adapter = orderHistoryAdapter
                                    }
                                }

                            }
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some error occurred",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    } catch (e: JSONException) {
                        Toast.makeText(
                            activity as Context,
                            "Some Unexpected error occurred",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                },
                Response.ErrorListener {

                    if (activity != null) {
                        Toast.makeText(
                            activity as Context,
                            "Volley error occurred",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
            ) {
                override fun getHeaders(): MutableMap<String, String> {

                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "c53fffb51ed23c"

                    return headers
                }
            }

            queue.add(jsonObjectRequest)

        } else {

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Not found")
            dialog.setPositiveButton("Open Settings") { text, listener ->
                val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingsIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)

            }
            dialog.create()
            dialog.show()
        }

        return view
    }
}