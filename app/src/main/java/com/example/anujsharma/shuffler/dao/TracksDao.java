package com.example.anujsharma.shuffler.dao;

import android.content.Context;

import com.android.volley.VolleyError;
import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.utilities.Constants;
import com.example.anujsharma.shuffler.utilities.Utilities;
import com.example.anujsharma.shuffler.volley.RequestCallback;
import com.example.anujsharma.shuffler.volley.VolleyRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by anuj5 on 02-01-2018.
 */

public class TracksDao extends VolleyRequest {

    RequestCallback requestCallback;

    public TracksDao(Context context, RequestCallback requestCallback) {
        super(context);
        this.requestCallback = requestCallback;
    }

    public void getTracksWithQuery(String query, int limit) {
        query = Utilities.encodeKeyword(query);
        String url = Utilities.getApiUrlTracksQuery(query, limit);
        callApiForArray(url, new DaoCallback() {
            @Override
            public void response(Object response) {
                JSONArray jsonArrayResponse = (JSONArray) response;
                ArrayList<Song> songs = new ArrayList<>();
                if (jsonArrayResponse.length() > 0) {
                    for (int i = 0; i < jsonArrayResponse.length(); i++) {
                        try {
                            Song song = new Song(jsonArrayResponse.getJSONObject(i));
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                requestCallback.onListRequestSuccessful(songs, Constants.SEARCH_SONGS_WITH_QUERY, true);
            }

            @Override
            public void stringResponse(String response) {

            }

            @Override
            public void errorResponse(VolleyError error) {
                requestCallback.onListRequestSuccessful(null, Constants.SEARCH_SONGS_WITH_QUERY, true);
            }
        }, Constants.METHOD_GET, null);

    }

    public void getTrackWithId(final String songId) {
        String url = Utilities.getApiUrlTrackId(songId);
        callApiForObject(url, new DaoCallback() {
            @Override
            public void response(Object response) {
                JSONObject jsonObjectResponse = (JSONObject) response;
                Song song = new Song(jsonObjectResponse);
                requestCallback.onObjectRequestSuccessful(song, Constants.SEARCH_SONG_WITH_ID, true);
            }

            @Override
            public void stringResponse(String response) {

            }

            @Override
            public void errorResponse(VolleyError error) {
                requestCallback.onObjectRequestSuccessful(null, Constants.SEARCH_SONG_WITH_ID, false);
            }
        }, Constants.METHOD_GET, null);
    }
}
