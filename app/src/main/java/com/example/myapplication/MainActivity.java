package com.example.myapplication;

import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    public static final String EXTRA_MESSAGE = "com.example.myapplication.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        displayUsers();
    }

    /*
     * Called when the user taps send
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = findViewById(R.id.editText);
        String message = editText.toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void displayUsers() {
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
                                Log.d("Inner-loop", "true");
                            }
                            reader.endObject();
                            Log.d("Outer-loop", "True");
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


