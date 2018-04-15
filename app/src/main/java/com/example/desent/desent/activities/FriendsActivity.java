package com.example.desent.desent.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.desent.desent.R;
import com.example.desent.desent.SessionManagement;
import com.example.desent.desent.models.Constants;
import com.example.desent.desent.models.Friend;
import com.example.desent.desent.models.FriendAdapter;
import com.example.desent.desent.models.RequestHandler;
import com.example.desent.desent.models.Score;
import com.example.desent.desent.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private ProgressDialog progressDialog;

    DrawerLayout drawer;

    FriendAdapter adapter;
    List<Friend> friendList;
    RecyclerView recyclerView;

    //SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        setUpNavigationView();

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.rvFriends);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the friendlist
        friendList = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        //loadFriends();

        /*friendList.add(
                new Friend(
                        1,
                        "Navn Navnesen 1",
                        35,
                        4.5,
                        R.drawable.earth1));

        friendList.add(
                new Friend(
                        2,
                        "Navn Navnesen 2",
                        43,
                        3.2,
                        R.drawable.earth2));

        friendList.add(
                new Friend(
                        3,
                        "Navn Navnesen 3",
                        13,
                        6.7,
                        R.drawable.earth4));*/
        //friendList.add(new Friend(0, sharedPreferences.getString("pref_key_personal_name",""), 167, 3.6, R.drawable.earth));
        friendList.add(new Friend(1, "Rob Adams", 135, 3.2, R.drawable.robadams));
        friendList.add(new Friend(2, "Mary Jones", 230, 2.2, R.drawable.maryjones));
        friendList.add(new Friend(3, "Patricia Clarkson", 53, 6.2, R.drawable.patriciaclarkson));
        friendList.add(new Friend(4, "Michael Smith", 310, 1.2, R.drawable.michaelsmith));
        friendList.add(new Friend(5, "James Pitt", 32, 6.4, R.drawable.jamespitt));

        //creating recyclerview adapter
        FriendAdapter adapter = new FriendAdapter(this, friendList);

        //setting adapter to recyclerview
        recyclerView.setAdapter(adapter);
    }

    /*
    private void loadFriends() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                Constants.URL_RETRIEVE_FRIENDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONArray scores = new JSONArray(response);

                            for (int i = 0; i < scores.length(); i++){
                                JSONObject scoreObject = scores.getJSONObject(i);

                                int id = scoreObject.getInt("id");
                                //String email = scoreObject.getString("email");
                                String name = scoreObject.getString("name");
                                int num_coins = scoreObject.getInt("num_coins");
                                /*int walk = scoreObject.getInt("walk");
                                int cycle = scoreObject.getInt("cycle");
                                int drive = scoreObject.getInt("drive");*/
    /*
                                double avg_cf = scoreObject.getDouble("avg_cf");

                                Friend friend = new Friend(id, name, num_coins, avg_cf, R.drawable.earth1);
                                friendList.add(friend);
                            }
                            //creating recyclerview adapter
                            adapter = new FriendAdapter(FriendsActivity.this, friendList);

                            //setting adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
    */

    protected void setUpNavigationView() {
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
            bitmap.recycle();
            bitmap = null;
            System.gc();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(FriendsActivity.this, MainActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_history) {
            startActivity(new Intent(FriendsActivity.this, HistoryActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(FriendsActivity.this, SettingsActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_user_profile) {
            startActivity(new Intent(FriendsActivity.this, ProfileActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_friend_list) {
            //startActivity(new Intent(FriendsActivity.this, FriendsActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_leaderboard) {
            startActivity(new Intent(FriendsActivity.this, LeaderboardActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_info_app) {
            startActivity(new Intent(FriendsActivity.this, InformationActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_about_app) {
            startActivity(new Intent(FriendsActivity.this, AboutUsActivity.class));
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_log_out){
            SessionManagement session = new SessionManagement(getApplicationContext());
            session.logoutUser();
            drawer.closeDrawer(GravityCompat.START);
        }
        /*else if (id == R.id.nav_about_us) {
            drawer.closeDrawer(GravityCompat.START);
        }*/
        return true;
    }
}
