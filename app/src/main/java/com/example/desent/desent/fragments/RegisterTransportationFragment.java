package com.example.desent.desent.fragments;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.desent.desent.R;

/**
 * Created by celine on 11/07/17.
 */

public class RegisterTransportationFragment extends Fragment {

    private ScrollView scrollView;

    //private boolean isProfileValid;
    private CheckBox carOwner;
    private EditText priceTextView;
    private EditText drivingDistanceTextView;
    private EditText ownershipPeriodTextView;
    private EditText editTextRegNr;

    //private Spinner carSizeSpinner;

    private CheckBox carOwner2;
    private EditText priceTextView2;
    private EditText drivingDistanceTextView2;
    private EditText ownershipPeriodTextView2;
    private EditText editTextRegNr2;
    private LinearLayout car_nr_2_info;

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getTheme().applyStyle(R.style.AppTheme_NoActionBar_AccentColorGreen, true);

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_register_transportation, container, false);

        scrollView = (ScrollView) rootView.findViewById(R.id.scrollViewTransportation);

        //TODO: button
        carOwner = rootView.findViewById(R.id.checkbox_car_owner);
        editTextRegNr = rootView.findViewById(R.id.reg_nr);
        priceTextView = rootView.findViewById(R.id.car_price);
        drivingDistanceTextView = rootView.findViewById(R.id.car_driving_distance);
        ownershipPeriodTextView = rootView.findViewById(R.id.car_ownership_period);
        //carOwner.setChecked(true);
        carOwner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                onCheckboxClicked(compoundButton, b);
            }

        });

        //carSizeSpinner = rootView.findViewById(R.id.spinner_car_size);
        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.pref_car_size_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        carSizeSpinner.setAdapter(adapter);
        */

        carOwner2 = rootView.findViewById(R.id.checkbox_car_owner_2);
        editTextRegNr2 = rootView.findViewById(R.id.reg_nr_car_2);
        priceTextView2 = rootView.findViewById(R.id.car_price_nr_2);
        drivingDistanceTextView2 = rootView.findViewById(R.id.car_driving_distance_nr_2);
        ownershipPeriodTextView2 = rootView.findViewById(R.id.car_ownership_period_nr_2);
        car_nr_2_info = rootView.findViewById(R.id.car_nr_2_info);
        //carOwner.setChecked(true);
        carOwner2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                car_nr_2_info.setVisibility(View.VISIBLE);
                onCheckboxClicked(compoundButton, b);
            }

        });

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        restorePreferences();
        //carOwner.callOnClick();

        return rootView;
    }

    public void onCheckboxClicked(CompoundButton compoundButton, boolean b) {
        //carSizeSpinner.setEnabled(b);
        editTextRegNr.setEnabled(b);
        priceTextView.setEnabled(b);
        drivingDistanceTextView.setEnabled(b);
        ownershipPeriodTextView.setEnabled(b);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("pref_key_car_owner", carOwner.isChecked());
        System.out.println("Car owner ODW: " + carOwner.isChecked());
        //editor.putString("pref_key_car_size", carSizeSpinner.getSelectedItem().toString());
        //editor.putString("pref_key_car_price", priceTextView.getText().toString());
        editor.putString("pref_key_car_price", String.valueOf(priceTextView.getText()));
        System.out.println("Car price ODW: " + priceTextView.getText());
        editor.putString("pref_key_reg_nr", editTextRegNr.getText().toString());
        System.out.println("Car reg nr ODW: " + editTextRegNr.getText());
        editor.putString("pref_key_car_distance", drivingDistanceTextView.getText().toString());
        editor.putString("pref_key_car_ownership_period", ownershipPeriodTextView.getText().toString());

        if (carOwner2.isChecked()) {
            editor.putBoolean("pref_key_car_owner_2", carOwner2.isChecked());
            editor.putString("pref_key_car_price_2", priceTextView2.getText().toString());
            editor.putString("pref_key_reg_nr_2", editTextRegNr2.getText().toString());
            editor.putString("pref_key_car_distance_nr_2", drivingDistanceTextView2.getText().toString());
            editor.putString("pref_key_car_ownership_period", ownershipPeriodTextView2.getText().toString());
        }

        /*if(carOwner.isChecked()){
            editor.putBoolean("pref_key_car_owner", carOwner.isChecked());
            //editor.putString("pref_key_car_size", carSizeSpinner.getSelectedItem().toString());
            editor.putString("pref_key_car_price", String.valueOf(priceTextView.getText()));
            editor.putString("pref_key_reg_nr", String.valueOf(editTextRegNr.getText()));
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
        editor.commit();
    }

    private void restorePreferences(){
        carOwner.setChecked(sharedPreferences.getBoolean("pref_key_car_owner", true));
        //carSizeSpinner.setSelection(((ArrayAdapter<String>) carSizeSpinner.getAdapter()).getPosition(sharedPreferences.getString("pref_key_car_size", "Small")));
        editTextRegNr.setText(sharedPreferences.getString("pref_key_reg_nr", "HJ 20005"), TextView.BufferType.EDITABLE);
        priceTextView.setText(sharedPreferences.getString("pref_key_car_price", "300000"), TextView.BufferType.EDITABLE);
        drivingDistanceTextView.setText(sharedPreferences.getString("pref_key_car_distance", "8000"), TextView.BufferType.EDITABLE);
        ownershipPeriodTextView.setText(sharedPreferences.getString("pref_key_car_ownership_period", "3"), TextView.BufferType.EDITABLE);

        carOwner2.setChecked(sharedPreferences.getBoolean("pref_key_car_owner_2", false));
        //carSizeSpinner.setSelection(((ArrayAdapter<String>) carSizeSpinner.getAdapter()).getPosition(sharedPreferences.getString("pref_key_car_size", "Small")));
        editTextRegNr2.setText(sharedPreferences.getString("pref_key_reg_nr_2", "HJ 20005"), TextView.BufferType.EDITABLE);
        priceTextView2.setText(sharedPreferences.getString("pref_key_car_price_2", "300000"), TextView.BufferType.EDITABLE);
        drivingDistanceTextView2.setText(sharedPreferences.getString("pref_key_car_distance_2", "8000"), TextView.BufferType.EDITABLE);
        ownershipPeriodTextView2.setText(sharedPreferences.getString("pref_key_car_ownership_period_2", "3"), TextView.BufferType.EDITABLE);
    }
}
