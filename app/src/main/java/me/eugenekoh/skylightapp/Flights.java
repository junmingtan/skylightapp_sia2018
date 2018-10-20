package me.eugenekoh.skylightapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import me.eugenekoh.skylightapp.utils.Tools;

public class Flights extends AppCompatActivity {

    private TabLayout tab_layout;
    private ActionBar actionBar;
    private NestedScrollView nested_scroll_view;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flights);
        initToolbar();
        initCardView();
        checkBidCompleted();
    }

    private void initCardView() {
        View button = findViewById(R.id.travelflex);
        button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sp = getSharedPreferences("bumpbid",MODE_PRIVATE);
                    if (sp.getBoolean("info",false)) {
                        startActivity(new Intent(Flights.this, Bumpbid.class));
                    } else {
                        startActivity(new Intent(Flights.this, BumpbidInfo.class));
                    }
                }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setTitle("My Flights");
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
            startActivity(new Intent(Flights.this, LoginCardLight.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkBidCompleted() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("bids").document("SQ890");
        final String TAG = "XiaoQi";
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {

                    Log.d(TAG, "DocumentSnapshot data: else loop" + snapshot.getData().get("isOngoing"));
                    Boolean isOngoing = (Boolean) snapshot.getData().get("isOngoing");
                    if(!isOngoing) {
                        initAccepted();
                        initNavigation();
                    }
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void initAccepted(){
        TextView tv = findViewById(R.id.depart_time);
        tv.setText("Depart at 12.35pm, 27 Oct 2018");
        tv = findViewById(R.id.arrive_time);
        tv.setText("Arrive at 7.35pm, 27 Oct 2017");
        tv = findViewById(R.id.flightnum);
        tv.setText("SQ 317");
        tv = findViewById(R.id.boarding_time);
        tv.setText("12.05pm");
        tv = findViewById(R.id.gate);
        tv.setText("A9");
        tv = findViewById(R.id.seat);
        tv.setText("28C");
        tv = findViewById(R.id.bid_details);
        tv.setText("BumpBid confirmed. Click for more details");
    }
    private void initNavigation(){
        BottomNavigationView navi = findViewById(R.id.navigation);
        navi.setVisibility(View.VISIBLE);
        navi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_chat:
                        startActivity(new Intent(Flights.this, Chat.class));
                        finish();
                        return true;
                    case R.id.navigation_flight:
                        return true;
                    case R.id.navigation_travel:
                        startActivity(new Intent(Flights.this, Travel.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}
