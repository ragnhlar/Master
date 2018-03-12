package com.example.desent.desent.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
 * Created by ragnhlar on 28.02.2018.
 */

public class RegisterConsentFragment extends Fragment {

    /*private final static String CIPHER_KEY = "p2m8j0DgoqjJGxnDYfq70fV92h7sCg0N";

    private boolean isProfileValid;
    private ImageView profilePic;
    private EditText nameTextView;
    private EditText emailTextView;
    private EditText passwordTextView;
    private EditText confirmPasswordTextView;*/
    private SharedPreferences sharedPreferences;
    //private Uri imageUri;
    private DatabaseHelper myDb;
    private CheckBox cbConsent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myDb = new DatabaseHelper(getContext());

        getActivity().getTheme().applyStyle(R.style.AppTheme_NoActionBar_AccentColorGreen, true);

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_register_consent, container, false);

        cbConsent = (CheckBox) rootView.findViewById(R.id.cbConsent);

        /*
        profilePic = rootView.findViewById(R.id.profile_pic);
        nameTextView = rootView.findViewById(R.id.name);
        emailTextView = rootView.findViewById(R.id.email);
        passwordTextView = rootView.findViewById(R.id.password);
        confirmPasswordTextView = rootView.findViewById(R.id.confirm_password);
        */
        //profilePic.setOnClickListener(new View.OnClickListener(){ //TODO:request permission
            /*@Override
            public void onClick(View arg0) {
                pickImage();
            }});*/

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.contains("pref_key_personal_consent") && sharedPreferences.getString("pref_key_personal_consent", "") == "true") {
            cbConsent.setChecked(true);
        } else {
            cbConsent.setChecked(false);
        }
        cbConsent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cbConsent.isChecked()) {
                    myDb.insertConsent("true");
                    editor.putString("pref_key_personal_consent", "true");
                    editor.apply();
                } else {
                    editor.putString("pref_key_personal_consent", "false");
                    myDb.insertConsent("false");
                    editor.apply();
                }
            }
        });
        restorePreferences();
        setErrors();
        return rootView;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        String consent;

        if (cbConsent.isChecked()) {
            consent = "true";
        } else {
            consent = "false";
        }

        editor.putString("pref_key_personal_consent", consent);
        /*editor.putString("pref_key_personal_name", String.valueOf(nameTextView.getText()));
        editor.putString("pref_key_personal_email", String.valueOf(emailTextView.getText()));*/
        System.out.println("Consent stored in shared preferences: " + sharedPreferences.getString("pref_key_personal_consent", "false"));
        /*
        try {
            editor.putString("pref_key_personal_password", AESHelper.encrypt(CIPHER_KEY, String.valueOf(passwordTextView.getText())));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        if (cbConsent.isChecked()) {
            myDb.insertConsent("true");
        } else {
            myDb.insertConsent("false");
        }
        editor.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        /*if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }*/
    }

    private void setErrors(){

        /*nameTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (String.valueOf(nameTextView.getText()).equals(""))
                    nameTextView.setError(getString(R.string.error_invalid_name));
                else
                    nameTextView.setError(null);

            }
        });

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
        });*/

    }
    /*
    private void pickImage() {
        Crop.pickImage(getContext(), this, Crop.REQUEST_PICK);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(getContext(), this, Crop.REQUEST_CROP);
    }*/

    /*private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            imageUri = Crop.getOutput(result);
            profilePic.setImageURI(Crop.getOutput(result));
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                profilePic.setImageBitmap(Utility.getCroppedBitmap(bitmap));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/

    private void restorePreferences(){

        /*
        try {
            imageUri = Uri.parse(sharedPreferences.getString("pref_key_profile_picture", "android.resource://com.example.desent.desent/drawable/earth"));
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
            profilePic.setImageBitmap(Utility.getCroppedBitmap(bitmap));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        nameTextView.setText(sharedPreferences.getString("pref_key_personal_name", ""), TextView.BufferType.EDITABLE);
        emailTextView.setText(sharedPreferences.getString("pref_key_personal_email", ""), TextView.BufferType.EDITABLE);
        */
        String consent = sharedPreferences.getString("pref_key_personal_consent","");
        if (consent == "true"){
            cbConsent.setChecked(true);
        } else {
            cbConsent.setChecked(false);
        }
        //cbConsent.setChecked(sharedPreferences.getBoolean("pref_key_personal_consent", false));
        /*try {
            passwordTextView.setText(AESHelper.decrypt(CIPHER_KEY, sharedPreferences.getString("pref_key_personal_password", "")), TextView.BufferType.EDITABLE);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
    }
}
