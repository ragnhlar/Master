package com.example.desent.desent.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.desent.desent.R;
import com.example.desent.desent.SessionManagement;
import com.example.desent.desent.models.DatabaseHelper;
import com.example.desent.desent.models.Energy;
import com.example.desent.desent.models.Transportation;
import com.example.desent.desent.utils.ChartData;
import com.example.desent.desent.utils.GraphPoints;
import com.example.desent.desent.utils.Utility;
import com.example.desent.desent.views.StackBarChart;
import com.example.desent.desent.views.StackedBarLabel;
import com.example.desent.desent.views.Yaxis;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.jjoe64.graphview.series.DataPoint;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by celine on 20/07/17.
 */

public class HistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    //Spinner spinner;
    final static String LOGG = "HistoryPage";
    DatabaseHelper myDb;
    Energy energy;
    Transportation transportation;
    StackBarChart stackBarChart;
    StackedBarLabel labelOrganizer;
    Yaxis yaxis;
    BottomNavigationViewEx bnveHistory;
    TextView histAnnotation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        //getActionBar().setTitle("History");
        //getSupportActionBar().setTitle("History");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setUpNavigationView();

        myDb = new DatabaseHelper(this);
        myDb.getWeekDrivingDistance();
        histAnnotation = (TextView) findViewById(R.id.hist_annotation);
        histAnnotation.setText(getResources().getString(R.string.carbon_footprint_unit));
        stackBarChart = (StackBarChart) findViewById(R.id.chart);
        labelOrganizer = (StackedBarLabel) findViewById(R.id.labelStackedBar);
        yaxis = (Yaxis) findViewById(R.id.y_axis);
        //displayDistanceGraph();
        //displayCarbonFootprintGraph();

        bnveHistory = (BottomNavigationViewEx) findViewById(R.id.navHistorySort);
        bnveHistory.setSelectedItemId(R.id.hist_cf);
        bnveHistory.enableAnimation(true);
        bnveHistory.enableShiftingMode(false);
        bnveHistory.enableItemShiftingMode(false);
        bnveHistory.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.selector_time_navigation_white_grey));
        bnveHistory.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));
        //bnveSort.setIconSize(25,25);
        bnveHistory.setTextSize(12);
        //bnveSort.setIconsMarginTop(20);

        bnveHistory.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.hist_cf:
                        displayCarbonFootprintGraph();
                        break;

                    case R.id.hist_transportation:
                        displayDistanceGraph();
                        break;

                    case R.id.hist_energy:
                        displayEnergyConsumptionGraph();
                        break;

                }
                stackBarChart.invalidate();
                labelOrganizer.invalidate();
                yaxis.invalidate();
                return true;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //spinner.setSelection(0);

        AsyncHistorySetup asyncHistorySetup = new AsyncHistorySetup(this,
                stackBarChart,
                labelOrganizer,
                yaxis);

        asyncHistorySetup.execute();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(HistoryActivity.this, MainActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_history) {
            drawer.closeDrawers();
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(HistoryActivity.this, SettingsActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_user_profile) {
            startActivity(new Intent(HistoryActivity.this, ProfileActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_friend_list) {
            startActivity(new Intent(HistoryActivity.this, FriendsActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_leaderboard) {
            startActivity(new Intent(HistoryActivity.this, LeaderboardActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_info_app) {
            startActivity(new Intent(HistoryActivity.this, InformationActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_about_app) {
            startActivity(new Intent(HistoryActivity.this, AboutUsActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        }  else if (id == R.id.nav_log_out){
            SessionManagement session = new SessionManagement(getApplicationContext());
            session.logoutUser();
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
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
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ArrayList<String> getWeekLabels(){
        ArrayList<String> weekLabels = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        String[] ref = getResources().getStringArray(R.array.week_days);

        for (int i=7; i>0; i--){
            weekLabels.add(ref[(today-i)>=0 ? today-i : today-i+7]);
        }
        return weekLabels;
    }

    public void displayCarbonFootprintGraph() {

        histAnnotation.setText(getResources().getString(R.string.carbon_footprint_unit));

        List<ChartData> value = new ArrayList<>();

        //remove comment out to get app to work properly
        float[] carbonFootprintTransportation = transportation.getWeekCarbonFootprint();
        float[] carbonFootprintEnergy = energy.generateArrayWeekCarbonFootprint();

        Float[] value1 = new Float[7];
        Float[] value2 = new Float[7];

        //remove to get app to work properly
        /*
        value2[0] = (float) 1.4;
        value1[0] = (float) 0.8;

        value2[1] = (float) 1.4;
        value1[1] = (float) 0.7;

        value2[2] = (float) 4.5;
        value1[2] = (float) 0.8;

        value2[3] = (float) 4.5;
        value1[3] = (float) 0.7;

        value2[4] = (float) 3.1;
        value1[4] = (float) 1.2;

        value2[5] = (float) 3.1;
        value1[5] = (float) 1.2;

        value2[6] = (float) 3.5;
        value1[6] = (float) 0.9;
        */

        //remove comment to get app to work properly

        for (int i=0; i<7; i++) {
            //to visualize when no values in db
            /*value1[i] = (float) i + (float) 1;
            value2[i] = (float) i + (float) 1;*/

            value1[i] = carbonFootprintEnergy[i];
            value2[i] = carbonFootprintTransportation[i];
        }

        String limitColor = "#FF0000";

        String barColor1 = "#03a9f4";
        String barColor2 = "#64dd17";

        String labelText1 = "From energy";
        String labelText2 = "From transportation"; //TODO: string

        value.add(new ChartData(value1, labelText1, barColor1));
        value.add(new ChartData(value2, labelText2, barColor2));

        List<String> h_labels = getWeekLabels();

        stackBarChart.setHorizontal_label(h_labels);
        stackBarChart.setBarIndent(50);
        stackBarChart.setDecimalsNumber(1);
        Log.i(LOGG, "før setData");

        stackBarChart.setData(value);

        labelOrganizer.clear();

        labelOrganizer = (StackedBarLabel) findViewById(R.id.labelStackedBar);
        // Set color on labels
        labelOrganizer.addColorLabels(barColor1);
        labelOrganizer.addColorLabels(barColor2);

        // Set label text
        labelOrganizer.addLabelText(labelText1);
        labelOrganizer.addLabelText(labelText2);

        yaxis = (Yaxis) findViewById(R.id.y_axis);
        yaxis.setBorder(60);
        yaxis.setFirstValueSet(value);
    }

    public void displayEnergyConsumptionGraph() {

        histAnnotation.setText("kWh");

        List<ChartData> value = new ArrayList<>();

        //remove comment to work properly
        float[] weekEnergyConsumption = energy.generateArrayWeekEnergyConsumption();
        Float[] value1 = new Float[7];

        //remove to get app to walk properly
        /*
        value1[0] = (float) 4.9;
        value1[1] = (float) 6.0;
        value1[2] = (float) 5.2;
        value1[3] = (float) 8.8;
        value1[4] = (float) 8.2;
        value1[5] = (float) 6.6;
        value1[6] = (float) 5.5;
        */

        //remove comment to work properly
        for (int i=0; i<7; i++)

            value1[i] = weekEnergyConsumption[i];
            //added to visualize graph with no values in db
            //value1[i] = (float) i + (float) + 1;

        String barColor1 = "#03a9f4";

        String labelText1 = "Energy consumption";

        value.add(new ChartData(value1, labelText1, barColor1));

        List<String> h_labels = getWeekLabels();

        stackBarChart.setHorizontal_label(h_labels);
        stackBarChart.setBarIndent(50);
        Log.i(LOGG, "før setData");

        stackBarChart.setData(value);
        stackBarChart.setDecimalsNumber(1);

        labelOrganizer.clear();

        labelOrganizer = (StackedBarLabel) findViewById(R.id.labelStackedBar);
        // Set color on labels
        labelOrganizer.addColorLabels(barColor1);

        // Set label text
        labelOrganizer.addLabelText(labelText1);

        yaxis = (Yaxis) findViewById(R.id.y_axis);
        yaxis.setBorder(60);
        yaxis.setFirstValueSet(value);
    }

    public void displayDistanceGraph() {

        histAnnotation.setText("km");

        List<ChartData> value = new ArrayList<>();

        //remove comment to work properly
        float[] walkingDistance = myDb.getWeekWalkingDistance();
        float[] cyclingDistance = myDb.getWeekCyclingDistance();
        float[] drivingDistance = myDb.getWeekDrivingDistance();

        Float[] value1 = new Float[7];
        Float[] value2 = new Float[7];
        Float[] value3 = new Float[7];

        /*
        value1[0] = (float) 3;
        value2[0] = (float) 4;
        value3[0] = (float) 28.3;

        value1[1] = (float) 4;
        value2[1] = (float) 0;
        value3[1] = (float) 34.5;

        value1[2] = (float) 2;
        value2[2] = (float) 5;
        value3[2] = (float) 34.1;

        value1[3] = (float) 2;
        value2[3] = (float) 0;
        value3[3] = (float) 23.1;

        value1[4] = (float) 3;
        value2[4] = (float) 6;
        value3[4] = (float) 23.6;

        value1[5] = (float) 8;
        value2[5] = (float) 2;
        value3[5] = (float) 26.8;

        value1[6] = (float) 2;
        value2[6] = (float) 0;
        value3[6] = (float) 30.4;
        */

        //remove comment to work properly
        for (int i=0; i<7; i++) {
            /* to see how graph look likes
            value1[i] = (float) i + (float) 1.0;
            value2[i] = (float) i + (float) 1.0;
            value3[i] = (float) i + (float) 1.0;
            */
            //retrieves actual historical data
            value1[i] = walkingDistance[i];
            value2[i] = cyclingDistance[i];
            value3[i] = drivingDistance[i];
        }

        String barColor1 = "#00ff00";
        String barColor2 = "#00cc00";
        String barColor3 = "#009900";

        String labelText1 = "Distance walked";
        String labelText2 = "Distance cycled";
        String labelText3 = "Distance driven";

        value.add(new ChartData(value1, labelText1, barColor1));
        value.add(new ChartData(value2, labelText2, barColor2));
        value.add(new ChartData(value3, labelText3, barColor3));

        List<String> h_labels = getWeekLabels();

        stackBarChart.setHorizontal_label(h_labels);
        stackBarChart.setBarIndent(50);
        Log.i(LOGG, "før setData");

        stackBarChart.setData(value);
        stackBarChart.setDecimalsNumber(1);

        // stackBarChart.setDescription("Travel distance");

        labelOrganizer.clear();

        // Set color on labels
        labelOrganizer.addColorLabels(barColor1);
        labelOrganizer.addColorLabels(barColor2);
        labelOrganizer.addColorLabels(barColor3);

        // Set label text
        labelOrganizer.addLabelText(labelText1);
        labelOrganizer.addLabelText(labelText2);
        labelOrganizer.addLabelText(labelText3);

        yaxis.setBorder(60);
        yaxis.setFirstValueSet(value);
    }

    public Energy getEnergy() {
        return energy;
    }

    public void setEnergy(Energy energy) {
        this.energy = energy;
    }

    public Transportation getTransportation() {
        return transportation;
    }

    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }
}
