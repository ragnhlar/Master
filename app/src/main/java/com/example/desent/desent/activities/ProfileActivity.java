package com.example.desent.desent.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.desent.desent.R;
import com.example.desent.desent.SessionManagement;
import com.example.desent.desent.models.DatabaseHelper;
import com.example.desent.desent.models.Energy;
import com.example.desent.desent.models.Transportation;
import com.example.desent.desent.utils.Utility;
import com.example.desent.desent.views.StackBarChart;
import com.example.desent.desent.views.StackedBarLabel;
import com.example.desent.desent.views.Yaxis;

import java.io.FileNotFoundException;

public class ProfileActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView name, email, address, zipcode, city, birthdate, gender;
    ImageView changeInfo, profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        changeInfo = findViewById(R.id.changeUserInfo);
        profilePic = findViewById(R.id.imgProfilePicture);
        //profilePic.setImageResource(sharedPreferences.getString("pref_key_profile_picture",""));

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
        birthdate.setText((sharedPreferences.getString("pref_key_personal_birthdate", "")));

        gender = findViewById(R.id.tvGender);
        gender.setText(sharedPreferences.getString("pref_key_personal_gender",""));
    }
}
