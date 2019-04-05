package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myapplication.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayUsers();
    }

    /*
     * Called when the user taps send
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = findViewById(R.id.editText).toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void displayUsers() {
        TextView tv = findViewById(R.id.textView2);
        HttpResponse<String> jsonResponse = Unirest.get("http://caracal.imada.sdu.dk/app2019/users")
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYXBwMjAxOSJ9.3MGDqJYkivAsiMOXwvoPTD6_LTCWkP3RvI2zpzoB1XE")
                .header("Content-type","application/json")
                .asString();
        String body = jsonResponse.getBody();
        //tv.setText(body);
        Log.d("Hej", "body");
    }

}


