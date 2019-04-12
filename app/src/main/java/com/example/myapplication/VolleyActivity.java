package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

public class VolleyActivity extends AppCompatActivity {

    TextView tv;
    EditText uid;
    EditText uName;
    boolean volley_GET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        volley_GET = getIntent().getExtras().getBoolean("Volley_GET");

        // Layout for volley GET
        tv = findViewById(R.id.textView3);
        Button button = (Button) findViewById(R.id.button2);
        tv.setVisibility(View.GONE);
        button.setVisibility(View.GONE);

        // Layout for Volley POST
        Button button2 = (Button) findViewById(R.id.button4);
        TextView tv1 = findViewById(R.id.textView4);
        TextView tv2 = findViewById(R.id.textView5);
        uid = (EditText) findViewById(R.id.editText2);
        uName = (EditText) findViewById(R.id.editText3);
        tv1.setVisibility(View.GONE);
        tv2.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
        uid.setVisibility(View.GONE);
        uName.setVisibility(View.GONE);

        if (volley_GET) {
            tv.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayUsers();
                }
            });
        } else if (!volley_GET) {
            tv1.setVisibility(View.VISIBLE);
            tv2.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            uid.setVisibility(View.VISIBLE);
            uName.setVisibility(View.VISIBLE);

            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postUser();
//                    Intent intent = new Intent(VolleyActivity.this, MainActivity.class);
                }
            });
        }
    }

    private void displayUsers() {
        // Cache and network for the requestqueue
        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate a new requestqueue (for network requests)
        RequestQueue queue = new RequestQueue(cache, network);
        String url = "http://caracal.imada.sdu.dk/app2019/users";

        queue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                tv.setText("Users:\n" + JsonToString(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv.setText("Oops error in HTTP request");
            }
        }) { // Following method override is an override of the getHeaders method in the StringRequest class
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAxOSJ9.3MGDqJYkivAsiMOXwvoPTD6_LTCWkP3RvI2zpzoB1XE");
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        queue.add(stringRequest);
    }

    private String JsonToString(String response) {
        // The parsed string
        String finalString = "";

        JsonReader reader = new JsonReader(new StringReader(response));

        try {
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
            return finalString;
        } catch (IOException e) {
            Log.d("IOException", e.getMessage());
        }
        return finalString;
    }

    private void postUser() {
        // Cache and network for the requestqueue
        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
        Network network = new BasicNetwork(new HurlStack());

        // Instantiate a new requestqueue (for network requests)
        RequestQueue queue = new RequestQueue(cache, network);
        String url = "http://caracal.imada.sdu.dk/app2019/users";

        queue.start();

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplication(), "User submitted!", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VolleyActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAxOSJ9.3MGDqJYkivAsiMOXwvoPTD6_LTCWkP3RvI2zpzoB1XE");
                params.put("Content-Type", "application/json");
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id",uid.getText().toString().trim());
                params.put("name", uName.getText().toString().trim());
//                params.put("stamp", stamp.toString());
                return params;
            }
        };

        queue.add(postRequest);
    }


}
