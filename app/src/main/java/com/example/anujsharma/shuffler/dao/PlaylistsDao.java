package com.example.anujsharma.shuffler.dao;

import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.anujsharma.shuffler.models.Playlist;
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
 * Created by anuj5 on 07-01-2018.
 */

public class PlaylistsDao extends VolleyRequest {

    RequestCallback requestCallback;

    public PlaylistsDao(Context context, RequestCallback requestCallback) {
        super(context);
        this.requestCallback = requestCallback;
    }

    public void getPlaylistsWithQuery(String query, int limit) {
        query = Utilities.encodeKeyword(query);
        String url = Utilities.getApiUrlPlaylistsQuery(query, limit);
        callApiForArray(url, new DaoCallback() {
            @Override
            public void response(Object response) {
                JSONArray jsonArrayResponse = (JSONArray) response;
                ArrayList<Playlist> playlists = new ArrayList<>();
                if (jsonArrayResponse.length() > 0) {
                    for (int i = 0; i < jsonArrayResponse.length(); i++) {
                        try {
                            Playlist playlist = new Playlist(jsonArrayResponse.getJSONObject(i));
                            playlists.add(playlist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                requestCallback.onListRequestSuccessful(playlists, Constants.SEARCH_PLAYLISTS_WITH_QUERY, true);
            }

            @Override
            public void stringResponse(String response) {

            }

            @Override
            public void errorResponse(VolleyError error) {
                requestCallback.onListRequestSuccessful(null, Constants.SEARCH_PLAYLISTS_WITH_QUERY, false);
            }
        }, Constants.METHOD_GET, null);

    }

    public void getTracksFromPlaylistId(long playlistId) {
        String url = Utilities.getApiUrlPlaylistId(String.valueOf(playlistId));
        callApiForObject(url, new DaoCallback() {
            @Override
            public void response(Object response) {
                JSONObject jsonObject = (JSONObject) response;
                ArrayList<Song> songs = new ArrayList<>();
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("tracks");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Song song = new Song(jsonArray.getJSONObject(i));
                            songs.add(song);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                requestCallback.onListRequestSuccessful(songs, Constants.SEARCH_SONG_WITH_PLAYLIST_ID, true);
            }

            @Override
            public void stringResponse(String response) {

            }

            @Override
            public void errorResponse(VolleyError error) {
                requestCallback.onListRequestSuccessful(null, Constants.SEARCH_SONG_WITH_PLAYLIST_ID, false);
            }
        }, Constants.METHOD_GET, null);
    }

    public void getPlaylistFromPlaylistId(long playlistId) {
        String url = Utilities.getApiUrlPlaylistId(String.valueOf(playlistId));
        Log.d("TAG", url);
        callApiForObject(url, new DaoCallback() {
            @Override
            public void response(Object response) {
                JSONObject jsonObject = (JSONObject) response;
                Playlist playlist = new Playlist(jsonObject);
                requestCallback.onObjectRequestSuccessful(playlist, Constants.SEARCH_PLAYLISTS_WITH_ID, true);
            }

            @Override
            public void stringResponse(String response) {

            }

            @Override
            public void errorResponse(VolleyError error) {
                requestCallback.onObjectRequestSuccessful(null, Constants.SEARCH_PLAYLISTS_WITH_ID, false);
            }
        }, Constants.METHOD_GET, null);
    }
}
