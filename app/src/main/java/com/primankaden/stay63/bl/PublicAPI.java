package com.primankaden.stay63.bl;


import android.net.Uri;
import android.util.Log;
import android.util.Xml;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.primankaden.stay63.Stay63Application;
import com.primankaden.stay63.Utils;

import org.apache.http.client.methods.HttpPost;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PublicAPI {
    private static final String CLIENT_ID = "test";
    private static final String SALT = "just_f0r_tests";
    private static final String OS = "android";

    public static final String CLASSIFIER_STOPS = "http://tosamara.ru/api/classifiers/stops.xml";
    public static final String CLASSIFIER_FULL_STOPS = " http://tosamara.ru/api/classifiers/stopsFullDB.xml";
    public static final String CLASSIFIER_ROUTES = " http://tosamara.ru/api/classifiers/routes.xml";
    public static final String ROUTES_AND_STOPS_CORRESPONDENCE = "http://tosamara.ru/api/classifiers/routesAndStopsCorrespondence.xml";
    public static final String GEOPORTALS_STOPS_CORRESPONDENCE = "http://tosamara.ru/api/classifiers/GeoportalStopsCorrespondence.xml";
    public static final String REQUEST_ADDRESS = "tosamara.ru/api/xml";
    private static final String TAG = "PublicAPI";

    private static String generateAuthKey(String message) {
        return Utils.SHA1(message.concat(SALT));
    }

    public static String getXml(String url) {
        URL oURL;
        URLConnection oConnection;
        BufferedReader oReader;
        String sLine;
        StringBuilder sbResponse;
        String sResponse = "";
        try {
            oURL = new URL(url);
            oConnection = oURL.openConnection();
            oReader = new BufferedReader(new InputStreamReader(oConnection.getInputStream()));
            sbResponse = new StringBuilder();

            while ((sLine = oReader.readLine()) != null) {
                sbResponse.append(sLine);
            }
            sResponse = sbResponse.toString();
        } catch (Exception e) {
            Log.w(TAG, e.getLocalizedMessage());
        }

        return sResponse;
    }

    //?method=getFirstArrivalToStop&KS_ID=123&COUNT=10&os=android&clientid=appName%2F1.0&authkey=b1b3773a05c0ed0176787a4f1574ff0075f7521e
    public static String getTransportXml(String stopId, int count) {
        String response = "";
        try {
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
                StringBuffer responseBuf = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    responseBuf.append(inputLine);
                }
                in.close();
                response = responseBuf.toString();
            }
        } catch (IOException e) {
            Log.w(TAG, e.getLocalizedMessage());
        }
        return response;
    }
}
