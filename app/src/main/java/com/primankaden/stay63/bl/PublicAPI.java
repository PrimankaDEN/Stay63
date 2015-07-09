package com.primankaden.stay63.bl;


import android.net.Uri;
import android.util.Log;

import com.primankaden.stay63.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class PublicAPI {
    private static final String CLIENT_ID = "test";
    private static final String SALT = "just_f0r_tests";
    public static final String CLASSIFIER_FULL_STOPS = " http://tosamara.ru/api/classifiers/stopsFullDB.xml";
    private static final String TAG = "PublicAPI";

    private static String generateAuthKey(String message) {
        return Utils.SHA1(message.concat(SALT));
    }

    public static String getXml(String url) throws IOException {
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

    //?method=getFirstArrivalToStop&KS_ID=123&COUNT=10&os=android&clientid=appName%2F1.0&authkey=b1b3773a05c0ed0176787a4f1574ff0075f7521e
    public static String getTransportXml(String stopId, int count) throws IOException {
        String response = "";
        String authKey = generateAuthKey(stopId + count);
        String url = new Uri.Builder().scheme("http").authority("tosamara.ru").path("api/xml")
                .appendQueryParameter("method", "getFirstArrivalToStop")
                .appendQueryParameter("KS_ID", stopId)
                .appendQueryParameter("COUNT", String.valueOf(count))
                .appendQueryParameter("os", "android")
                .appendQueryParameter("clientid", CLIENT_ID)
                .appendQueryParameter("authkey", authKey).build().toString();
        URL obj = new URL(url);
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
}
