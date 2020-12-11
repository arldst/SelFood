package com.kom5a_tugasbesar.selfood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kom5a_tugasbesar.selfood.R;

public class LoginActivity extends AppCompatActivity {

    public TextView emailInput, passwordInput, registerText;
    public Button loginPelangganBtn, loginRestoBtn;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.loginInputEmail);
        passwordInput = findViewById(R.id.loginInputPassword);
        loginPelangganBtn = findViewById(R.id.loginButtonUser);
        loginRestoBtn = findViewById(R.id.loginButtonResto);
        registerText = findViewById(R.id.loginRegisterButton);
        progressBar = findViewById(R.id.loginProgressBar);

    }
}