package com.example.desent.desent.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.desent.desent.R;
import com.example.desent.desent.SessionManagement;
import com.example.desent.desent.models.Indicator;
import com.example.desent.desent.utils.Utility;

import java.io.FileNotFoundException;

public class ProfileActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{

    SharedPreferences sharedPreferences;
    TextView name, avgCf, tv_coin_score, tvProgress;
    //, email, address, zipcode, city, birthdate, gender;
    ImageView changeInfo, profilePic;
    LinearLayout ll_coin_score;
    DrawerLayout drawer;
    ProgressBar personalGoal;

    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //getActionBar().setTitle("Profile");
        //getSupportActionBar().setTitle("Profile");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setUpNavigationView();

        changeInfo = findViewById(R.id.changeUserInfo);
        profilePic = findViewById(R.id.imgProfilePicture);
        //profilePic.setImageResource(sharedPreferences.getString("pref_key_profile_picture",""));

        name = findViewById(R.id.tvName);
        name.setText(sharedPreferences.getString("pref_key_personal_name",""));

        avgCf = findViewById(R.id.avgCF);

        //tv_coin_score = findViewById(R.id.tv_coin_score);
        //tv_coin_score.setText("You have " + getCoinScore());

        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Today you have received Earth Coins (EC) from:")
                .setMessage("Walking:" + "\n" +
                "3 km: 1 EC" + "\n" +
                "8 km: 2 EC" + "\n" + "\n" +
                "Cycling:" + "\n" +
                "3 km: 1 EC" + "\n" +
                "8 km: 2 EC" + "\n" + "\n" +
                "Active minutes:" + "\n" +
                "Over 30 min: 1 EC" + "\n" + "\n" +
                "Total EC earned today: 7 EC")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
        builder.create();

        Spinner spinner = (Spinner) findViewById(R.id.spinnerPersonalGoal);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource
                (this, R.array.personal_goal_array, R.layout.spinner_layout);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_layout);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //ll_coin_score = (LinearLayout) findViewById(R.id.ll_coin_score);
        /*ll_coin_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.show();
            }
        });*/

        tvProgress = findViewById(R.id.tvProgress);

        personalGoal = (ProgressBar) findViewById(R.id.personalGoalPB);

        progressStatus = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100){
                    progressStatus += 1;
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            personalGoal.setProgress(progressStatus);
                            tvProgress.setText(progressStatus+"");
                        }
                    });
                }
            }
        }).start();

        /*email = findViewById(R.id.tvEmail);
        email.setText(sharedPreferences.getString("pref_key_personal_email",""));

        address = findViewById(R.id.tvAddress);
        address.setText(sharedPreferences.getString("pref_key_personal_address", ""));

        zipcode = findViewById(R.id.tvZipcode);
        zipcode.setText(sharedPreferences.getString("pref_key_personal_zip_code",""));

        city = findViewById(R.id.tvCity);
        city.setText(sharedPreferences.getString("pref_key_personal_city",""));

        birthdate = findViewById(R.id.tvBirthdate);
        birthdate.setText((sharedPreferences.getString("pref_key_personal_birthdate", "")));

        gender = findViewById(R.id.tvGender);
        gender.setText(sharedPreferences.getString("pref_key_personal_gender",""));*/
        /*DecoView arcView = (DecoView) findViewById(R.id.dynamicArcView);

        //create background track
        arcView.addSeries(new SeriesItem.Builder(Color.argb(255,218,218,218))
        .setRange(0,100,100)
        .setInitialVisibility(false)
        .setLineWidth(32f)
        .build());

        //create data series track
        SeriesItem seriesItem1 = new SeriesItem.Builder(Color.argb(255,64,196,0))
                .setRange(0,100,0)
                .setLineWidth(32f)
                .build();

        int series1Index = arcView.addSeries(seriesItem1);

        arcView.configureAngles(300,0);

        //arcView.addSeries(new SeriesItem.Builder(Color.argb(255,218,218,218)).build());

        SeriesItem seriesItem2 = new SeriesItem.Builder(Color.argb(255,64,196,0))
                .setRange(0,100,0)
                .setInitialVisibility(false)
                .setLineWidth(32f)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_OUTER, Color.parseColor("#22000000"),0.4f))
                .setSeriesLabel(new SeriesLabel.Builder("Percent %.0f%%").build())
                .setInterpolator(new OvershootInterpolator())
                .setShowPointWhenEmpty(false)
                .setCapRounded(false)
                .setInset(new PointF(32f,32f))
                .setDrawAsPoint(false)
                .setSpinClockwise(true)
                .setSpinDuration(6000)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
                .build();
        int series2Index = arcView.addSeries(seriesItem2);

        arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(1000)
                .setDuration(2000)
                .build());

        arcView.addEvent(new DecoEvent.Builder(25).setIndex(series1Index).setDelay(4000).build());
        arcView.addEvent(new DecoEvent.Builder(100).setIndex(series2Index).setDelay(8000).build());
        arcView.addEvent(new DecoEvent.Builder(10).setIndex(series2Index).setDelay(12000).build());

        arcView.setHorizGravity(DecoView.HorizGravity.GRAVITY_HORIZONTAL_LEFT);
        arcView.setVertGravity(DecoView.VertGravity.GRAVITY_VERTICAL_BOTTOM);*/

    }

    protected void setUpNavigationView(){
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
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(ProfileActivity.this, HistoryActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_user_profile) {
            //startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_friend_list) {
            startActivity(new Intent(ProfileActivity.this, FriendsActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_leaderboard) {
            startActivity(new Intent(ProfileActivity.this, LeaderboardActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_info_app) {
            startActivity(new Intent(ProfileActivity.this, InformationActivity.class));
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

    public int getCoinScore() {
        int dailyCoinScore = 0;
        /*Indicator indicator = new Indicator() {
            @Override
            public void calculateValues() {

            }
        };*/
        //float dailyWalkingDistance = indicator.getWalkingDistance();
        float dailyWalkingDistance = 12;
        if (dailyWalkingDistance >= 3){
            dailyCoinScore += 1;
        }
        if (dailyWalkingDistance >= 8){
            dailyCoinScore += 2;
        }

        float dailyCycleDistance = 12;
        if (dailyCycleDistance >= 3){
            dailyCoinScore += 1;
        }
        if (dailyCycleDistance >= 8){
            dailyCoinScore += 2;
        }

        float activeMinutes = 33;
        if (activeMinutes >= 30){
            dailyCoinScore += 1;
        }
        return dailyCoinScore;
    }
}
