package com.example.anujsharma.shuffler.dao;

import com.android.volley.VolleyError;

/**
 * Created by anuj5 on 02-01-2018.
 */

public interface DaoCallback {

    void response(Object response);

    void stringResponse(String response);

    void errorResponse(VolleyError error);
}
