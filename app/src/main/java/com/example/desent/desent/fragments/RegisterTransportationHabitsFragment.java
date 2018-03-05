package com.example.desent.desent.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.desent.desent.R;

/**
 * Created by ragnhlar on 28.02.2018.
 */

public class RegisterTransportationHabitsFragment extends Fragment{

    private boolean isProfileValid;

    private ImageView img;

    private CheckBox checkBoxWalk;
    private CheckBox checkBoxBicycle;
    private CheckBox checkBoxCar;
    private CheckBox checkBoxBus;
    private CheckBox checkBoxTrain;
    private CheckBox checkBoxMotorcycle;
    private CheckBox checkBoxScooter;
    private CheckBox checkBoxTram;
    private CheckBox checkBoxSubway;
    private CheckBox checkBoxFerry;
    private CheckBox checkBoxOther;

    //private Spinner carSizeSpinner;
    //private EditText priceTextView;
    //private EditText drivingDistanceTextView;
    //private EditText ownershipPeriodTextView;
    private SharedPreferences sharedPreferences;

    private String habits;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getTheme().applyStyle(R.style.AppTheme_NoActionBar_AccentColorGreen, true);

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_register_transportation_habits, container, false);

        img = (ImageView) rootView.findViewById(R.id.image);
        img.setImageResource(R.drawable.circle_green);

        habits = "";

        checkBoxWalk = (CheckBox) rootView.findViewById(R.id.cbWalk);
        checkBoxWalk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxWalk.isChecked()){
                    habits += "Walk, ";
                }
            }
        });
        checkBoxBicycle = (CheckBox) rootView.findViewById(R.id.cbBicycle);
        checkBoxBicycle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxWalk.isChecked()){
                    habits += "Cycle, ";
                }
            }
        });
        checkBoxCar = (CheckBox) rootView.findViewById(R.id.cbCar);
        checkBoxCar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxWalk.isChecked()){
                    habits += "Car, ";
                }
            }
        });
        checkBoxBus = (CheckBox) rootView.findViewById(R.id.cbBus);
        checkBoxBus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxWalk.isChecked()){
                    habits += "Bus, ";
                }
            }
        });
        checkBoxTrain = (CheckBox) rootView.findViewById(R.id.cbTrain);
        checkBoxTrain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxWalk.isChecked()){
                    habits += "Train, ";
                }
            }
        });
        checkBoxMotorcycle = (CheckBox) rootView.findViewById(R.id.cbMotorcycle);
        checkBoxMotorcycle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxWalk.isChecked()){
                    habits += "Motorcycle, ";
                }
            }
        });
        checkBoxScooter = (CheckBox) rootView.findViewById(R.id.cbScooter);
        checkBoxScooter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxWalk.isChecked()){
                    habits += "Scooter, ";
                }
            }
        });
        checkBoxTram = (CheckBox) rootView.findViewById(R.id.cbTram);
        checkBoxTram.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxWalk.isChecked()){
                    habits += "Tram, ";
                }
            }
        });
        checkBoxSubway = (CheckBox) rootView.findViewById(R.id.cbSubway);
        checkBoxSubway.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxWalk.isChecked()){
                    habits += "Subway, ";
                }
            }
        });
        checkBoxFerry = (CheckBox) rootView.findViewById(R.id.cbFerry);
        checkBoxFerry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxWalk.isChecked()){
                    habits += "Ferry, ";
                }
            }
        });
        checkBoxOther = (CheckBox) rootView.findViewById(R.id.cbOther);
        checkBoxOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkBoxWalk.isChecked()){
                    habits += "Other.";
                }
            }
        });


        /*
        carOwner = rootView.findViewById(R.id.checkbox_car_owner);
        carOwner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onCheckboxClicked(compoundButton, b);
            }

        });

        carSizeSpinner = rootView.findViewById(R.id.spinner_car_size);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.pref_car_size_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        carSizeSpinner.setAdapter(adapter);

        priceTextView = rootView.findViewById(R.id.car_price);
        drivingDistanceTextView = rootView.findViewById(R.id.car_driving_distance);
        ownershipPeriodTextView = rootView.findViewById(R.id.car_ownership_period);
        */
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        //checked(getView());
        restorePreferences();
        //carOwner.callOnClick();

        return rootView;
    }

    /*public String checked(View v){
        switch (v.getId()) {
            case R.id.cbWalk:
                habits += "Walk, ";
                break;
            case R.id.cbBicycle:
                habits += "Bicycle, ";
                break;
            case R.id.cbCar:
                habits += "Car, ";
                break;
            case R.id.cbBus:
                habits += "Bus, ";
                break;
            case R.id.cbTrain:
                habits += "Train, ";
                break;
            case R.id.cbMotorcycle:
                habits += "Motorcycle, ";
                break;
            case R.id.cbScooter:
                habits += "Scooter, ";
                break;
            case R.id.cbTram:
                habits += "Tram, ";
                break;
            case R.id.cbSubway:
                habits += "Subway, ";
                break;
            case R.id.cbFerry:
                habits += "Ferry, ";
                break;
            case R.id.cbOther:
                habits += "Other.";
                break;
        }
        System.out.println("Habits: " + habits);
        return habits;
    }*/

    public void onCheckboxClicked(CompoundButton compoundButton, boolean b) {

        /*carSizeSpinner.setEnabled(b);
        priceTextView.setEnabled(b);
        drivingDistanceTextView.setEnabled(b);
        ownershipPeriodTextView.setEnabled(b);
        */
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println("Habits: " + habits);
        editor.putString("pref_key_transportation_habits",habits);
        /*
        if(carOwner.isChecked()){
            editor.putBoolean("pref_key_car_owner", carOwner.isChecked());
            editor.putString("pref_key_car_size", carSizeSpinner.getSelectedItem().toString());
            editor.putString("pref_key_car_price", String.valueOf(priceTextView.getText()));
            editor.putString("pref_key_car_distance", String.valueOf(drivingDistanceTextView.getText()));
            editor.putString("pref_key_car_ownership_period", String.valueOf(ownershipPeriodTextView.getText()));
        }else{
            editor.putBoolean("pref_key_car_owner", false);
            /*
            editor.putString("pref_key_car_size", ;
            editor.putString("pref_key_car_price", String.valueOf(priceTextView.getText()));
            editor.putString("pref_key_car_distance", String.valueOf(drivingDistanceTextView.getText()));
            editor.putString("pref_key_car_ownership_period", String.valueOf(ownershipPeriodTextView.getText()));
        */
        /*}
        */
        editor.commit();
    }

    private void restorePreferences(){

        /*carOwner.setChecked(sharedPreferences.getBoolean("pref_key_car_owner", true));
        carSizeSpinner.setSelection(((ArrayAdapter<String>) carSizeSpinner.getAdapter()).getPosition(sharedPreferences.getString("pref_key_car_size", "Small")));
        priceTextView.setText(sharedPreferences.getString("pref_key_car_price", "300000"), TextView.BufferType.EDITABLE);
        drivingDistanceTextView.setText(sharedPreferences.getString("pref_key_car_distance", "8000"), TextView.BufferType.EDITABLE);
        ownershipPeriodTextView.setText(sharedPreferences.getString("pref_key_car_ownership_period", "3"), TextView.BufferType.EDITABLE);
        */
    }
}
