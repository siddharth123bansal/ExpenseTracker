package com.example.expensetracker.helpers;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.expensetracker.Adapters.DataAdapter;
import com.example.expensetracker.Loadings.LoadingDialog;
import com.example.expensetracker.MainActivity;
import com.example.expensetracker.Models.Datamodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VolleyReq {

    public ArrayList<Datamodel> fetchData(String symbol, String sdate, String edate, Context context,
                                          ArrayList<Datamodel> list,RecyclerView rec){
        Map<String, String> headers = new HashMap<>();
        headers.put("X-RapidAPI-Key", "e0a01ebadamshe1cda345609d7a4p1e54aajsn3c1729e8efaf");
        headers.put("X-RapidAPI-Host", "apistocks.p.rapidapi.com");
        LoadingDialog pd= new LoadingDialog(context);
        pd.setCancelable(true);
        pd.show();
        String url = "https://apistocks.p.rapidapi.com/daily?symbol="+symbol+"&dateStart="+sdate+"&dateEnd="+ edate;
        //String urls = "https://apistocks.p.rapidapi.com/daily?symbol=AAPLdateStart2021-07-01=&dateEnd=2021-07-31";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {
                            list.clear();
                            try {
                                //Toast.makeText(context, "SUCCESS", Toast.LENGTH_SHORT).show();
                                JSONArray array = response.getJSONArray("Results");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject p = array.getJSONObject(i);
                                    String date = p.getString("Date");
                                    String open = p.getString("Open");
                                    String close = p.getString("Close");
                                    String high = p.getString("High");
                                    String low = p.getString("Low");
                                    String vol = p.getString("Volume");
                                    String adjclose = p.getString("AdjClose");
                                    list.add(new Datamodel(date, open, close, high, low, vol, adjclose));
                                }
//                                val recycler=findViewById<RecyclerView>(R.id.recyclerData)
//                                        recycler.adapter=DataAdapter(this,list)
//                                val linear= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)
//                                recycler.layoutManager=linear
                                rec.setAdapter(new DataAdapter(context,list));
                                LinearLayoutManager linear = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                                rec.setLayoutManager(linear);
                                pd.dismiss();
                            } catch (JSONException e) {
                                pd.dismiss();
                                Toast.makeText(context, "Error try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VolleyRequest", "Error: " + error.getMessage());
                        pd.dismiss();
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                return headers;
            }
        };

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
        return list;
    }
}
