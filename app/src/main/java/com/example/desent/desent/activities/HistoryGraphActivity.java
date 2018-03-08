package com.example.desent.desent.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.desent.desent.R;
import com.example.desent.desent.models.DatabaseHelper;
import com.example.desent.desent.models.Energy;
import com.example.desent.desent.models.Transportation;
import com.example.desent.desent.utils.Utility;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.desent.desent.models.DatabaseHelper.D_COL_1;
import static com.example.desent.desent.models.DatabaseHelper.D_COL_2;
import static com.example.desent.desent.models.DatabaseHelper.TABLE_DISTANCE;

public class HistoryGraphActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    GraphView graph;
    LineGraphSeries<DataPoint> series;
    BarGraphSeries<DataPoint> barGraphSeries;
    PointsGraphSeries<DataPoint> pointsGraphSeries;
    DatabaseHelper myHelper;
    SQLiteDatabase sqLiteDatabase;
    SimpleDateFormat sdf = new SimpleDateFormat("E F.M."); //Day Day.Month.

    //DrawerLayout drawer;
    Energy energy;
    Transportation transportation;
    Spinner spinner;
    //StackBarChart stackBarChart;
    //StackedBarLabel labelOrganizer;
    //Yaxis yaxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_graph);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setUpNavigationView();*/

        spinner = (Spinner) findViewById(R.id.history_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.history_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        myHelper = new DatabaseHelper(this);
        myHelper.getWeekDrivingDistance();

        //sqLiteDatabase = myHelper.getWritableDatabase();

        graph = findViewById(R.id.graph);

        //series = new LineGraphSeries<>(getDataPoint());
        /*series.setColor(Color.rgb(226,91,34));
        series.setThickness(15);
        series.setDrawBackground(true);
        series.setBackgroundColor(Color.argb(60,95,226,156));
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(15);
        graph.addSeries(series);*/

        //barGraphSeries = new BarGraphSeries<>(displayCarbonFootprintGraph());

        /*barGraphSeries.setColor(Color.rgb(226,91,34));
        barGraphSeries.setDrawValuesOnTop(true);
        barGraphSeries.setValuesOnTopColor(Color.rgb(226,91,34));
        barGraphSeries.setSpacing(10);
        barGraphSeries.setTitle("LegendName");*/
        /*graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setTextSize(50);
        graph.getLegendRenderer().setTextColor(Color.GREEN);
        //graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setFixedPosition(12,45);
        /*barGraphSeries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                if (data.getY() > 6){
                    return  Color.rgb(0,225,0);
                } else if (data.getY() > 4){
                    return Color.rgb(0,0,225);
                } else {
                    return Color.rgb(225,0,0);
                }
            }
        });*/

        //graph.getGridLabelRenderer().setHumanRounding(true);
        //graph.getGridLabelRenderer().setNumHorizontalLabels(7);
        //graph.addSeries(barGraphSeries);

        /*graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(6);
        */
        /*
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(2);
        graph.getViewport().setMaxY(7);
        */
        //horizontal scrolling
        graph.getViewport().setScrollable(true);
        //vertical scrolling
        //graph.getViewport().setScalableY(true);

        //Scrolling and zooming horizontal
        //graph.getViewport().setScalable(true);
        //Scrolling and zooming vertical
        //graph.getViewport().setScalableY(true);

        /*graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX){
                    return sdf.format(new Date((long) value));
                    //show normal x values
                    //return super.formatLabel(value, isValueX);
                } else {
                    return super.formatLabel(value, isValueX);
                    //show currency for y values
                    //return super.formatLabel(value, isValueX) + " kgCO2";
                }
            }
        });*/

        //pointsGraphSeries = new PointsGraphSeries<>(getDataPoint());
        //pointsGraphSeries.setColor(R.color.green);
        //graph.addSeries(pointsGraphSeries);
    }

    @Override
    protected void onStart() {
        super.onStart();
        spinner.setSelection(0);

        //AsyncHistorySetup asyncHistorySetup = new AsyncHistorySetup(this, stackBarChart, labelOrganizer, yaxis);
        //asyngHistorySetup.execute();
    }

    /*private void setUpNavigationView() {
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
    }*/

    public void initSpinner() {
        spinner.setOnItemSelectedListener(spinnerHandler);
    }

    AdapterView.OnItemSelectedListener spinnerHandler = new AdapterView.OnItemSelectedListener(){
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            switch (pos) {
                case 0:
                    displayCarbonFootprintGraph();
                    break;
                case 1:
                    displayDistanceGraph();
                    break;
                case 2:
                    displayEnergyConsumptionGraph();
                    break;
            }
            /*
            stacBarChart.invalidate;
            labelOrganizer.invalidate;
            yaxis.invalidate;
             */
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    public ArrayList<String> getWeekLabels(){
        ArrayList<String> weekLabels = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        String[] ref = getResources().getStringArray(R.array.week_days);

        for (int i = 7; i > 0; i--){
            weekLabels.add(ref[(today - i) >= 0 ? today - i : today - i + 7]);
        }
        return weekLabels;
    }

    private void displayCarbonFootprintGraph() {
        //List<ChartData> value = new ArrayList<>();

        BarGraphSeries carbonFootprintGraph = new BarGraphSeries<>();
        barGraphSeries.setColor(R.color.blue);
        barGraphSeries.setDrawValuesOnTop(true);
        barGraphSeries.setValuesOnTopColor(Color.rgb(226,91,34));
        barGraphSeries.setSpacing(10);
        barGraphSeries.setTitle("Emission from energy");

        float[] carbonFootprintEnergy = energy.generateArrayWeekCarbonFootprint();

        for (int i = 0; i < carbonFootprintEnergy.length; i++) {
            carbonFootprintGraph.appendData(new DataPoint(i,(int) carbonFootprintEnergy[i]),true,20, false);
        }

        graph.addSeries(carbonFootprintGraph);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setTextSize(30);
        graph.getLegendRenderer().setTextColor(R.color.green);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX){
                    return super.formatLabel(value, isValueX);
                    /*//show normal x values
                    return super.formatLabel(value, isValueX);*/
                } else {
                    //return super.formatLabel(value, isValueX);
                    //show currency for y values
                    return super.formatLabel(value, isValueX) + " kgCO2";
                }
            }
        });
        //graph.getLegendRenderer().setFixedPosition(12,45);
        /*
        DataPoint[] dp = new DataPoint[carbonFootprintTransportation.length];

        for (int i = 0; i < dp.length; i++) {
            dp[i] = new DataPoint(i, carbonFootprintTransportation[i]);
        }
        return dp;
        //float[] carbonFootprintEnergy = energy.generateArrayWeekCarbonFootprint();

        /*Float[] value1 = new Float[7];
        //Float[] value2 = new Float[7];

        for (int i = 0; i < 7; i++) {
            value1[i] = carbonFootprintTransportation[i];
            //value2[i] = carbonFootprintTransportation[i];
        }*/

        //BarGraphSeries<DataPoint> carbonGraph = new BarGraphSeries<>();
        /*float[] dp2 = myHelper.getWeekDrivingDistance();

        for (int i = 0; i < dp2.length; i++){
            dp[i] = new DataPoint(i, dp2[i]);
        }*/
    }

    private void displayDistanceGraph() {
    }

    private void displayEnergyConsumptionGraph() {
    }

    //Series: used to fill the graph with data
    //Line graphs series, bargraphseries, pointsgraphseries
    private DataPoint[] getDataPoint() {
        //Read data from database
        /*float[] dp2 = myHelper.getWeekDrivingDistance();
        DataPoint[] dp = new DataPoint[dp2.length];

        for (int i = 0; i < dp2.length; i++){
            dp[i] = new DataPoint(i, dp2[i]);
        }*/
        DataPoint[] dp = new DataPoint[]{
                /*new DataPoint(new Date().getTime(),1),
                new DataPoint(new Date().getTime(),22),
                new DataPoint(new Date().getTime(),3),
                new DataPoint(new Date().getTime(),14),
                new DataPoint(new Date().getTime(),9),
                new DataPoint(new Date().getTime(),16),
                new DataPoint(new Date().getTime(),8),
                new DataPoint(new Date().getTime(),2),
                new DataPoint(new Date().getTime(),11),
                new DataPoint(new Date().getTime(),8),
                new DataPoint(new Date().getTime(),17)*/

                new DataPoint(0,1),
                new DataPoint(1,11),
                new DataPoint(2,5),
                new DataPoint(3,8),
                new DataPoint(4,5),
                new DataPoint(5,15),
                new DataPoint(6,9),
                new DataPoint(7,13),
                new DataPoint(9,1)
        };
        return(dp);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    //ViewPort: part of the graph that is currently visible on the screen
}
