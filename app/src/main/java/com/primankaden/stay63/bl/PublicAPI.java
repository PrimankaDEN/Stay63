package com.primankaden.stay63.bl;


import android.net.Uri;
import android.util.Log;

import com.primankaden.stay63.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class PublicAPI {
    private static final String CLIENT_ID = "test";
    private static final String SALT = "just_f0r_tests";
    private static final String CLASSIFIER_FULL_STOPS = " http://tosamara.ru/api/classifiers/stopsFullDB.xml";
    private static final String TAG = "PublicAPI";

    public static String getFullStopList() throws IOException {
        return getXml(PublicAPI.CLASSIFIER_FULL_STOPS);
    }

    //?method=getFirstArrivalToStop&KS_ID=123&COUNT=10&os=android&clientid=appName%2F1.0&authkey=b1b3773a05c0ed0176787a4f1574ff0075f7521e
    public static String getFirstArrivalToStop(String stopId, int count) throws IOException {
        String authKey = generateAuthKey(stopId + count);
        String url = new UrlBuilder("getFirstArrivalToStop").append("KS_ID", stopId).append("COUNT", String.valueOf(count)).authkey(authKey).build();
        return sendGETRequest(url);
    }

    //?method=getRouteArrivalToStop&KS_ID=123&KR_ID=10&os=android&clientid=appName%2F1.0&authkey=b1b3773a05c0ed0176787a4f1574ff0075f7521e
    public static String getRouteArrivalToStop(String stopId, String routeId) throws IOException {
        String authKey = generateAuthKey(routeId.concat(stopId));
        String url = new UrlBuilder("getRouteArrivalToStop").append("KS_ID", stopId).append("KR_ID", routeId).authkey(authKey).build();
        return sendGETRequest(url);
    }

    //?method=getRouteSchedule&KR_ID=123&day=10.07.2013&os=android&clientid=appName%2F1.0&authkey=b1b3773a05c0ed0176787a4f1574ff0075f7521e
    public static String getRouteSchedule(String routeId, Date day) throws IOException {
        String date = Utils.apiDateFormat(day);
        String authKey = generateAuthKey(routeId.concat(date));
        String url = new UrlBuilder("getRouteArrivalToStop").append("KR_ID", routeId).append("day", date).authkey(authKey).build();
        return sendGETRequest(url);
    }

    //?method=getTransportPosition&HULLNO=31676&os=android&clientid=appName%2F1.0&authkey=b1b3773a05c0ed0176787a4f1574ff0075f7521e
    public static String getTransportPosition(String transportId) throws IOException {
        String authKey = generateAuthKey(transportId);
        String uri = new UrlBuilder("getTransportPosition").append("HULLNO", transportId).authkey(authKey).build();
        return sendGETRequest(uri);
    }

    private static String sendGETRequest(String request) throws IOException {
        String response = "";
        URL obj = new URL(request);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        Log.d(TAG, con.toString());
        int responseCode = con.getResponseCode();
        Log.d(TAG, "Response Code : " + responseCode + " " + con.getResponseMessage());
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder responseBuf = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                responseBuf.append(inputLine);
            }
            in.close();
            response = responseBuf.toString();
        }
        return response;
    }

    private static String generateAuthKey(String message) {
        return Utils.SHA1(message.concat(SALT));
    }

    private static String getXml(String url) throws IOException {
        URL oURL;
        URLConnection oConnection;
        BufferedReader oReader;
        String sLine;
        StringBuilder sbResponse;
        String sResponse;
        oURL = new URL(url);
        oConnection = oURL.openConnection();
        oReader = new BufferedReader(new InputStreamReader(oConnection.getInputStream()));
        sbResponse = new StringBuilder();

        while ((sLine = oReader.readLine()) != null) {
            sbResponse.append(sLine);
        }
        sResponse = sbResponse.toString();
        return sResponse;
    }

    private static class UrlBuilder {
        private final Uri.Builder builder;

        UrlBuilder(String method) {
            builder = new Uri.Builder().scheme("http").authority("tosamamra.ru").path("api/xml")
                    .appendQueryParameter("method", method);
        }

        UrlBuilder append(String key, String value) {
            builder.appendQueryParameter(key, value);
            return this;
        }

        UrlBuilder authkey(String authKey) {
            builder.appendQueryParameter("authkey", authKey);
            return this;
        }

        String build() {
            return builder
                    .appendQueryParameter("os", "android")
                    .appendQueryParameter("clientid", CLIENT_ID)
                    .build().toString();
        }
    }
}
