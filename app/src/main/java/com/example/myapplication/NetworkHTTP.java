package com.example.myapplication;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkHTTP {

    // Returns an HttpURLConnection object from a given url-string
    protected static HttpURLConnection urlToHTTPConnection(String url) {

        URL postgrestEndpoint = null;
        HttpURLConnection conn = null;
        try {
            // Creation of URL
            postgrestEndpoint = new URL(url);

            // Creation of the connection
            conn = (HttpURLConnection) postgrestEndpoint.openConnection();
        } catch (MalformedURLException e) {
            Log.d("MalformedURL", e.getMessage());
        } catch (IOException e) {
            Log.d("IO Exception", e.getMessage());
        }

        // Adding HTTP header properties
        conn.setRequestProperty("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAxOSJ9.3MGDqJYkivAsiMOXwvoPTD6_LTCWkP3RvI2zpzoB1XE");
        conn.setRequestProperty("Content-Type", "application/json");
        return conn;
    }


}
