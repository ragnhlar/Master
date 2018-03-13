package com.example.desent.desent.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.desent.desent.R;
import com.example.desent.desent.SessionManagement;
import com.example.desent.desent.fragments.RegisterConsentFragment;
import com.example.desent.desent.fragments.RegisterCredentialFragment;
import com.example.desent.desent.fragments.RegisterHousingFragment;
import com.example.desent.desent.fragments.RegisterPersonalFragment;
import com.example.desent.desent.fragments.RegisterTransportationFragment;
import com.example.desent.desent.fragments.RegisterTransportationHabitsFragment;
import com.example.desent.desent.models.DatabaseHelper;
import com.example.desent.desent.models.PreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static android.view.View.GONE;

/**
 * Created by celine on 06/07/17.
 */

public class RegisterActivity extends FragmentActivity {

    // To insert in database in phpmyadmin
    private RequestQueue requestQueue;
    private static final String URL_USER_REG = "http://129.241.105.20:80/smiling_earth/user_registration.php";
    private static final String URL_HOUSE_REG = "http://129.241.105.20:80/smiling_earth/house_registration.php";
    private static final String URL_TRANS_REG = "http://129.241.105.20:80/smiling_earth/trans_registration.php";
    private StringRequest request;

    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 6;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    SessionManagement session;
    PreferencesManager pref;
    SharedPreferences sharedPreferences;
    private DatabaseHelper myDb;

