package com.example.desent.desent.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
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
import java.util.Iterator;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LeaderboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressDialog progressDialog;

    DrawerLayout drawer;

    List<Score> scoreList;
    List<Score> tempList;
    RecyclerView recyclerView;
    ScoreAdapter adapter;

    BottomNavigationViewEx bnveSort;

    Boolean sortListByAvgCf = true;

    TextView tvNameFirstPlace, tvAvgCfFirstPlace, tvNumCoinsFirstPlace,
            tvNameSecondPlace, tvAvgCfSecondPlace, tvNumCoinsSecondPlace,
            tvNameThirdPlace, tvAvgCfThirdPlace, tvNumCoinsThirdPlace;
    ImageView imgFirstPlace, imgSecondPlace, imgThirdPlace;

    LinearLayout llFirst, llSecond, llThird;

    FrameLayout frameLayout;

    SharedPreferences sharedPreferences;

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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setUpNavigationView();

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.rvLeaderboard);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //getting the framelayout from xml
        frameLayout = (FrameLayout) findViewById(R.id.frameLayoutPodium);

        tvNameFirstPlace = findViewById(R.id.tvNameFirstPlace);
        tvAvgCfFirstPlace = findViewById(R.id.tvAvgCfFirstPlace);
        imgFirstPlace = findViewById(R.id.imgFirstPlace);
        tvNumCoinsFirstPlace = findViewById(R.id.tvNumCoinsFirstPlace);
        llFirst = findViewById(R.id.llNumCoinsFirst);

        tvNameSecondPlace = findViewById(R.id.tvNameSecondPlace);
        tvAvgCfSecondPlace = findViewById(R.id.tvAvgCfSecondPlace);
        imgSecondPlace = findViewById(R.id.imgSecondPlace);
        tvNumCoinsSecondPlace = findViewById(R.id.tvNumCoinsSecondPlace);
        llSecond = findViewById(R.id.llNumCoinsSecond);

        tvNameThirdPlace = findViewById(R.id.tvNameThirdPlace);
        tvAvgCfThirdPlace = findViewById(R.id.tvAvgCfThirdPlace);
        imgThirdPlace = findViewById(R.id.imgThirdPlace);
        tvNumCoinsThirdPlace = findViewById(R.id.tvNumCoinsThirdPlace);
        llThird = findViewById(R.id.llNumCoinsThird);

        //initializing the scorelist
        scoreList = new ArrayList<>();

        //"friends" inserted until server is up an running
        scoreList.add(new Score(0, sharedPreferences.getString("pref_key_personal_name",""), 167, 3.6, R.drawable.earth));
        scoreList.add(new Score(1, "Rob Adams", 135, 3.2, R.drawable.robadams));
        scoreList.add(new Score(2, "Mary Jones", 230, 2.2, R.drawable.maryjones));
        scoreList.add(new Score(3, "Patricia Clarkson", 53, 6.2, R.drawable.patriciaclarkson));
        scoreList.add(new Score(4, "Michael Smith", 310, 1.2, R.drawable.michaelsmith));
        scoreList.add(new Score(5, "James Pitt", 32, 6.4, R.drawable.jamespitt));

        tempList = new ArrayList<>();

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
                        tempList.clear();

                        bnveSort.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.selector_time_navigation_white_grey));
                        bnveSort.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));

                        llFirst.setVisibility(View.GONE);
                        llSecond.setVisibility(View.GONE);
                        llThird.setVisibility(View.GONE);

                        tvAvgCfFirstPlace.setVisibility(View.VISIBLE);
                        tvAvgCfSecondPlace.setVisibility(View.VISIBLE);
                        tvAvgCfThirdPlace.setVisibility(View.VISIBLE);

                        sortListByAvgCf = true;

                        loadScores(sortListByAvgCf);
                        break;

                    case R.id.most_ec:
                        tempList.clear();

                        bnveSort.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));
                        bnveSort.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));

                        llFirst.setVisibility(View.VISIBLE);
                        llSecond.setVisibility(View.VISIBLE);
                        llThird.setVisibility(View.VISIBLE);

                        tvAvgCfFirstPlace.setVisibility(View.GONE);
                        tvAvgCfSecondPlace.setVisibility(View.GONE);
                        tvAvgCfThirdPlace.setVisibility(View.GONE);

                        sortListByAvgCf = false;

                        loadScores(sortListByAvgCf);
                        break;
                }
                return true;
            }
        });
    }

    private void loadScores(final Boolean sortListByAvgCf) {
        progressDialog.show();
        /*
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
                                    /*double avg_cf = scoreObject.getDouble("avg_cf");
                                    //int image = scoreObject.getInt("image");

                                    Score score = new Score(id, email, num_coins, avg_cf, R.drawable.earth1);
                                    scoreList.add(score);
                                }
                            }
                            */
                            if (sortListByAvgCf){
                                progressDialog.dismiss();
                                // sort list by lowest to highest average carbon footprint
                                Collections.sort(scoreList, new Comparator<Score>() {
                                    @Override
                                    public int compare(Score score, Score t1) {
                                        return Double.compare(score.getAvg_cf(), t1.getAvg_cf());
                                    }
                                });
                            } else {
                                progressDialog.dismiss();

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
                            updatePodium(scoreList, sortListByAvgCf);
                            //creating recyclerview adapter
                            adapter = new ScoreAdapter(LeaderboardActivity.this, tempList);

                            //setting adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                            /*
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
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);*/
    }

    private void updatePodium(List<Score> scoreList, Boolean sortListByAvgCf) {

        int counter = 0;
        for (Score score : scoreList){
            if (sortListByAvgCf){
                if (counter == 0){
                    tvNameFirstPlace.setText(score.getName());
                    tvAvgCfFirstPlace.setText(String.valueOf(score.getAvg_cf()) + " " + getResources().getString(R.string.carbon_footprint_unit));
                    imgFirstPlace.setImageDrawable(getApplicationContext().getResources().getDrawable(score.getImage()));
                }
                if (counter == 1){
                    tvNameSecondPlace.setText(score.getName());
                    tvAvgCfSecondPlace.setText(String.valueOf(score.getAvg_cf()) + " " + getResources().getString(R.string.carbon_footprint_unit));
                    imgSecondPlace.setImageDrawable(getApplicationContext().getResources().getDrawable(score.getImage()));
                }
                if (counter == 2){
                    tvNameThirdPlace.setText(score.getName());
                    tvAvgCfThirdPlace.setText(String.valueOf(score.getAvg_cf()) + " " + getResources().getString(R.string.carbon_footprint_unit));
                    imgThirdPlace.setImageDrawable(getApplicationContext().getResources().getDrawable(score.getImage()));
                }
                if (counter > 2){
                    tempList.add(score);
                }
                counter++;
            } else {
                if (counter == 0){
                    tvNameFirstPlace.setText(score.getName());
                    tvNumCoinsFirstPlace.setText(String.valueOf(score.getNum_coins()));
                    imgFirstPlace.setImageDrawable(getApplicationContext().getResources().getDrawable(score.getImage()));
                }
                if (counter == 1){
                    tvNameSecondPlace.setText(score.getName());
                    tvNumCoinsSecondPlace.setText(String.valueOf(score.getNum_coins()));
                    imgSecondPlace.setImageDrawable(getApplicationContext().getResources().getDrawable(score.getImage()));
                }
                if (counter == 2){
                    tvNameThirdPlace.setText(score.getName());
                    tvNumCoinsThirdPlace.setText(String.valueOf(score.getNum_coins()));
                    imgThirdPlace.setImageDrawable(getApplicationContext().getResources().getDrawable(score.getImage()));
                }
                if (counter > 2){
                    tempList.add(score);
                }
                counter++;
            }
        }
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
        } else if (id == R.id.nav_about_app) {
            startActivity(new Intent(LeaderboardActivity.this, AboutUsActivity.class));
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
