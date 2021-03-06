package com.samael.foodordering.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.samael.foodordering.R
import com.samael.foodordering.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var etMobilePhone: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgetPassword: TextView
    lateinit var txtRegister: TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etMobilePhone = findViewById(R.id.etMobilePhone)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgetPassword = findViewById(R.id.txtForgetPassword)
        txtRegister = findViewById(R.id.txtRegister)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)


        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {

            val mobile_number = etMobilePhone.text.toString()
            val password = etPassword.text.toString()

            if (mobile_number != "" && password != "") {
                if (mobile_number.length == 10 && password.length >= 4) {
                    val queue = Volley.newRequestQueue(this@LoginActivity)
                    val url = "http://13.235.250.119/v2/login/fetch_result"

                    val jsonObject = JSONObject()

                    jsonObject.put("mobile_number", mobile_number)
                    jsonObject.put("password", password)

                    if (ConnectionManager().checkConnectivity(this@LoginActivity)) {
                        val jsonObjectRequest =
                            object : JsonObjectRequest(Method.POST, url, jsonObject,
                                Response.Listener {
                                    try {

                                        val data = it.getJSONObject("data")

                                        val success = data.getBoolean("success")

                                        if (success) {
                                            Toast.makeText(this@LoginActivity, "Login Successfully", Toast.LENGTH_SHORT).show()

                                            val user_data = data.getJSONObject("data")

                                            val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                                            setSharedPreferences(user_data)
                                            savePreferences()
                                            startActivity(intent)
                                            finish()

                                        } else {
                                            Toast.makeText(this@LoginActivity, data.getString("errorMessage"), Toast.LENGTH_SHORT).show()
                                        }
                                    } catch (e: JSONException) {
                                        Toast.makeText(this@LoginActivity, "Some Unexpected Error Occur", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                Response.ErrorListener {
                                    if (this@LoginActivity != null) {
                                        Toast.makeText(this@LoginActivity, "Volley error occurred!", Toast.LENGTH_SHORT).show()
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
                        val dialog = AlertDialog.Builder(this@LoginActivity)
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
                            ActivityCompat.finishAffinity(this@LoginActivity)
                        }

                        dialog.create()
                        dialog.show()
                    }
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Enter the correct Details",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this@LoginActivity, "Enter the Login Details", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        txtForgetPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }

        txtRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    fun setSharedPreferences(user_data: JSONObject) {
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()

        sharedPreferences.edit()
            .putString("user_id", user_data.getString("user_id"))
            .apply()

        sharedPreferences.edit()
            .putString("name", user_data.getString("name"))
            .apply()

        sharedPreferences.edit()
            .putString("email", user_data.getString("email"))
            .apply()

        sharedPreferences.edit()
            .putString("mobile_number", user_data.getString("mobile_number"))
            .apply()

        sharedPreferences.edit()
            .putString("address", user_data.getString("address"))
            .apply()
    }

    fun savePreferences() {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
    }
}
