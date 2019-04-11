package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HttpURLConnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_urlconn);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayUsers();
            }
        });
    }


    private void displayUsers() {
        try {
            // Install an HttpResponseCache for more responsiveness
            HttpResponseCache httpCache = HttpResponseCache.install(getCacheDir(),100000L);
        } catch (IOException e) {
            Log.d("IO Exception", e.getMessage());
        }

        // Network operations must not be run in the UI (main) thread
        final ExecutorService exec = Executors.newSingleThreadExecutor();

        // Future that concenates a string of all usernames in the db (at the time of initialization)
        Future<String> future = exec.submit(new Callable<String>() {

            @Override
            public String call() {

                // Using the self-made NetworkHTTP class to establish a connection object to the given endpoint
                HttpURLConnection conn = NetworkHTTP.urlToHTTPConnection("http://caracal.imada.sdu.dk/app2019/users");

                try {
                    if (conn.getResponseCode()==200) {
                        InputStream responseBody = null;
                        InputStreamReader respBodyReader = null;

                        // Initialize an InputStream, InputStreamReader and JsonReader for the connection
                        responseBody = conn.getInputStream();
                        respBodyReader = new InputStreamReader(responseBody, "UTF-8");
                        JsonReader reader = new JsonReader(respBodyReader);

                        String finalString = "Users:\n";

                        // The JSON file from the http request is a JSON array with JSON objects inside
                        reader.beginArray();
                        while (reader.hasNext()) {
                            reader.beginObject();
                            while (reader.hasNext()) {
                                if (reader.nextName().equals("id")) {
                                    finalString = finalString + "\n" + reader.nextString();
                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();
                        }
                        reader.endArray();
                        reader.close();
                        respBodyReader.close();
                        responseBody.close();
                        return finalString;
                    } else {
                        return "Bad HTTP response";
                    }
                } catch (IOException e) {
                    Log.d("IOException", e.getMessage());
                    return "IO Exception";
                }
            }
        });
       TextView view = null;
       try {
           view = findViewById(R.id.textView2);
           view.setText(future.get(), TextView.BufferType.NORMAL);
       } catch (ExecutionException e) {
           Log.d("Execution Exception", e.getMessage());
       } catch (InterruptedException e) {
           Log.d("Interrupted exception", e.getMessage());
       }
    }
}
