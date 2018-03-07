package com.example.desent.desent.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.desent.desent.R;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView name, email, address, zipcode, city, birthdate, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        name = findViewById(R.id.tvName);
        name.setText(sharedPreferences.getString("pref_key_personal_name",""));

        email = findViewById(R.id.tvEmail);
        email.setText(sharedPreferences.getString("pref_key_personal_email",""));

        address = findViewById(R.id.tvAddress);
        address.setText(sharedPreferences.getString("pref_key_personal_address", ""));

        zipcode = findViewById(R.id.tvZipcode);
        zipcode.setText(sharedPreferences.getString("pref_key_personal_zip_code",""));

        city = findViewById(R.id.tvCity);
        city.setText(sharedPreferences.getString("pref_key_personal_city",""));

        birthdate = findViewById(R.id.tvBirthdate);
        birthdate.setText(sharedPreferences.getString("pref_key_personal_birthdate", ""));

        gender = findViewById(R.id.tvGender);
        gender.setText(sharedPreferences.getString("pref_key_personal_gender",""));
    }
}
