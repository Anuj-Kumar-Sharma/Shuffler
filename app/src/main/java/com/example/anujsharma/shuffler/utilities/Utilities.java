package com.example.anujsharma.shuffler.utilities;

import com.example.anujsharma.shuffler.volley.Urls;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by anuj5 on 02-01-2018.
 */

public class Utilities {

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
}
