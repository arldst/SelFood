package com.kom5a_tugasbesar.selfood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kom5a_tugasbesar.selfood.Activity.ui.RegisterOptionFragment;
import com.kom5a_tugasbesar.selfood.R;

public class LoginActivity extends AppCompatActivity {

    private TextView emailInput, passwordInput, registerText;
    private Button loginPelangganBtn, loginRestoBtn;
    private FrameLayout registerOptionFragment;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.loginInputEmail);
        passwordInput = findViewById(R.id.loginInputPassword);
        loginPelangganBtn = findViewById(R.id.loginButtonUser);
        loginRestoBtn = findViewById(R.id.loginButtonResto);
        registerText = findViewById(R.id.loginRegisterButton);
        registerOptionFragment = findViewById(R.id.loginRegisterOption);
        progressBar = findViewById(R.id.loginProgressBar);

        String registerTextDialog = registerText.getText().toString();
        SpannableString ss = new SpannableString(registerTextDialog);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                registerTextAction();
            }
        };

        ss.setSpan(clickableSpan, 18, 27, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        registerText.setText(ss);
        registerText.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void registerTextAction() {

        RegisterOptionFragment fragment = RegisterOptionFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_to_up, R.anim.slide_out_to_down, R.anim.slide_in_to_up, R.anim.slide_out_to_down);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.loginRegisterOption, fragment, "REGISTER_OPTION_FRAGMENT").commit();
    }
}