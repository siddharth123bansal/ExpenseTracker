package com.example.expensetracker.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensetracker.Models.Datamodel
import com.example.expensetracker.R

class DataAdapter(val context: Context,val list:ArrayList<Datamodel>):RecyclerView.Adapter<DataAdapter.Data>() {

    class Data(itemView: View):RecyclerView.ViewHolder(itemView){
        val date=itemView.findViewById<TextView>(R.id.date)
        val open=itemView.findViewById<TextView>(R.id.open)
        val close=itemView.findViewById<TextView>(R.id.close)
        val high=itemView.findViewById<TextView>(R.id.high)
        val low=itemView.findViewById<TextView>(R.id.low)
        val vol=itemView.findViewById<TextView>(R.id.volume)
        val adjclose=itemView.findViewById<TextView>(R.id.adjvol)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Data {
        val view = LayoutInflater.from(context).inflate(R.layout.sample_data,parent,false)
        return DataAdapter.Data(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: Data, position: Int) {
        val data:Datamodel=list.get(position)
        holder.date.setText("Date="+data.date.toString().trim())
        holder.open.setText("Open="+data.open.toString().trim())
        holder.close.setText("Close"+data.close.toString().trim())
        holder.high.setText("High="+data.high.toString().trim())
        holder.low.setText("Low="+data.low.toString().trim())
        holder.vol.setText("Vol="+data.volume.toString().trim())
        holder.adjclose.setText("AdjClose="+data.adjClose.toString().trim())
    }
}