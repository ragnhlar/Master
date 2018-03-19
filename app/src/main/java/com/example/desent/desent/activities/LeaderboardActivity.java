package com.example.desent.desent.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.desent.desent.R;
import com.example.desent.desent.SessionManagement;
import com.example.desent.desent.models.Constants;
import com.example.desent.desent.models.Indicator;
import com.example.desent.desent.models.RequestHandler;
import com.example.desent.desent.models.Score;
import com.example.desent.desent.models.ScoreAdapter;
import com.example.desent.desent.utils.EstimationType;
import com.example.desent.desent.utils.TimeScale;
import com.example.desent.desent.utils.Utility;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LeaderboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressDialog progressDialog;

    DrawerLayout drawer;

    List<Score> scoreList;
    RecyclerView recyclerView;
    ScoreAdapter adapter;

    FrameLayout frameLayout;

    BottomNavigationViewEx bnveSort;

    Boolean sortListByAvgCf = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setUpNavigationView();

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.rvLeaderboard);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //getting the framelayout from xml
        frameLayout = (FrameLayout) findViewById(R.id.frameLayoutPodium);

        //initializing the scorelist
        scoreList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        loadScores(sortListByAvgCf);

        bnveSort = (BottomNavigationViewEx) findViewById(R.id.navLeaderboardSort);
        bnveSort.setSelectedItemId(R.id.best_avg_cf);
        bnveSort.enableAnimation(true);
        bnveSort.enableShiftingMode(false);
        bnveSort.enableItemShiftingMode(false);
        bnveSort.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.selector_time_navigation_white_grey));
        bnveSort.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));
        //bnveSort.setIconSize(25,25);
        bnveSort.setTextSize(15);
        //bnveSort.setIconsMarginTop(20);

        bnveSort.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.best_avg_cf:
                        bnveSort.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.selector_time_navigation_white_grey));
                        bnveSort.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));

                        //sortListByAvgCf();

                        sortListByAvgCf = true;

                        loadScores(sortListByAvgCf);

                        /*if (carbonFootprint.getEstimationType() == EstimationType.NONE)
                            informationCO2Left.setVisibility(VISIBLE);
                        textViewTimeScale.setVisibility(View.GONE);
                        for (Indicator indicator: indicators)
                            indicator.setTimeScale(TimeScale.TODAY);*/
                        break;

                    case R.id.most_ec:
                        bnveSort.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));
                        bnveSort.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));

                        //sortListByEarthCoins();

                        sortListByAvgCf = false;

                        loadScores(sortListByAvgCf);


                        /*informationCO2Left.setVisibility(GONE);
                        textViewTimeScale.setVisibility(View.VISIBLE);
                        textViewTimeScale.setText("Average this week");
                        for (Indicator indicator: indicators)
                            indicator.setTimeScale(TimeScale.WEEK);*/
                        break;
                }
                return true;
            }
        });
        
        //loadScores();
        /*
        scoreList.add(
                new Score(
                        1,
                        "Navn Navnesen 1",
                        35,
                        4.5,
                        R.drawable.earth1));

        scoreList.add(
                new Score(
                        2,
                        "Navn Navnesen 2",
                        43,
                        3.2,
                        R.drawable.earth2));

        scoreList.add(
                new Score(
                        3,
                        "Navn Navnesen 3",
                        13,
                        6.7,
                        R.drawable.earth4));
        */
        //creating recyclerview adapter
        //ScoreAdapter adapter = new ScoreAdapter(this, scoreList);

        //setting adapter to recyclerview
        //recyclerView.setAdapter(adapter);
    }

    private void sortListByEarthCoins() {
    }

    private void sortListByAvgCf() {
    }

    private void loadScores(final Boolean sortListByAvgCf) {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Constants.URL_RETRIEVE_ALL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            progressDialog.dismiss();
                            if (scoreList.isEmpty()){
                                JSONArray scores = new JSONArray(response);
                                for (int i = 0; i < scores.length(); i++){
                                    JSONObject scoreObject = scores.getJSONObject(i);

                                    int id = scoreObject.getInt("id");
                                    String email = scoreObject.getString("email");
                                    //String name = scoreObject.getString("name");
                                    int num_coins = scoreObject.getInt("num_coins");
                                    /*int walk = scoreObject.getInt("walk");
                                    int cycle = scoreObject.getInt("cycle");
                                    int drive = scoreObject.getInt("drive");*/
                                    double avg_cf = scoreObject.getDouble("avg_cf");
                                    //int image = scoreObject.getInt("image");

                                    Score score = new Score(id, email, num_coins, avg_cf, R.drawable.earth1);
                                    scoreList.add(score);
                                }
                            }

                            if (sortListByAvgCf){
                                // sort list by lowest to highest average carbon footprint
                                Collections.sort(scoreList, new Comparator<Score>() {
                                    @Override
                                    public int compare(Score score, Score t1) {
                                        return Double.compare(score.getAvg_cf(), t1.getAvg_cf());
                                    }
                                });
                            } else {
                                //sort list by highest to lowest number of Earth Coins
                                Collections.sort(scoreList, new Comparator<Score>() {
                                    @Override
                                    public int compare(Score score, Score t1) {
                                        Integer num_coins_1 = score.getNum_coins();
                                        Integer num_coins_2 = t1.getNum_coins();
                                        return num_coins_2.compareTo(num_coins_1);
                                    }
                                });
                            }
                            //creating recyclerview adapter
                            adapter = new ScoreAdapter(LeaderboardActivity.this, scoreList);

                            //setting adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }

    protected void setUpNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.title_nav_header)).setText(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_personal_name", ""));
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.subtitle_nav_header)).setText(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_personal_email", ""));
        ImageView profilePicture = navigationView.getHeaderView(0).findViewById(R.id.imageView);

        Uri imageUri = null;
        try {
            imageUri = Uri.parse(PreferenceManager.getDefaultSharedPreferences(this).getString("pref_key_profile_picture", "android.resource://com.example.desent.desent/drawable/earth"));
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(imageUri));
            profilePicture.setImageBitmap(Utility.getCroppedBitmap(bitmap));
            bitmap.recycle();
            bitmap = null;
            System.gc();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(LeaderboardActivity.this, MainActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(LeaderboardActivity.this, HistoryActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(LeaderboardActivity.this, SettingsActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_user_profile) {
            startActivity(new Intent(LeaderboardActivity.this, ProfileActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_friend_list) {
            startActivity(new Intent(LeaderboardActivity.this, FriendsActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_leaderboard) {
            //startActivity(new Intent(LeaderboardActivity.this, ProfileActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_info_app) {
            startActivity(new Intent(LeaderboardActivity.this, InformationActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_log_out){
            SessionManagement session = new SessionManagement(getApplicationContext());
            session.logoutUser();
            drawer.closeDrawer(GravityCompat.START);
        }
        /*else if (id == R.id.nav_about_us) {
            drawer.closeDrawer(GravityCompat.START);
        }*/
        return true;
    }
}
