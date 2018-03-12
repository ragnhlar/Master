package com.example.desent.desent.activities;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.desent.desent.R;
import com.example.desent.desent.SessionManagement;
import com.example.desent.desent.fragments.CategoryFragment;
import com.example.desent.desent.fragments.CircleFragment;
import com.example.desent.desent.fragments.CyclingDistanceFragment;
import com.example.desent.desent.fragments.ElectricCarFragment;
import com.example.desent.desent.fragments.IndicatorsBarFragment;
import com.example.desent.desent.fragments.SolarPanelSizeFragment;
import com.example.desent.desent.fragments.WalkingDistanceFragment;
import com.example.desent.desent.models.ActiveMinutes;
import com.example.desent.desent.models.Calories;
import com.example.desent.desent.models.CarbonFootprint;
import com.example.desent.desent.models.DatabaseHelper;
import com.example.desent.desent.models.DistanceTracker;
import com.example.desent.desent.models.DrivingDistance;
import com.example.desent.desent.models.Energy;
import com.example.desent.desent.models.EnergyConsumption;
import com.example.desent.desent.models.EnergyDatabaseUpdate;
import com.example.desent.desent.models.Expenses;
import com.example.desent.desent.models.Indicator;
import com.example.desent.desent.models.Transportation;
import com.example.desent.desent.models.VehicleCost;
import com.example.desent.desent.utils.EstimationType;
import com.example.desent.desent.utils.TimeScale;
import com.example.desent.desent.utils.Utility;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.os.Handler;
import android.widget.Toast;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView earthCoin;
    TextView numCoin;

    //Spinner timeSpinner;
    TextView textViewTimeScale;

    BottomNavigationViewEx bnve;
    BottomNavigationViewEx bnveTime;

    TableLayout tableInfo;
    //TextView tvWalkInfo;

    //Fragments
    CircleFragment carbonFootprintCircleFragment;
    private CategoryFragment transportationFragment;
    private CategoryFragment energyFragment;
    private IndicatorsBarFragment indicatorsBarFragment;

    private View categoriesBar;

    //Estimations parameters
    private WalkingDistanceFragment walkingDistanceFragment;
    private CyclingDistanceFragment cyclingDistanceFragment;
    private SolarPanelSizeFragment solarPanelSizeFragment;
    private ElectricCarFragment electricCarFragment;

    //Indicators
    protected ArrayList<Indicator> indicators = new ArrayList<>();
    protected Calories calories;
    protected ActiveMinutes activeMinutes;
    protected Expenses expenses;
    protected CarbonFootprint carbonFootprint;
    protected DrivingDistance drivingDistance;
    protected EnergyConsumption energyConsumption;
    protected Energy energy;

    //Information views
    protected View informationCO2Left;
    protected View informationSavings;
    protected View informationDaysLeftSolarPanel;
    protected View informationOwnEnergy;
    protected View informationSeparator; //TODO: not treat it in the activity?

    //Drawer
    private DrawerLayout drawer;
    private  Toolbar toolbar;
    private NavigationView navigationView;

    //DistanceTracker
    private String activity = "STILL";
    private DistanceTracker distanceTracking;
    private boolean gpsFlag;
    private static final String FENCE_RECEIVER_ACTION = "FENCE_RECEIVE";

    //Energy
    Handler EnergyHandler = new Handler();

    SessionManagement session;

    public EnergyDatabaseUpdate energyDatabaseUpdate;
    public DatabaseHelper mDatabaseHelper;

    private boolean isFirstDisplay = true;
    private boolean receiversRegistered;

    public boolean isFirstDisplay() {
        return isFirstDisplay;
    }

    public void setFirstDisplay(boolean firstDisplay) {
        isFirstDisplay = firstDisplay;
    }

    public ArrayList<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(ArrayList<Indicator> indicators) {
        this.indicators = indicators;
    }

    public Calories getCalories() {
        return calories;
    }

    public void setCalories(Calories calories) {
        this.calories = calories;
    }

    public void setActiveMinutes(ActiveMinutes activeMinutes){
        this.activeMinutes = activeMinutes;
    }

    public Expenses getExpenses() {
        return expenses;
    }

    public void setExpenses(Expenses expenses) {
        this.expenses = expenses;
    }

    public CarbonFootprint getCarbonFootprint() {
        return carbonFootprint;
    }

    public void setCarbonFootprint(CarbonFootprint carbonFootprint) {
        this.carbonFootprint = carbonFootprint;
    }

    public DrivingDistance getDrivingDistance() {
        return drivingDistance;
    }

    public void setDrivingDistance(DrivingDistance drivingDistance) {
        this.drivingDistance = drivingDistance;
    }

    public EnergyConsumption getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(EnergyConsumption energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        //timeSpinner.setSelection(0);
        bnveTime.setSelectedItemId(R.id.time_today);

        // Implement when back button is pressed show "dashboard"

        //not a good way to solve this...
        startActivity(new Intent(MainActivity.this, MainActivity.class));

        /*
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        for (Indicator indicator:indicators)
            indicator.setEstimationType(EstimationType.NONE);

        if (timeSpinner.getSelectedItemPosition() == 0)
            informationCO2Left.setVisibility(View.VISIBLE);

        informationSavings.setVisibility(View.GONE);
        informationDaysLeftSolarPanel.setVisibility(View.GONE);
        informationSeparator.setVisibility(GONE);
        informationOwnEnergy.setVisibility(GONE);

        ft.hide(walkingDistanceFragment);
        ft.hide(cyclingDistanceFragment);
        ft.hide(solarPanelSizeFragment);
        ft.commit();
        */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new SessionManagement(getApplicationContext());
        Toast.makeText(getApplicationContext(), "User login status: " + session.isLoggedIn(), Toast.LENGTH_LONG).show();
        session.checkLogin();

        // Drawer "hamburger"
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //Navigation drawer
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        earthCoin = findViewById(R.id.earth_coin);
        numCoin = findViewById(R.id.coin_score);

        //Spinners
        //timeSpinner = (Spinner) findViewById(R.id.time_spinner);
        //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.time_spinner_array, android.R.layout.simple_spinner_item);
        //adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        //timeSpinner.setAdapter(adapter);


        textViewTimeScale = (TextView) findViewById(R.id.textViewTimeScale);

        tableInfo = (TableLayout) findViewById(R.id.tableInfo);
        //tvWalkInfo = (TextView) findViewById(R.id.tvWalkInfo);

        //Bottom navigation

        //final BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        //Time navigation
        bnveTime = (BottomNavigationViewEx) findViewById(R.id.navTime);
        bnveTime.setSelectedItemId(R.id.time_today);
        bnveTime.enableAnimation(true);
        bnveTime.enableShiftingMode(false);
        bnveTime.enableItemShiftingMode(false);
        bnveTime.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.selector_time_navigation_white_grey));
        bnveTime.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));
        bnveTime.setIconSize(25,25);
        bnveTime.setTextSize(14);
        bnveTime.setIconsMarginTop(20);

        bnveTime.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.time_today:
                        bnveTime.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(), R.color.selector_time_navigation_white_grey));
                        bnveTime.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));

                        if (carbonFootprint.getEstimationType() == EstimationType.NONE)
                            informationCO2Left.setVisibility(VISIBLE);
                        textViewTimeScale.setVisibility(View.GONE);
                        for (Indicator indicator: indicators)
                            indicator.setTimeScale(TimeScale.TODAY);
                        break;

                    case R.id.time_week:
                        bnveTime.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));
                        bnveTime.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));

                        informationCO2Left.setVisibility(GONE);
                        textViewTimeScale.setVisibility(View.VISIBLE);
                        textViewTimeScale.setText("Average this week");
                        for (Indicator indicator: indicators)
                            indicator.setTimeScale(TimeScale.WEEK);
                        break;

                    case R.id.time_month:
                        bnveTime.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));
                        bnveTime.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_time_navigation_white_grey));

                        informationCO2Left.setVisibility(GONE);
                        textViewTimeScale.setVisibility(View.VISIBLE);
                        textViewTimeScale.setText("Average this month");
                        for (Indicator indicator: indicators)
                            indicator.setTimeScale(TimeScale.MONTH);
                }
                refreshAll();

                return true;
            }
        });

        /*
        AdapterView.OnItemSelectedListener timeSpinnerHandler = new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            switch (pos) {
                case 0:
                    if (carbonFootprint.getEstimationType() == EstimationType.NONE)
                        informationCO2Left.setVisibility(VISIBLE);
                        textViewTimeScale.setVisibility(View.GONE);
                    for (Indicator indicator: indicators)
                        indicator.setTimeScale(TimeScale.TODAY);
                    break;
                case 1:
                    informationCO2Left.setVisibility(GONE);
                    textViewTimeScale.setVisibility(View.VISIBLE);
                    textViewTimeScale.setText("Average this week");
                    for (Indicator indicator: indicators)
                        indicator.setTimeScale(TimeScale.WEEK);
                    break;
                case 2:
                    informationCO2Left.setVisibility(GONE);
                    textViewTimeScale.setVisibility(View.VISIBLE);
                    textViewTimeScale.setText("Average this month");
                    for (Indicator indicator: indicators)
                        indicator.setTimeScale(TimeScale.MONTH);
            }
            refreshAll();
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
         */

        bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setIconSize(30,30);
        bnve.setTextSize(14);
        bnve.setIconsMarginTop(5);

        /*
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
            itemView.setShiftingMode(false);
            itemView.setChecked(false);
        }*/

        findViewById(R.id.navigation_none).setVisibility(GONE);

        bnve.setOnNavigationItemSelectedListener(
                new BottomNavigationViewEx.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        boolean isCarOwner = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean("pref_key_car_owner", true);

                        switch (item.getItemId()) {

                            case R.id.navigation_none:

                                bnveTime.setVisibility(View.VISIBLE);

                                tableInfo.setVisibility(View.GONE);
                                //tvWalkInfo.setVisibility(View.GONE);

                                for (Indicator indicator:indicators)
                                    indicator.setEstimationType(EstimationType.NONE);

                                //if (timeSpinner.getSelectedItemPosition() == 0)
                                if (bnveTime.getCurrentItem() == R.id.time_today) {
                                    informationCO2Left.setVisibility(View.VISIBLE);
                                }

                                informationSavings.setVisibility(View.GONE);
                                informationDaysLeftSolarPanel.setVisibility(View.GONE);
                                informationSeparator.setVisibility(GONE);
                                informationOwnEnergy.setVisibility(GONE);

                                ft.hide(walkingDistanceFragment);
                                ft.hide(cyclingDistanceFragment);
                                ft.hide(solarPanelSizeFragment);
                                ft.hide(electricCarFragment);
                                ft.commit();

                                break;

                            case R.id.navigation_solar_installation:

                                bnve.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_bottom_navigation_blue));
                                bnve.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_bottom_navigation_blue));

                                bnveTime.setVisibility(View.GONE);

                                tableInfo.setVisibility(View.VISIBLE);
                                //tvWalkInfo.setVisibility(View.GONE);

                                for (Indicator indicator:indicators)
                                    indicator.setEstimationType(EstimationType.SOLAR_INSTALLATION);

                                informationCO2Left.setVisibility(View.GONE);
                                informationSavings.setVisibility(View.VISIBLE);
                                informationDaysLeftSolarPanel.setVisibility(View.VISIBLE);
                                informationSeparator.setVisibility(VISIBLE);
                                informationOwnEnergy.setVisibility(VISIBLE);

                                ft.hide(walkingDistanceFragment);
                                ft.hide(cyclingDistanceFragment);
                                ft.show(solarPanelSizeFragment);
                                ft.hide(electricCarFragment);
                                ft.commit();

                                break;
                            case R.id.navigation_walking:

                                bnve.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_bottom_navigation_green));
                                bnve.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_bottom_navigation_green));

                                bnveTime.setVisibility(View.GONE);

                                tableInfo.setVisibility(View.VISIBLE);
                                //tvWalkInfo.setVisibility(View.VISIBLE);

                                for (Indicator indicator:indicators)
                                    indicator.setEstimationType(EstimationType.WALKING);

                                informationCO2Left.setVisibility(View.GONE);
                                informationOwnEnergy.setVisibility(GONE);

                                if (isCarOwner) {
                                    informationSavings.setVisibility(View.VISIBLE);
                                    informationDaysLeftSolarPanel.setVisibility(View.VISIBLE);
                                    informationSeparator.setVisibility(VISIBLE);
                                } else {
                                    informationSavings.setVisibility(View.GONE);
                                    informationDaysLeftSolarPanel.setVisibility(View.GONE);
                                    informationSeparator.setVisibility(GONE);
                                }

                                ft.show(walkingDistanceFragment);
                                ft.hide(cyclingDistanceFragment);
                                ft.hide(solarPanelSizeFragment);
                                ft.hide(electricCarFragment);
                                ft.commit();

                                walkingDistanceFragment.refresh();

                                break;
                            case R.id.navigation_cycling:

                                bnve.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_bottom_navigation_green));
                                bnve.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_bottom_navigation_green));

                                bnveTime.setVisibility(View.GONE);

                                tableInfo.setVisibility(View.VISIBLE);
                                //tvWalkInfo.setVisibility(View.GONE);

                                for (Indicator indicator:indicators)
                                    indicator.setEstimationType(EstimationType.CYCLING);

                                informationCO2Left.setVisibility(View.GONE);
                                informationOwnEnergy.setVisibility(GONE);

                                if (isCarOwner) {
                                    informationSavings.setVisibility(View.VISIBLE);
                                    informationDaysLeftSolarPanel.setVisibility(View.VISIBLE);
                                    informationSeparator.setVisibility(VISIBLE);
                                } else {
                                    informationSavings.setVisibility(View.GONE);
                                    informationDaysLeftSolarPanel.setVisibility(View.GONE);
                                    informationSeparator.setVisibility(GONE);
                                }

                                ft.hide(walkingDistanceFragment);
                                ft.show(cyclingDistanceFragment);
                                ft.hide(solarPanelSizeFragment);
                                ft.hide(electricCarFragment);
                                ft.commit();

                                cyclingDistanceFragment.refresh();

                                break;
                            case R.id.navigation_electric_car:

                                bnve.setItemTextColor(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_bottom_navigation_green));
                                bnve.setItemIconTintList(ContextCompat.getColorStateList(getApplicationContext(),R.color.selector_bottom_navigation_green));

                                bnveTime.setVisibility(View.GONE);

                                tableInfo.setVisibility(View.VISIBLE);
                                //tvWalkInfo.setVisibility(View.GONE);

                                for (Indicator indicator:indicators)
                                    indicator.setEstimationType(EstimationType.ELECTRIC_CAR);

                                informationCO2Left.setVisibility(View.GONE);
                                informationSavings.setVisibility(View.VISIBLE);
                                informationDaysLeftSolarPanel.setVisibility(View.VISIBLE);
                                informationOwnEnergy.setVisibility(GONE);
                                informationSeparator.setVisibility(VISIBLE);

                                ft.show(electricCarFragment);
                                ft.hide(walkingDistanceFragment);
                                ft.hide(cyclingDistanceFragment);
                                ft.hide(solarPanelSizeFragment);
                                ft.commit();

                                break;
                        }
                        refreshAll();
                        return true;
                    }
                });

        bnve.setOnNavigationItemReselectedListener(
                new BottomNavigationView.OnNavigationItemReselectedListener() {
                    @Override
                    public void onNavigationItemReselected(@NonNull MenuItem item) {
                        findViewById(R.id.navigation_none).performClick();
                    }
                });
        init();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(walkingDistanceFragment);
        ft.hide(cyclingDistanceFragment);
        ft.hide(solarPanelSizeFragment);
        ft.hide(electricCarFragment);
        ft.commit();

        //DistanceTracker
        Activity activityContext = this;
        distanceTracking = new DistanceTracker(activityContext, this);
        distanceTracking.setActivity(activity);
        registerReceivers();
    }

    @Override
    protected void onStart() {
        super.onStart();
        distanceTracking.start();
        registerReceivers();
        //registerReceiver(distanceTracking.getFenceReceiver(), new IntentFilter(FENCE_RECEIVER_ACTION));
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        AsyncMainSetup asyncMainSetup = new AsyncMainSetup(this,
                progressBar,
                indicators,
                carbonFootprintCircleFragment,
                indicatorsBarFragment,
                transportationFragment,
                energyFragment,
                walkingDistanceFragment,
                cyclingDistanceFragment,
                solarPanelSizeFragment,
                electricCarFragment);

        asyncMainSetup.execute();

        // necessary for reading the Weather
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        energyDatabaseUpdate = new EnergyDatabaseUpdate(this); //TODO: async
        mDatabaseHelper = new DatabaseHelper(this);

        EnergyUpdatesRunnableCode.run();
    }

    private void registerReceivers() {
        //only register if not already registered
        if (!receiversRegistered){
           registerReceiver(distanceTracking.getFenceReceiver(), new IntentFilter(FENCE_RECEIVER_ACTION));
           receiversRegistered = true;
        }
    }

    @Override
    protected void onResume() {
        registerReceivers();
        super.onResume();
        if(gpsFlag == true) {
            distanceTracking.askForGps();
            gpsFlag = false;
        }
        //setUp();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //distanceTracking.stop();
        //unregisterReceiver(distanceTracking.getFenceReceiver());
        if (receiversRegistered) {
            unregisterReceiver(distanceTracking.getFenceReceiver());
            receiversRegistered = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case 2: { if ((grantResults.length > 0)
                    && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Log.i("MAIN","onRequestPermissionsResult");
                //distanceTracking.askForGps();
                gpsFlag = true;
            } else {
                //Toast.makeText(this, "The app needs to enable location to do the calculations.",
                  //      Toast.LENGTH_LONG).show();
            }
                return;
            }
        }
    }

    private void setUpNavigationView() {

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

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:

                        //not a good way to solve this...
                        startActivity(new Intent(MainActivity.this, MainActivity.class));

                        // launch new intent instead of loading fragment
                        /*
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                         */
                        drawer.closeDrawers();

                        return true;
                    case R.id.nav_history:

                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_settings:

                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_about_us:

                        // launch new intent instead of loading fragment
                        /*
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                         */
                    case R.id.nav_log_out:

                        session.logoutUser();
                        //startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_history_graph:

                        startActivity(new Intent(MainActivity.this, HistoryGraphActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_info_app:

                        startActivity(new Intent(MainActivity.this, InformationActivity.class));
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_user_profile:

                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                }
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    /*AdapterView.OnItemSelectedListener timeSpinnerHandler = new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

            switch (pos) {
                case 0:
                    if (carbonFootprint.getEstimationType() == EstimationType.NONE)
                        informationCO2Left.setVisibility(VISIBLE);
                        textViewTimeScale.setVisibility(View.GONE);
                    for (Indicator indicator: indicators)
                        indicator.setTimeScale(TimeScale.TODAY);
                    break;
                case 1:
                    informationCO2Left.setVisibility(GONE);
                    textViewTimeScale.setVisibility(View.VISIBLE);
                    textViewTimeScale.setText("Average this week");
                    for (Indicator indicator: indicators)
                        indicator.setTimeScale(TimeScale.WEEK);
                    break;
                case 2:
                    informationCO2Left.setVisibility(GONE);
                    textViewTimeScale.setVisibility(View.VISIBLE);
                    textViewTimeScale.setText("Average this month");
                    for (Indicator indicator: indicators)
                        indicator.setTimeScale(TimeScale.MONTH);
            }
            refreshAll();
        }

        public void onNothingSelected(AdapterView<?> parent) {
        }
    };*/

    public void updateDaysLeftSolarPanel() {
        int days = expenses.calculateDaysLeftForSolarRoof();
        ((TextView) findViewById(R.id.text_view_information_days_left_solar_panel)).setText(String.format(getResources().getString(R.string.information_days_left_solar_panel), (days == -1) ? getString(R.string.infinity) : String.valueOf(days)));
    }

    public void updateCO2left() {
        TextView co2Left = (TextView) findViewById(R.id.text_view_information_co2_left);
        if (carbonFootprint.getDailyValue() < carbonFootprint.getLimitValue()) {
            co2Left.setVisibility(VISIBLE);
            co2Left.setText(String.format(getResources().getString(R.string.information_co2_left2), Utility.doubleToStringNDecimals(carbonFootprint.getLimitValue() - carbonFootprint.getDailyValue(), carbonFootprint.getDecimalsNumber())));
        } else
            co2Left.setVisibility(GONE);
    }

    private void updateSavings() {
        ((TextView) findViewById(R.id.text_view_information_daily_savings)).setText(String.format(getResources().getString(R.string.information_savings), Utility.floatToStringNDecimals(expenses.calculateSavings(), expenses.getDecimalsNumber()) + " " + expenses.getUnit()));
    }

    public void updateOwnEnergy(){
        ((TextView) findViewById(R.id.text_view_information_own_energy)).setText(String.format(getResources().getString(R.string.information_own_energy), String.valueOf(energyConsumption.calculatePercentageSelfConsumption()) + " %"));
    }

    public void refreshAll(){

        if (!isFirstDisplay) {

            for (Indicator indicator : indicators)
                indicator.calculateValues();

            carbonFootprintCircleFragment.refresh();
            energyFragment.refresh();
            transportationFragment.refresh();
            indicatorsBarFragment.refresh();
            updateSavings();
            updateOwnEnergy();
            updateCO2left();
            updateDaysLeftSolarPanel();
        }
    }

    /*public void initTimeSpinner(){
        timeSpinner.setOnItemSelectedListener(timeSpinnerHandler);
    }*/

    protected void init() {

        //Colors
        int mGreen = ContextCompat.getColor(this, R.color.green);
        int mBlue = ContextCompat.getColor(this, R.color.blue);

        //Information text views
        informationCO2Left = findViewById(R.id.information_co2_left);
        informationSavings = findViewById(R.id.information_daily_savings);
        informationDaysLeftSolarPanel = findViewById(R.id.information_days_left_solar_panel);
        informationOwnEnergy = findViewById(R.id.information_own_energy);
        informationSeparator = findViewById(R.id.separator_information);

        walkingDistanceFragment = (WalkingDistanceFragment) getFragmentManager().findFragmentById(R.id.walking_distance);
        cyclingDistanceFragment = (CyclingDistanceFragment) getFragmentManager().findFragmentById(R.id.cycling_distance);
        solarPanelSizeFragment = (SolarPanelSizeFragment) getFragmentManager().findFragmentById(R.id.solar_panel_size);
        electricCarFragment = (ElectricCarFragment) getFragmentManager().findFragmentById(R.id.electric_car);

        categoriesBar = findViewById(R.id.categories_bar);

        //Fragment
        carbonFootprintCircleFragment = (CircleFragment) getFragmentManager().findFragmentById(R.id.dailyCarbonFootprint);
        carbonFootprintCircleFragment.setStartAngle(135);
        carbonFootprintCircleFragment.setSweepAngle(270);
        carbonFootprintCircleFragment.init();

        transportationFragment = (CategoryFragment) getFragmentManager().findFragmentById(R.id.transportation_dashboard_fragment);
        transportationFragment.setColor(mGreen);
        transportationFragment.setCategoryName("Transport");
        transportationFragment.setImgName("transportation");
        transportationFragment.init();

        energyFragment = (CategoryFragment) getFragmentManager().findFragmentById(R.id.housing_dashboard_fragment);
        energyFragment.setColor(mBlue);
        energyFragment.setCategoryName("Energy");
        energyFragment.setImgName("ic_wb_incandescent_black_24dp");
        energyFragment.init();

        indicatorsBarFragment = (IndicatorsBarFragment) getFragmentManager().findFragmentById(R.id.indicators_bar);

        setUpNavigationView();
    }

    // Define the code block to be executed
    public Runnable EnergyUpdatesRunnableCode = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            Log.i("Handler", "Weather data update");

            try {
                energyDatabaseUpdate.UpdateCurrentValues();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                energyDatabaseUpdate.UpdateForecast();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Runnable code repeated twice per hour (1700.000 ms)
            EnergyHandler.postDelayed(EnergyUpdatesRunnableCode, 1700000);
        }
    };
}
