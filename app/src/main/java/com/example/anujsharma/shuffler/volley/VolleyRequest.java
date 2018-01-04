package com.example.anujsharma.shuffler.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.anujsharma.shuffler.dao.DaoCallback;
import com.example.anujsharma.shuffler.utilities.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by anuj5 on 02-01-2018.
 */

public class VolleyRequest {

    private Context context;

    public VolleyRequest(Context context) {
        this.context = context;
    }

    public void callApiForArray(String url, final DaoCallback daoCallback, int method, JSONObject jsonObject) {
        switch (method) {
            case Constants.METHOD_GET:
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                        url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        daoCallback.response(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        daoCallback.errorResponse(error);
                    }
                });
                VolleySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);
                break;

        }
    }

    public void callApiForObject(String url, final DaoCallback daoCallback, int method, JSONObject jsonObject) {
        switch (method) {
            case Constants.METHOD_GET:
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                        url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        daoCallback.response(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        daoCallback.errorResponse(error);
                    }
                });
                VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
                break;
        }
    }


}
