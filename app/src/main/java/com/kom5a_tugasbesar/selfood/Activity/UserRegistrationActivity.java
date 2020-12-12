package com.kom5a_tugasbesar.selfood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.kom5a_tugasbesar.selfood.R;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String[] restoCategory = new String[]{"Barat", "Cepat Saji", "Chinese", "Jepang", "Korea", "Seafood", "Timur Tengah", "Lainnnya"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

    }
}