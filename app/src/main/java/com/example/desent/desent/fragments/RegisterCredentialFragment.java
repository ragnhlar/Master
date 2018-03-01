package com.example.desent.desent.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.desent.desent.R;
import com.example.desent.desent.models.DatabaseHelper;
import com.example.desent.desent.utils.AESHelper;
import com.example.desent.desent.utils.Utility;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ragnhlar on 27.02.2018.
 */

public class RegisterCredentialFragment extends android.support.v4.app.Fragment {

    private final static String CIPHER_KEY = "p2m8j0DgoqjJGxnDYfq70fV92h7sCg0N";

    private ImageView image;
    private EditText emailTextView;
    private EditText passwordTextView;
    private EditText confirmPasswordTextView;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper myDb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myDb = new DatabaseHelper(getContext());

        getActivity().getTheme().applyStyle(R.style.AppTheme_NoActionBar_AccentColorGreen, true);

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_register_credential, container, false);

        image = (ImageView)rootView.findViewById(R.id.imageEarth);
        image.setImageResource(R.drawable.earth1);

        emailTextView = rootView.findViewById(R.id.emailInput);
        passwordTextView = rootView.findViewById(R.id.passwordInput);
        confirmPasswordTextView = rootView.findViewById(R.id.passwordConfirm);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        restorePreferences();

        setErrors();

        return rootView;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("pref_key_personal_email", String.valueOf(emailTextView.getText()));
        System.out.println("Email in registration: " + sharedPreferences.getString("pref_key_personal_email", null));

        try {
            editor.putString("pref_key_personal_password", AESHelper.encrypt(CIPHER_KEY, String.valueOf(passwordTextView.getText())));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        myDb.insertEmail(emailTextView.getText().toString());
        editor.commit();
    }

    private void setErrors(){

        emailTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String email = String.valueOf(emailTextView.getText());
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    emailTextView.setError(getString(R.string.error_invalid_email));
                else
                    emailTextView.setError(null);
            }
        });

        passwordTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String password = String.valueOf(passwordTextView.getText());
                if (password.length() < 8)
                    passwordTextView.setError(getString(R.string.error_password_too_short));
                else
                    passwordTextView.setError(null);
            }
        });

        confirmPasswordTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (!(String.valueOf(confirmPasswordTextView.getText()).equals(String.valueOf(passwordTextView.getText()))))
                    confirmPasswordTextView.setError(getString(R.string.error_password_not_match));
                else
                    passwordTextView.setError(null);
            }
        });
    }

    private void restorePreferences(){

        emailTextView.setText(sharedPreferences.getString("pref_key_personal_email", ""), TextView.BufferType.EDITABLE);

        try {
            passwordTextView.setText(AESHelper.decrypt(CIPHER_KEY, sharedPreferences.getString("pref_key_personal_password", "")), TextView.BufferType.EDITABLE);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
