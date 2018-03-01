package com.example.desent.desent.fragments;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.desent.desent.R;

/**
 * Created by celine on 11/07/17.
 */

public class RegisterHousingFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private RadioGroup heatingTypeRadioGroup;

    private Spinner spinnerBuildingYear;
    private Spinner spinnerRenovationYear;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().getTheme().applyStyle(R.style.AppTheme_NoActionBar_AccentColorBlue, true);

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_register_housing, container, false);

        heatingTypeRadioGroup = rootView.findViewById(R.id.radio_group_heating);

        spinnerBuildingYear = rootView.findViewById(R.id.spinnerRenovation);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.year_of_renovation_construction_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBuildingYear.setAdapter(adapter);
        spinnerBuildingYear.setSelection(adapter.getPosition("1961 - 1970"));

        spinnerRenovationYear = rootView.findViewById(R.id.spinnerBuilding);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.year_of_renovation_construction_array, android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRenovationYear.setAdapter(adapter2);
        spinnerRenovationYear.setSelection(adapter.getPosition("2011 - 2015"));


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        restorePreferences();

        return rootView;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        String heatType = (String) ((RadioButton) getView().findViewById(heatingTypeRadioGroup.getCheckedRadioButtonId())).getText();

        editor.putString("pref_key_heat_type", heatType);

        editor.commit();

    }

    private void restorePreferences(){

        String heatType = sharedPreferences.getString("pref_key_heat_type", "Electric (resistance)");

        switch(heatType) {

            case "Electric (resistance)":
                heatingTypeRadioGroup.check(R.id.radio_button_electric);
                break;
            case "Heat pump":
                heatingTypeRadioGroup.check(R.id.radio_button_heat_pump);
                break;
            case "Gas":
                heatingTypeRadioGroup.check(R.id.radio_button_gas);
                break;
            case "Oil":
                heatingTypeRadioGroup.check(R.id.radio_button_oil);
                break;
            case "Wood":
                heatingTypeRadioGroup.check(R.id.radio_button_wood);
                break;
            case "Solar panel":
                heatingTypeRadioGroup.check(R.id.radio_button_solar_panel);
                break;
        }

    }
}
