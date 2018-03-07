package com.example.desent.desent.models;

import android.content.Context;

/**
 * Created by ragnhlar on 07.03.2018.
 */

public class ActiveMinutes extends Indicator {

    public ActiveMinutes(String name, String unit, String explanation, Transportation transport, Context context) {
        super(name, unit, explanation, transport);
    }

    protected float calculateActiveMinutesFromWalking(float distance){
        return 10*distance; //10 min per km, find accurate formula
    }

    protected float calculateActiveMinutesFromCycling(float distance){
        return 3*distance; //3 min per km, find accurate formula
    }

    @Override
    public void calculateValues() {

        switch (estimationType) {

            case NONE:
                averageValues[0] = calculateActiveMinutesFromWalking(transport.getWalkingDistance(timeScale)) + calculateActiveMinutesFromCycling(transport.getCyclingDistance(timeScale)/1000);
                break;
            case SOLAR_INSTALLATION:
                averageValues[0] = calculateActiveMinutesFromWalking(transport.getWalkingDistance(timeScale)) + calculateActiveMinutesFromCycling(transport.getCyclingDistance(timeScale)/1000);
                break;
            case WALKING:
                averageValues[0] = calculateActiveMinutesFromWalking(this.walkingDistance) + calculateActiveMinutesFromCycling(transport.getCyclingDistance(timeScale)/1000);
                break;
            case CYCLING:
                averageValues[0] = calculateActiveMinutesFromWalking(transport.getWalkingDistance(timeScale)) + calculateActiveMinutesFromCycling(this.cyclingDistance);
                break;
            case ELECTRIC_CAR:
                averageValues[0] = calculateActiveMinutesFromWalking(transport.getWalkingDistance(timeScale)) + calculateActiveMinutesFromCycling(transport.getCyclingDistance(timeScale)/1000);
                break;
        }
    }
}
