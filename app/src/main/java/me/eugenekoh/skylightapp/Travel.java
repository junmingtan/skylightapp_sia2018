package me.eugenekoh.skylightapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import me.eugenekoh.skylightapp.utils.Tools;
import me.eugenekoh.skylightapp.utils.ViewAnimation;

public class Travel extends AppCompatActivity {

    private TabLayout tab_layout;
    private ActionBar actionBar;
    private NestedScrollView nested_scroll_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        initToolbar();
        initComponent();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Itinerary");
        actionBar.setDisplayHomeAsUpEnabled(false);
        Tools.setSystemBarColor(this, R.color.grey_20);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_60));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_SHORT).show();
            SharedPreferences sp = getSharedPreferences("login",MODE_PRIVATE);
            sp.edit().putBoolean("logged", false).apply();
            startActivity(new Intent(Travel.this, LoginCardLight.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponent() {
        BottomNavigationView navi = findViewById(R.id.navigation);
        navi.setVisibility(View.VISIBLE);
        navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_chat:
                        startActivity(new Intent(Travel.this, Chat.class));
                        finish();
                        return true;
                    case R.id.navigation_flight:
                        startActivity(new Intent(Travel.this, Flights.class));
                        finish();
                        return true;
                    case R.id.navigation_travel:
                        return true;
                }
                return false;
            }
        });
    }
}
