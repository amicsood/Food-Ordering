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
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.samael.foodordering.R
import com.samael.foodordering.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class RegistrationActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var rlRegister: RelativeLayout
    lateinit var etName: EditText
    lateinit var etEmailAddress: EditText
    lateinit var etMobile: EditText
    lateinit var etDelivery: EditText
    lateinit var etEnterPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnRegister: Button

    var emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rlRegister = findViewById(R.id.rlRegister)
        etName = findViewById(R.id.etName)
        etEmailAddress = findViewById(R.id.etEmailAddress)
        etMobile = findViewById(R.id.etMobile)
        etDelivery = findViewById(R.id.etDelivery)
        etEnterPassword = findViewById(R.id.etEnterPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        toolbar = findViewById(R.id.toolbar)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

        setUpToolbar()

        btnRegister.setOnClickListener {

            val name = etName.text.toString()
            val email = etEmailAddress.text.toString()
            val mobile_number = etMobile.text.toString()
            val address = etDelivery.text.toString()
            val password = etEnterPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (name != "" && email != "" && mobile_number != "" && address != "" && password != "" && confirmPassword != "") {

                if (!email.trim().matches(emailPattern))
                    Toast.makeText(this@RegistrationActivity, "Invalid Email", Toast.LENGTH_SHORT).show()

                if (password == confirmPassword)
                {
                    val queue = Volley.newRequestQueue(this@RegistrationActivity)
                    val url = "http://13.235.250.119/v2/register/fetch_result"

                    val jsonObject = JSONObject()

                    jsonObject.put("name", name)
                    jsonObject.put("mobile_number", mobile_number)
                    jsonObject.put("password", password)
                    jsonObject.put("address", address)
                    jsonObject.put("email", email)

                    if (ConnectionManager().checkConnectivity(this@RegistrationActivity)) {
                        val jsonObjectRequest =
                            object : JsonObjectRequest(Method.POST, url, jsonObject,
                                Response.Listener {

                                    try {

                                        val data = it.getJSONObject("data")

                                        val success = data.getBoolean("success")


                                        if (success) {

                                            Toast.makeText(
                                                this@RegistrationActivity,
                                                "Registered Successfully",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            val user_data = data.getJSONObject("data")
                                            val intent = Intent(
                                                this@RegistrationActivity,
                                                DashboardActivity::class.java
                                            )

                                            setSharedPreferences(user_data)

                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(
                                                this@RegistrationActivity,
                                                data.getString("errorMessage"),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    } catch (e: JSONException) {
                                        Toast.makeText(
                                            this@RegistrationActivity,
                                            "Some Unexpected Error Occur",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                },
                                Response.ErrorListener {
                                    if (this@RegistrationActivity != null) {
                                        Toast.makeText(
                                            this@RegistrationActivity,
                                            "Volley error occurred!",
                                            Toast.LENGTH_SHORT
                                        ).show()
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
                        val dialog = AlertDialog.Builder(this@RegistrationActivity)
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
                            ActivityCompat.finishAffinity(this@RegistrationActivity)
                        }

                        dialog.create()
                        dialog.show()
                    }

                } else {
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Password and Confirm Password is not same",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Fill All the Details!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun setSharedPreferences(user_data: JSONObject) {

        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

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

    override fun onSupportNavigateUp(): Boolean {
        val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
        startActivity(intent)
        return true
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}