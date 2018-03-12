package com.example.anujsharma.shuffler.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.example.anujsharma.shuffler.models.Song;
import com.example.anujsharma.shuffler.volley.Urls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by anuj5 on 02-01-2018.
 */

public class Utilities {

    public static String TAG = "UTILITY_TAG";

    public static String encodeKeyword(String keyword) {
        keyword = keyword.replaceAll("[^\\.\\-\\w\\s]", " ");
        keyword = keyword.replaceAll("\\s+", " ");
        keyword = keyword.trim();
        try {
            keyword = (URLEncoder.encode(keyword, "UTF-8")).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return keyword;
    }

    public static String getApiUrlTracksQuery(String query, int limit) {
        return Urls.TRACKS + "?&q=" + query + "&limit=" + limit + "&filter=public&client_id=" + Urls.CLIENT_ID;
    }

    public static String getApiUrlTrackId(String songId) {
        return Urls.TRACKS + "/" + songId + "?client_id=" + Urls.CLIENT_ID;
    }

    public static String getApiUrlUsersQuery(String query, int limit) {
        return Urls.USERS + "?&q=" + query + "&limit=" + limit + "&filter=public&client_id=" + Urls.CLIENT_ID;
    }

    public static String getApiUrlUserId(String userId) {
        return Urls.USERS + "/" + userId + "?client_id=" + Urls.CLIENT_ID;
    }

    public static String getApiUrlPlaylistsQuery(String query, int limit) {
        return Urls.PLAYLISTS + "?&q=" + query + "&limit=" + limit + "&filter=public&client_id=" + Urls.CLIENT_ID;
    }

    public static String getApiUrlPlaylistId(String playlistId) {
        return Urls.PLAYLISTS + "/" + playlistId + "?limit=200&client_id=" + Urls.CLIENT_ID;
    }

    public static String getApiUrlTrackOfUser(String userId, int limit) {
        return Urls.USERS + "/" + userId + "/tracks?limit=" + limit + "&client_id=" + Urls.CLIENT_ID;
    }

    public static String formatInteger(int n) {
        // a.b
        if (n > 1000000) {
            int a = n / 1000000;
            int b = n % 1000000;
            b = b / 100000;
            StringBuilder ans = new StringBuilder();
            ans.append(a);
            if (b == 0) {
                return ans.append("M").toString();
            }
            ans.append(".").append(b).append("M");
            return ans.toString();
        }
        if (n > 1000) {
            int a = n / 1000;
            int b = n % 1000;
            b = b / 100;
            StringBuilder ans = new StringBuilder();
            ans.append(a);
            if (b == 0) {
                return ans.append("K").toString();
            }
            ans.append(".").append(b).append("K");
            return ans.toString();
        }
        return String.valueOf(n);
    }

    public static String formatTime(long n) {
        long s = n / 1000;
        return String.format("%d:%02d", s / 60, s % 60);
    }

    public static String formatIntegerWithCommas(int followersCount, String append) {
        return NumberFormat.getNumberInstance(Locale.US).format(followersCount) + append;
    }

    public static int getSelectedPosition(Context context, List<Song> songs, int offset) {
        SharedPreference pref = new SharedPreference(context);
        long currentPos = pref.getCurrentPlayingSong();
        for (Song song : songs) {
            if (song.getId() == currentPos) return offset;
            offset++;
        }
        return -1;
    }

    public static String getLargeArtworkUrl(String songArtwork) {
        // -large.jpg
        if (songArtwork != null && !songArtwork.isEmpty()) {
            songArtwork = songArtwork.replace("large.jpg", "t500x500.jpg");
        }
        return songArtwork;
    }

    public static Bitmap mergeThemAll(List<Bitmap> orderImagesList) {
        Bitmap result = null;
        if (orderImagesList != null && orderImagesList.size() > 0) {

            result = Bitmap.createBitmap(orderImagesList.get(0).getWidth() * 2, orderImagesList.get(0).getHeight() * 2, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            for (int i = 0; i < orderImagesList.size(); i++) {
                canvas.drawBitmap(orderImagesList.get(i), orderImagesList.get(i).getWidth() * (i % 2), orderImagesList.get(i).getHeight() * (i / 2), paint);
            }
        } else {
            Log.e("MergeError", "Couldn't merge bitmaps");
        }

        return result;

    }
}
