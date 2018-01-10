package com.example.anujsharma.shuffler.dao;

import android.content.Context;

import com.android.volley.VolleyError;
import com.example.anujsharma.shuffler.models.User;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.Utilities;
import com.example.anujsharma.shuffler.volley.RequestCallback;
import com.example.anujsharma.shuffler.volley.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anuj5 on 04-01-2018.
 */

public class UsersDao extends VolleyRequest {

    RequestCallback requestCallback;

    public UsersDao(Context context, RequestCallback requestCallback) {
        super(context);
        this.requestCallback = requestCallback;
    }

    public void getUsersWithQuery(String query, int limit) {
        query = Utilities.encodeKeyword(query);
        String url = Utilities.getApiUrlUsersQuery(query, limit);
        callApiForArray(url, new DaoCallback() {
            @Override
            public void response(Object response) {
                JSONArray jsonArrayResponse = (JSONArray) response;
                ArrayList<User> users = new ArrayList<>();
                if (jsonArrayResponse.length() > 0) {
                    for (int i = 0; i < jsonArrayResponse.length(); i++) {
                        try {
                            User user = new User(jsonArrayResponse.getJSONObject(i));
                            users.add(user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                requestCallback.onListRequestSuccessful(users, Constants.SEARCH_USERS_WITH_QUERY, true);
            }

            @Override
            public void stringResponse(String response) {

            }

            @Override
            public void errorResponse(VolleyError error) {
                requestCallback.onListRequestSuccessful(null, Constants.SEARCH_USERS_WITH_QUERY, true);
            }
        }, Constants.METHOD_GET, null);
    }

    public void getUserWithId(final String userId) {
        String url = Utilities.getApiUrlUserId(userId);
        callApiForObject(url, new DaoCallback() {
            @Override
            public void response(Object response) {
                JSONObject jsonObjectResponse = (JSONObject) response;
                User user = new User(jsonObjectResponse);
                requestCallback.onObjectRequestSuccessful(user, Constants.SEARCH_USER_WITH_ID, true);
            }

            @Override
            public void stringResponse(String response) {

            }

            @Override
            public void errorResponse(VolleyError error) {
                requestCallback.onObjectRequestSuccessful(null, Constants.SEARCH_USER_WITH_ID, false);
            }
        }, Constants.METHOD_GET, null);
    }
}
