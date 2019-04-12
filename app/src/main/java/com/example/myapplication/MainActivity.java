package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity {
    DrawerLayout dl;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv1 = findViewById(R.id.textView7);
        tv1.setTextSize(15.0f);
        tv1.setText("DM564 test app" + "\n");
        tv1.append("1. Send message - virker næsten (ish) \n2. HttpURLConnection - virker \n3. Volley - virker");
        toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        dl = (DrawerLayout)findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigationView);

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id) {
                    case R.id.Send_Messages_Item:
                        Toast.makeText(MainActivity.this, "Opening messages", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, DisplayMessageActivity.class));
                        dl.closeDrawers();
                        return true;
                    case R.id.HttpURL_Item:
                        Toast.makeText(MainActivity.this, "Opening HttpURLConnection networking", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, HttpURLConnActivity.class));
                        dl.closeDrawers();
                        return true;
                    case R.id.volley_subitem_GET:
                        Toast.makeText(MainActivity.this, "Opening Volley networking", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, VolleyActivity.class);
                        intent.putExtra("Volley_GET", true);
                        startActivity(intent);
                        dl.closeDrawers();
                        return true;
                    case R.id.volley_subitem_POST:
                        Toast.makeText(MainActivity.this, "Opening Volley networking", Toast.LENGTH_SHORT).show();
                        Intent intent2 = new Intent(MainActivity.this, VolleyActivity.class);
                        intent2.putExtra("Volley_GET", false);
                        startActivity(intent2);
                        dl.closeDrawers();
                    default:
                        return true;
                }
            }
        });

//        displayUsers();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                dl.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



}


