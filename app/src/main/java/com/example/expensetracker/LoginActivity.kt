package com.example.expensetracker

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.expensetracker.Loadings.LoadingDialog
import org.json.JSONObject
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val acc=findViewById<TextView>(R.id.noAcc)
        val email=findViewById<EditText>(R.id.lEmail)
        val pass=findViewById<EditText>(R.id.lPassword)
        acc.setOnClickListener {
            val i = Intent(this,SignUpActivity::class.java)
            startActivity(i)
            finish()
        }
        val loginBt=findViewById<Button>(R.id.logIn)
        loginBt.setOnClickListener {
            if(email.text.toString().isNullOrEmpty() && pass.text.toString().isNullOrEmpty()){
                email.setError("This field is required")
                pass.setError("This field is required")
            }else if(email.text.toString().isNullOrEmpty()){
                email.setError("This field is required")
            }else if(pass.text.toString().isNullOrEmpty()){
                pass.setError("This field is required")
            }else{
                val pd=LoadingDialog(this)
                pd.setCancelable(true)
                pd.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                pd.show()
                val requestBody = JSONObject()
                requestBody.put("email", email.text.toString())
                requestBody.put("password", pass.text.toString())
                val jsonObjectRequest = JsonObjectRequest(
                    Request.Method.POST,"https://siddharthmongoapi.onrender.com/login", requestBody,
                    { response ->
                        if(response!=null){
                            try {
                                val id= JSONObject(response.toString()).getString("id")
                                val user=JSONObject(response.toString()).getString("username")
                                val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                                sharedPreferences.edit()
                                    .putString("id",id)
                                    .putString("username", user)
                                    .putString("email", email.text.toString())
                                    .putString("pass", pass.text.toString())
                                    .apply()
                                pd.dismiss()
                                val i = Intent(this, MainActivity::class.java)
                                startActivity(i)
                                finish()
                            }catch (e:Exception){
                                var msg= JSONObject(response.toString()).getString("message")
                                pd.cancel()
                                Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    { error ->
                        Log.e("VolleyRequest", "Error: ${error.message}")
                        pd.cancel()
                        Toast.makeText(this,"error "+error.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                )
                jsonObjectRequest.retryPolicy= DefaultRetryPolicy(120000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                val requestQueue = Volley.newRequestQueue(this)
                requestQueue.add(jsonObjectRequest)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        try{
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val id = sharedPreferences.getString("id", null)
            if (id!!.isNotEmpty()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            }
        }catch (e: java.lang.Exception){
        }
    }
}