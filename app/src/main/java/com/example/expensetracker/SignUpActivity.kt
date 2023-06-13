package com.example.expensetracker

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
import java.lang.Exception

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        val alacc=findViewById<TextView>(R.id.AlreadyAcc)
        val signupBtn=findViewById<Button>(R.id.button)
        val username=findViewById<EditText>(R.id.eUsername)
        val email=findViewById<EditText>(R.id.eEmail)
        val pass=findViewById<EditText>(R.id.ePassword)
        alacc.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        signupBtn.setOnClickListener {
            if(username.text.toString().isNullOrEmpty() && email.text.toString().isNullOrEmpty() && pass.text.toString().isNullOrEmpty()){
                username.setError("Username is required")
                email.setError("Email is required")
                pass.setError("password is required")
            }else if(username.text.toString().isNullOrEmpty()){
                username.setError("Username is required")
            }else if(email.text.toString().isNullOrEmpty()){
                email.setError("Email is required")
            }else if(pass.text.toString().isNullOrEmpty()){
                pass.setError("password is required")
            }else{
                signUp(username.text.toString().trim(),email.text.toString().trim(),pass.text.toString().trim())
            }
        }
    }
    private fun signUp(user:String,email:String,pass:String) {
        val pd = LoadingDialog(this)
        pd.setCancelable(false)
        pd.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        pd.show()
        val requestBody = JSONObject()
        requestBody.put("username", user)
        requestBody.put("email", email)
        requestBody.put("password", pass)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST,"https://siddharthmongoapi.onrender.com/save", requestBody,
            { response ->
                if(response!=null){
                    try{
                        var id= JSONObject(response.toString()).getString("id")
                        var pwd= JSONObject(response.toString()).getString("password")
                        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        sharedPreferences.edit()
                            .putString("id",id)
                            .putString("username", user)
                            .putString("email", email)
                            .putString("pass", pass)
                            .apply()
                        pd.dismiss()
                        val i = Intent(this, MainActivity::class.java)
                        startActivity(i)
                        finish()
                    }catch (ex: Exception){
                        var msg= JSONObject(response.toString()).getString("message")
                        if(msg.contains("email_unique_index dup key:")){
                            pd.dismiss()
                            Toast.makeText(this,"User Already exists", Toast.LENGTH_SHORT).show()
                        }else {
                            pd.dismiss()
                            Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            { error ->
                Log.e("VolleyRequest", "Error: ${error.message}")
                Toast.makeText(this,"error "+error.message.toString(), Toast.LENGTH_SHORT).show()
                pd.cancel()
            }
        )
        jsonObjectRequest.retryPolicy= DefaultRetryPolicy(120000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(jsonObjectRequest)
    }
}