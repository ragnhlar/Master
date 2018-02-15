package com.example.desent.desent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.desent.desent.activities.LoginActivity;

import java.util.HashMap;

/**
 * Created by ragnhlar on 15.02.2018.
 */

public class SessionManagement {

    //shared preferences
    SharedPreferences pref;

    //editor for shared preferences
    SharedPreferences.Editor editor;

    //context
    Context _context;

    //shared pref mode
    int PRIVATE_MODE = 0;

    //shared pref file name
    private static final String PREF_NAME = "AndroidPref";

    //all shared preference keys
    private static final String IS_LOGIN = "IsLoggedIn";

    /*
    //username (make variable public to access from outside)
    public static final String KEY_NAME = "name";*/

    //Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    //constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //create login session
    //String name,
    public void createLoginSession(String email) {
        //storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        /*
        //storing name in pref
        editor.putString(KEY_NAME, name);*/

        //storing email in pref
        editor.putString(KEY_EMAIL, email);

        //commit changes
        editor.commit();
    }

    //get stored session data
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        //username
        /*
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));*/

        //user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        //return user
        return user;
    }

    //check login method will check user login status
    //if false it will redirect user to login page
    //else won't do anything
    public void checkLogin() {
        //check login status
        if (!this.isLoggedIn()){
            //user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            //closing all the activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //add new flag to start new activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //starting login activity
            _context.startActivity(i);
        }
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    //clear session details
    public void logoutUser() {
        //clearing all data from shared preferences
        editor.clear();
        editor.commit();

        //after logout redirect user to login activity
        Intent i = new Intent(_context, LoginActivity.class);
        //closing all the activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //add new flag to start new activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //starting login activity
        _context.startActivity(i);
    }
}
