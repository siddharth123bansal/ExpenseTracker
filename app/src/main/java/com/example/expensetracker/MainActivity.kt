package com.example.expensetracker

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.expensetracker.Adapters.DataAdapter
import com.example.expensetracker.Models.Datamodel
import com.example.expensetracker.helpers.VolleyReq
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    lateinit var menuBtn: ImageView
    lateinit var list:ArrayList<Datamodel>
    lateinit var sdate:TextView
    lateinit var edate:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        list=ArrayList()
        menuBtn = findViewById(R.id.menuBtn)
        menuBtn.setOnClickListener {
            showPopupMenu()
        }
        val symbol=findViewById<EditText>(R.id.symbol)
        sdate=findViewById(R.id.startDate)
        edate=findViewById(R.id.endDate)
        sdate.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                showDatePickerDialog(1)
            }
        }
        edate.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                showDatePickerDialog(2)
            }
        }

        val submit=findViewById<Button>(R.id.submitBtn)
        submit.setOnClickListener {
            if(symbol.text.toString().trim().isNullOrEmpty() || sdate.text.toString().trim().isNullOrEmpty()||
                edate.text.toString().trim().isNullOrEmpty()){
                Toast.makeText(this,"All fields are requried",Toast.LENGTH_SHORT).show()
            }else{
                fetchData(symbol.text.toString().trim(),sdate.text.toString().trim(),edate.text.toString().trim())
            }
        }
        val recycler=findViewById<RecyclerView>(R.id.recyclerData)
        recycler.adapter=DataAdapter(this,list)
        val linear= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recycler.layoutManager=linear
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun showDatePickerDialog(datePickerId: Int) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this, this, year, month, day)
        datePickerDialog.datePicker.tag = datePickerId
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val selectedDate = Calendar.getInstance()
        selectedDate.set(year, month, dayOfMonth)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(selectedDate.time)

        val datePickerId = view?.tag as Int
        if (datePickerId == 1) {
            sdate.text = formattedDate
        } else if (datePickerId == 2) {
            edate.text = formattedDate
        }
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(this, menuBtn)
        popupMenu.inflate(R.menu.dashboard_menu)
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val senderid = sharedPreferences.getString("id", null).toString()
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.logout -> {
                    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val e = sharedPreferences.edit()
                    e.clear()
                    e.apply()
                    startActivity(Intent(this, SignUpActivity::class.java))
                    true
                }
                else -> false
            }
        }
            popupMenu.show()
    }

    private fun fetchData(symbol:String,sdate:String,edate:String) {
        val rep=VolleyReq()
        val recycler=findViewById<RecyclerView>(R.id.recyclerData)

        list= rep.fetchData(symbol,sdate,edate,this,list,recycler)
    }
    override fun onResume() {
        super.onResume()
        val recycler=findViewById<RecyclerView>(R.id.recyclerData)
        recycler.adapter=DataAdapter(this,list)
        val linear= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
        recycler.layoutManager=linear
    }
}