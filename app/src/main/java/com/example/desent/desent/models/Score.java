package com.example.desent.desent.models;

import android.support.annotation.NonNull;

/**
 * Created by ragnhildlarsen on 18.03.2018.
 */

public class Score implements Comparable<Score>{

    private int id;
    private String name;
    private int num_coins;
    private double avg_cf;
    private int image;

    public Score(int id, String name, int num_coins, double avg_cf, int image) {
        this.id = id;
        this.name = name;
        this.num_coins = num_coins;
        this.avg_cf = avg_cf;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNum_coins() {
        return num_coins;
    }

    public double getAvg_cf() {
        return avg_cf;
    }

    public int getImage() {
        return image;
    }

    @Override
    public int compareTo(@NonNull Score score) {
        double compareAvgCf = score.getAvg_cf();
        return Double.compare(this.avg_cf, compareAvgCf);
    }
    /*
    @Override
    public int CompareTo(Score score){
        int compareNumCoins = score.getNum_coins();
        return compareNumCoins - this.num_coins;

    }*/

}