    private LinearLayout dotsLayout;
    private Button btnPrev, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        session = new SessionManagement(getApplicationContext());
        myDb = new DatabaseHelper(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        List fragments = new Vector();
        fragments.add(Fragment.instantiate(this, RegisterCredentialFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, RegisterPersonalFragment.class.getName()));
        //fragments.add(Fragment.instantiate(this,RegisterGeneralFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, RegisterHousingFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, RegisterTransportationHabitsFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, RegisterTransportationFragment.class.getName()));
        fragments.add(Fragment.instantiate(this, RegisterConsentFragment.class.getName()));
        //fragments.add(Fragment.instantiate(this, RegisterActivityFragment.class.getName()));
        this.mPagerAdapter = new MyPagerAdapter(super.getSupportFragmentManager(), fragments);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(viewPagerPageChangeListener);

        dotsLayout = findViewById(R.id.layoutDots);
        btnPrev = findViewById(R.id.btn_skip);
        btnNext = findViewById(R.id.btn_next);

        addBottomDots(0);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int current = getItem(-1);
                if (current > -1)
                    mPager.setCurrentItem(current);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < mPagerAdapter.getCount()) {
                    // move to next screen
                    System.out.println("Current: " + current);
                    System.out.println("mPagerAdapter: " + mPagerAdapter);
                    mPager.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private int getItem(int i) {
        return mPager.getCurrentItem() + i;
    }

    private void launchHomeScreen() {
        session.createLoginSession(myDb.getUserEmail());
        System.out.println("Consent: " + myDb.getConsent());
        //maybe in async task?
        /*Boolean reg = myDb.registerUser(sharedPreferences.getString("pref_key_personal_email",""),
                sharedPreferences.getString("pref_key_personal_name", ""),
                sharedPreferences.getString("pref_key_personal_gender", ""),
                sharedPreferences.getString("pref_key_personal_weight", ""),
                sharedPreferences.getString("pref_key_personal_birthdate", ""),
                sharedPreferences.getBoolean("pref_key_personal_consent", false));
        if (reg == true) {
            Toast.makeText(getApplicationContext(),"User registered in db", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"User not registered in db", Toast.LENGTH_LONG).show();
        }

        Boolean reg2 = myDb.registerHome2(sharedPreferences.getString("pref_key_personal_address",""),
                sharedPreferences.getString("pref_key_personal_zip_code", ""),
                sharedPreferences.getString("pref_key_personal_city", ""),
                sharedPreferences.getString("pref_key_b_year", ""),
                sharedPreferences.getString("pref_key_r_year", ""),
                sharedPreferences.getString("pref_key_heat_type", ""));
        if (reg2 == true) {
            Toast.makeText(getApplicationContext(),"Home registered in db", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"Home not registered in db", Toast.LENGTH_LONG).show();
        }

        Boolean reg3 = myDb.registerTHabits(
                sharedPreferences.getString("pref_key_transportation_habits", ""),
                sharedPreferences.getBoolean("pref_key_car_owner", false),
                sharedPreferences.getString("pref_key_car_reg_nr", ""),
                sharedPreferences.getString("pref_key_car_price", ""),
                sharedPreferences.getString("pref_key_car_distance", ""),
                sharedPreferences.getString("pref_key_car_ownership_period", ""),
                sharedPreferences.getBoolean("pref_key_car_owner_2", false),
                sharedPreferences.getString("pref_key_car_reg_nr_2", ""),
                sharedPreferences.getString("pref_key_car_price_2", ""),
                sharedPreferences.getString("pref_key_car_distance_2", ""),
                sharedPreferences.getString("pref_key_car_ownership_period_2", ""));
        System.out.println("Car owner: " + sharedPreferences.getBoolean("pref_key_car_owner", false));
        System.out.println("Reg nr car: " + sharedPreferences.getString("pref_key_car_reg_nr", ""));
        if (reg3 == true) {
            Toast.makeText(getApplicationContext(),"Transportation habits registered in db", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(),"Transportation habits not registered in db", Toast.LENGTH_LONG).show();
        }*/
        registerUser();
        registerHouseInfo();
        registerTransportation();
        /*request = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), "SUCCESS " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("email", sharedPreferences.getString("pref_key_personal_email","").toString());
                hashMap.put("password", sharedPreferences.getString("pref_key_personal_password","").toString());
                hashMap.put("name", sharedPreferences.getString("pref_key_personal_name","").toString());
                hashMap.put("gender", sharedPreferences.getString("pref_key_personal_gender","").toString());
                hashMap.put("weight", sharedPreferences.getString("pref_key_personal_weight","").toString());
                hashMap.put("birthdate", sharedPreferences.getString("pref_key_personal_birthdate","").toString());
                hashMap.put("consent", sharedPreferences.getString("pref_key_personal_consent","").toString());
                hashMap.put("address", sharedPreferences.getString("pref_key_personal_address","").toString());
                hashMap.put("zip_code", sharedPreferences.getString("pref_key_personal_zip_code","").toString());
                hashMap.put("city", sharedPreferences.getString("pref_key_personal_city","").toString());
                return hashMap;
            }
        };
        requestQueue.add(request);*/

        //myDb.registerUser();

        startActivity(new Intent(RegisterActivity.this, MainActivity.class));

        finish();

        /*if (myDb.getConsent() == "true") {
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        } else if (myDb.getConsent() == "false"){
            //må vise dialogboks å forklare, må starte aktivitet allikevel, men da kan ikke data brukes
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }*/
    }

    private void registerTransportation() {
        request = new StringRequest(Request.Method.POST, URL_TRANS_REG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), "SUCCESS " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("habits", sharedPreferences.getString("pref_key_transportation_habits","").toString());
                hashMap.put("car_owner_1", sharedPreferences.getString("pref_key_car_owner","").toString());
                hashMap.put("reg_nr_1", sharedPreferences.getString("pref_key_reg_nr","").toString());
                hashMap.put("car_value_1", sharedPreferences.getString("pref_key_car_price","").toString());
                hashMap.put("yearly_driving_distance_1", sharedPreferences.getString("pref_key_car_distance","").toString());
                hashMap.put("dur_ownership_1", sharedPreferences.getString("pref_key_car_ownership_period","").toString());
                hashMap.put("car_owner_2", sharedPreferences.getString("pref_key_car_owner_2","").toString());
                hashMap.put("reg_nr_2", sharedPreferences.getString("pref_key_reg_nr_2","").toString());
                hashMap.put("car_value_2", sharedPreferences.getString("pref_key_car_price_2","").toString());
                hashMap.put("yearly_driving_distance_2", sharedPreferences.getString("pref_key_car_distance_nr_2","").toString());
                hashMap.put("dur_ownership_2", sharedPreferences.getString("pref_key_car_ownership_period_2","").toString());

                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    private void registerHouseInfo() {
        request = new StringRequest(Request.Method.POST, URL_HOUSE_REG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), "SUCCESS " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("address", sharedPreferences.getString("pref_key_personal_address","").toString());
                hashMap.put("zip_code", sharedPreferences.getString("pref_key_personal_zip_code","").toString());
                hashMap.put("city", sharedPreferences.getString("pref_key_personal_city","").toString());
                hashMap.put("heat_type", sharedPreferences.getString("pref_key_heat_type","").toString());
                hashMap.put("b_year", sharedPreferences.getString("pref_key_b_year","").toString());
                hashMap.put("r_year", sharedPreferences.getString("pref_key_r_year","").toString());
                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    private void registerUser() {
        request = new StringRequest(Request.Method.POST, URL_USER_REG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.names().get(0).equals("success")) {
                        Toast.makeText(getApplicationContext(), "SUCCESS " + jsonObject.getString("success"), Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("email", sharedPreferences.getString("pref_key_personal_email","").toString());
                hashMap.put("password", sharedPreferences.getString("pref_key_personal_password","").toString());
                hashMap.put("name", sharedPreferences.getString("pref_key_personal_name","").toString());
                hashMap.put("gender", sharedPreferences.getString("pref_key_personal_gender","").toString());
                hashMap.put("weight", sharedPreferences.getString("pref_key_personal_weight","").toString());
                hashMap.put("birthdate", sharedPreferences.getString("pref_key_personal_birthdate","").toString());
                hashMap.put("consent", sharedPreferences.getString("pref_key_personal_consent","").toString());
                hashMap.put("address", sharedPreferences.getString("pref_key_personal_address","").toString());
                hashMap.put("zip_code", sharedPreferences.getString("pref_key_personal_zip_code","").toString());
                hashMap.put("city", sharedPreferences.getString("pref_key_personal_city","").toString());
                return hashMap;
            }
        };
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }private void addBottomDots(int currentPage) {
        TextView[] dots = new TextView[mPagerAdapter.getCount()];

        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        //TODO: fix
        TypedValue value = new TypedValue ();
        getTheme ().resolveAttribute (R.attr.colorAccent, value, true);
        int colorAccent = value.data;

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorAccent);
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener;

    {
        viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);

                // changing the next button text 'NEXT' / 'GOT IT'
                if (position == mPagerAdapter.getCount() - 1) {
                    // last page. make button text to GOT IT
                    btnNext.setText(getString(R.string.start));
                    //btnNext.setText("FINISH");
                    //btnPrev.setText("DECLINE");
                    //launch homescreen? and check and save permission
                } else {
                    // still pages are left
                    btnNext.setText(getString(R.string.next));
                }

                if (position == 0)
                    btnPrev.setVisibility(GONE);
                else
                    btnPrev.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        };
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private final List fragments;

        public MyPagerAdapter(FragmentManager fm, List fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return (Fragment) this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }
    }
}
