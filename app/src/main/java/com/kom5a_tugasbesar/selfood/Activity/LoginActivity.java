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
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kom5a_tugasbesar.selfood.Activity.ui.RegisterOptionFragment;
import com.kom5a_tugasbesar.selfood.R;

public class LoginActivity extends AppCompatActivity {

    private TextView emailInput, passwordInput, registerText;
    private Button loginBtn;
    private FrameLayout registerOptionFragment;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseAuth mAuth;
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.loginInputEmail);
        passwordInput = findViewById(R.id.loginInputPassword);
        loginBtn = findViewById(R.id.loginButton);
        registerText = findViewById(R.id.loginRegisterButton);
        registerOptionFragment = findViewById(R.id.loginRegisterOption);
        progressBar = findViewById(R.id.loginProgressBar);

        mAuth = FirebaseAuth.getInstance();

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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get data from input
                String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if(email.isEmpty()) {
                    emailInput.setError("Harap masukan email");
                    emailInput.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailInput.setError("Harap masukan email yang valid!");
                    emailInput.requestFocus();
                    return;
                }

                if(password.isEmpty()) {
                    passwordInput.setError("Password kosong");
                    passwordInput.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {

                                    FirebaseDatabase database = FirebaseDatabase.getInstance(dbUrl);
                                    DatabaseReference userRef = database.getReference("pelanggan");
                                    DatabaseReference restoRef = database.getReference("restoran");

                                    restoRef.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()) {
                                                progressBar.setVisibility(View.GONE);
                                                startActivity(new Intent(LoginActivity.this, RestoDashboardActivity.class));
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "Login gagal", Toast.LENGTH_LONG);
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
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