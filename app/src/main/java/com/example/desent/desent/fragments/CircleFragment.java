package com.example.desent.desent.fragments;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.desent.desent.R;
import com.example.desent.desent.models.Indicator;
import com.example.desent.desent.utils.Utility;
import com.example.desent.desent.views.CircularIndicator;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.util.ArrayList;

/**
 * Created by celine on 28/04/17.
 */
public class CircleFragment extends Fragment {

    protected Indicator indicator;
    protected CircularIndicator circularIndicator;
    protected TextView caption;
    protected ImageView earthImage;

    //Circle category
    protected int startAngle = 270;
    protected int sweepAngle = 360;
    protected String imgName = "earth";
    protected int numberOfStates = 5;

    String captionText;

    DecoView arcView;
    int backIndex;
    int series1Index;
    int series2Index;
    float seriesMax = 50f;

    public void setIndicator(Indicator indicator) {
        this.indicator = indicator;
    }

    public Indicator getIndicator() {return this.indicator;}

    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public int getSweepAngle() {
        return sweepAngle;
    }

    public void setSweepAngle(int sweepAngle) {
        this.sweepAngle = sweepAngle;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public int getNumberOfStates() {
        return numberOfStates;
    }

    public void setNumberOfStates(int numberOfStates) {
        this.numberOfStates = numberOfStates;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_circle, container, false);
    }

    public void updateImgState(){
        int state = (int) (((numberOfStates - 1) * indicator.getSumValues()) / indicator.getMaxValue()) + 1;
        if (state < 1)
            state = 1;
        else if (state > numberOfStates)
            state = numberOfStates;
        Resources res = getResources();
        earthImage.setImageBitmap(BitmapFactory.decodeResource(res, res.getIdentifier(imgName + String.valueOf(state), "drawable", getActivity().getPackageName())));

    }

    public void refresh(){

        circularIndicator.setValues(indicator.getAverageValues());
        circularIndicator.invalidate();

        caption.setText(Utility.floatToStringNDecimals(indicator.getSumValues(), indicator.getDecimalsNumber()) + " " + indicator.getUnit());

        updateImgState();
    }

    public void updateViews(){
        caption.setText(Utility.floatToStringNDecimals(indicator.getSumValues(), indicator.getDecimalsNumber()) + " " + indicator.getUnit());
        updateImgState();

        circularIndicator.invalidate();
    }

    public void setUp(){

        circularIndicator.setValues(indicator.getAverageValues());

        captionText = Utility.floatToStringNDecimals(indicator.getSumValues(), indicator.getDecimalsNumber()) + " " + indicator.getUnit();

    }

    public void init(){

        //Colors
        int mGreen = ContextCompat.getColor(getActivity(), R.color.green);
        int mBlue = ContextCompat.getColor(getActivity(), R.color.blue);

        ArrayList<Integer> energyTransportationColors = new ArrayList<>();
        energyTransportationColors.add(mGreen);
        energyTransportationColors.add(mBlue);

        float[] values = {0,0};

        circularIndicator = getView().findViewById(R.id.circular_indicator);
        circularIndicator.setValues(values);
        circularIndicator.setColors(energyTransportationColors);
        circularIndicator.setStartAngle(this.startAngle);
        circularIndicator.setSweepAngle(this.sweepAngle);

        caption = getView().findViewById(R.id.caption);

        earthImage = getView().findViewById(R.id.image_view_earth);

        arcView = (DecoView) getView().findViewById(R.id.dynamicArcView);
        arcView.configureAngles(270,0);

        //create required data series on the decoview
        /*createBackSeries();
        createDataSeries1();
        createDataSeries2();

        //setup events to be fired on a schedule
        createEvents();*/

        /*SeriesItem seriesItem = new SeriesItem.Builder(R.color.blue).setRange(0,50,0).build();
        int backIndex = arcView.addSeries(seriesItem);
        /*final SeriesItem seriesItem = new SeriesItem.Builder(R.color.blue)
                .setRange(0, 50, 0)
                .build();*/

        /*int series1Index = arcView.addSeries(seriesItem);
        /*arcView = (DecoView) getView().findViewById(R.id.dynamicArcView);

        //create background track
        arcView.addSeries(new SeriesItem.Builder(Color.argb(255,218,218,218))
                .setRange(0,100,100)
                .setInitialVisibility(false)
                .setLineWidth(32f)
                .build());

        //create data series track
        SeriesItem seriesItem1 = new SeriesItem.Builder(R.color.green)
                .setRange(0,100,0)
                .setLineWidth(32f)
                .build();

        int series1Index = arcView.addSeries(seriesItem1);

        //create data series track
        SeriesItem seriesItem2 = new SeriesItem.Builder(R.color.blue)
                .setRange(0,100,0)
                .setLineWidth(32f)
                .build();

        int series2Index = arcView.addSeries(seriesItem2);

        arcView.configureAngles(280,0);

        //arcView.addSeries(new SeriesItem.Builder(Color.argb(255,218,218,218)).build());
        /*
        SeriesItem seriesItem3 = new SeriesItem.Builder(Color.argb(255,64,196,0))
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
        int series3Index = arcView.addSeries(seriesItem3);*/

        /*arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
                .setDelay(1000)
                .setDuration(2000)
                .build());*/

        /*arcView.addEvent(new DecoEvent.Builder(60).setIndex(series1Index).setDelay(4000).build());
        arcView.addEvent(new DecoEvent.Builder(100).setIndex(series2Index).setDelay(8000).build());
        //arcView.addEvent(new DecoEvent.Builder(10).setIndex(series2Index).setDelay(12000).build());

        arcView.setHorizGravity(DecoView.HorizGravity.GRAVITY_HORIZONTAL_LEFT);
        arcView.setVertGravity(DecoView.VertGravity.GRAVITY_VERTICAL_BOTTOM);*/
    }

    /*
    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, seriesMax,0)
                .setInitialVisibility(true)
                .build();
        backIndex = arcView.addSeries(seriesItem);
    }*/

    /*
    private void createDataSeries1() {
        final SeriesItem seriesItem = new SeriesItem.Builder(R.color.blue)
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .build();
    }*/

    /*
    private void createDataSeries2() {
        final SeriesItem seriesItem = new SeriesItem.Builder(R.color.green)
                .setRange(0, seriesMax, 0)
                .setInitialVisibility(false)
                .build();
    }*/

    /*private void createEvents() {
        arcView.executeReset();

        arcView.addEvent(new DecoEvent.Builder(seriesMax)
                .setIndex(backIndex)
                .setDuration(3000)
                .setDelay(100)
                .build());

        arcView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(series1Index)
                .setDuration(2000)
                .setDelay(1250)
                .build());

        arcView.addEvent(new DecoEvent.Builder(42.4f)
                .setIndex(series1Index)
                .setDelay(3250)
                .build());

        arcView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(series2Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(7000)
                .build());

        arcView.addEvent(new DecoEvent.Builder(16.3f)
                .setIndex(series2Index)
                .setDelay(8500)
                .build());

        arcView.addEvent(new DecoEvent.Builder(0).setIndex(series2Index).setDelay(18000).build());

        arcView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(series1Index)
                .setDelay(21000)
                .setDuration(3000)
                .setDisplayText("GOAL!")
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {

                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        createEvents();
                    }
                })
                .build());
    }*/
}
