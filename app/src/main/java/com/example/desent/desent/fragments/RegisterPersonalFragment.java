package com.example.desent.desent.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.desent.desent.R;
import com.example.desent.desent.models.DatabaseHelper;
import com.example.desent.desent.utils.Utility;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static com.example.desent.desent.R.color.colorPrimary;

/**
 * Created by celine on 06/07/17.
 */

public class RegisterPersonalFragment extends Fragment {

    //Profile picure
    private ImageView profilePic;
    private ImageView cameraIcon;
    private TextView textViewChangePicture;

    //Personal information
    private EditText editTextName;
    private EditText editTextAddress;
    private EditText editTextZipCode;
    private EditText editTextCity;
    private EditText editTextWeight;

    private RadioGroup radioGroupGender;
    private RadioButton radioButtonFemale;
    private RadioButton radioButtonMale;

    //Datepicker for birthdate
    private DatePicker datePicker;

    private SharedPreferences sharedPreferences;
    private Uri imageUri;
    private DatabaseHelper myDb;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myDb = new DatabaseHelper(getContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        getActivity().getTheme().applyStyle(R.style.AppTheme_NoActionBar_AccentColorGreen, true);

        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_register_personal, container, false);

        profilePic = rootView.findViewById(R.id.profile_pic);
        cameraIcon = rootView.findViewById(R.id.imageButton);
        cameraIcon.setImageResource(R.drawable.ic_menu_camera);
        textViewChangePicture = rootView.findViewById(R.id.textViewChangePicture);
        //could maybe gather these on a group instead?
        profilePic.setOnClickListener(new View.OnClickListener(){ //TODO:request permission
            @Override
            public void onClick(View arg0) {
                pickImage();
            }});
        cameraIcon.setOnClickListener(new View.OnClickListener(){ //TODO:request permission
            @Override
            public void onClick(View arg0) {
                pickImage();
            }});
        textViewChangePicture.setOnClickListener(new View.OnClickListener(){ //TODO:request permission
            @Override
            public void onClick(View arg0) {
                pickImage();
            }});

        editTextName = rootView.findViewById(R.id.nameInput);
        editTextAddress = rootView.findViewById(R.id.addressInput);
        editTextZipCode = rootView.findViewById(R.id.zipcodeInput);
        editTextCity = rootView.findViewById(R.id.cityInput);
        editTextWeight = rootView.findViewById(R.id.weightInput);

        radioGroupGender = (RadioGroup) rootView.findViewById(R.id.radioGroup);
        radioGroupGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) radioGroupGender.findViewById(checkedId);
                int checkedIndex = radioGroupGender.indexOfChild(checkedRadioButton);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("pref_key_personal_gender", checkedIndex);
            }
        });
        /*
        radioButtonFemale = rootView.findViewById(R.id.radio_button_female);
        radioButtonMale = rootView.findViewById(R.id.radio_button_male);
        */
        datePicker = rootView.findViewById(R.id.datePicker);
        //set max birth date to today's date
        datePicker.setMaxDate(new Date().getTime());

        restorePreferences();

        //setErrors();

        return rootView;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("pref_key_profile_picture", imageUri.toString());
        editor.putString("pref_key_personal_name", String.valueOf(editTextName.getText()));
        editor.putString("pref_key_personal_address", String.valueOf(editTextAddress.getText()));
        editor.putString("pref_key_personal_zip_code", String.valueOf(editTextZipCode.getText()));
        editor.putString("pref_key_personal_city", String.valueOf(editTextCity.getText()));
        editor.putString("pref_key_personal_weight", String.valueOf(editTextWeight.getText()));
        //editor.putBoolean("pref_key_personal_male", radioButtonMale.isChecked());
        //editor.putBoolean("pref_key_personal_female", radioButtonFemale.isChecked());
        //editor.putInt("pref_key_personal_gender", radioGroupGender.)
        editor.putInt("pref_key_personal_birth_month", datePicker.getMonth());
        editor.putInt("pref_key_personal_birth_year", datePicker.getYear());
        editor.putInt("pref_key_personal_birth_day", datePicker.getDayOfMonth());
        editor.commit();

        myDb.insertPersonalData(editTextName.getText().toString(), editTextAddress.getText().toString(),
                editTextZipCode.getText().toString(), editTextCity.getText().toString(),
                editTextWeight.getText().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    /*
    private void setErrors(){

        editTextName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (String.valueOf(editTextName.getText()).equals(""))
                    editTextName.setError(getString(R.string.error_invalid_name));
                else
                    editTextName.setError(null);

            }
        });

        /*emailTextView.addTextChangedListener(new TextWatcher() {

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

    //}

    private void pickImage() {
        Crop.pickImage(getContext(), this, Crop.REQUEST_PICK);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(getContext(), this, Crop.REQUEST_CROP);
    }

    private void handleCrop(int resultCode, Intent result) {
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
    }

    private void restorePreferences(){

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

        editTextName.setText(sharedPreferences.getString("pref_key_personal_name", ""), TextView.BufferType.EDITABLE);
        editTextAddress.setText(sharedPreferences.getString("pref_key_personal_address", ""), TextView.BufferType.EDITABLE);
        editTextZipCode.setText(sharedPreferences.getString("pref_key_personal_zip_code", ""), TextView.BufferType.EDITABLE);
        editTextCity.setText(sharedPreferences.getString("pref_key_personal_city", ""), TextView.BufferType.EDITABLE);
        editTextWeight.setText(sharedPreferences.getString("pref_key_personal_weight", ""), TextView.BufferType.EDITABLE);
    }
}
