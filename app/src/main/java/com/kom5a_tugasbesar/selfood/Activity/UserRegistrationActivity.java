package com.kom5a_tugasbesar.selfood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kom5a_tugasbesar.selfood.Model.Pelanggan;
import com.kom5a_tugasbesar.selfood.R;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String[] restoCategory = new String[]{"Barat", "Cepat Saji", "Chinese", "Jepang", "Korea", "Seafood", "Timur Tengah", "Lainnnya"};

    // Layout components
    private EditText mEmail, mPassword, mConfPassword, mFullname, mPhonenumber;
    private Button mRegButton;
    private ProgressBar mProgBar;

    // Firebase
    private FirebaseAuth mAuth;
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        mEmail = findViewById(R.id.regInputEmail);
        mPassword = findViewById(R.id.regInputPassword);
        mConfPassword = findViewById(R.id.regInputConfPassword);
        mFullname = findViewById(R.id.regInputFullname);
        mPhonenumber = findViewById(R.id.regInputPhoneNumber);
        mRegButton = findViewById(R.id.regButtonUser);
        mProgBar = findViewById(R.id.regUserProgresBar);

        mAuth = FirebaseAuth.getInstance();

        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                // Get data from input
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confPassword = mConfPassword.getText().toString().trim();
                final String fullname = mFullname.getText().toString().trim();
                final String phonenumber = mPhonenumber.getText().toString().trim();

                if(email.isEmpty()) {
                    mEmail.setError("Harap masukan email");
                    mEmail.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Harap masukan email yang valid");
                    mEmail.requestFocus();
                    return;
                }

                if(password.length() < 8) {
                    mPassword.setError("Password minimal 8 karakter");
                    mPassword.requestFocus();
                    return;
                }

                if(!confPassword.equals(password)) {
                    mConfPassword.setError("Konfirmasi password salah");
                    mConfPassword.requestFocus();
                    return;
                }

                if(fullname.isEmpty()) {
                    mFullname.setError("Harap masukan nama lengkap");
                    mFullname.requestFocus();
                    return;
                }

                if(phonenumber.isEmpty()) {
                    mPhonenumber.setError("Harap masukan nomor telepon");
                    mPhonenumber.requestFocus();
                    return;
                }

                if(!Patterns.PHONE.matcher(phonenumber).matches()) {
                    mPhonenumber.setError("Harap masukan nomor yang valid");
                    mPhonenumber.requestFocus();
                    return;
                }

                mProgBar.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    Pelanggan user = new Pelanggan(email, fullname, phonenumber, 0);

                                    FirebaseDatabase database = FirebaseDatabase.getInstance(dbUrl);
                                    DatabaseReference userRef = database.getReference("pelanggan");

                                    userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()) {
                                                Toast.makeText(UserRegistrationActivity.this, "Registrasi berhasil!", Toast.LENGTH_LONG).show();
                                                mProgBar.setVisibility(View.GONE);

                                                Intent intent = new Intent(UserRegistrationActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                Toast.makeText(UserRegistrationActivity.this, "Registrasi gagal", Toast.LENGTH_LONG).show();
                                                mProgBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }

                                else {
                                    Toast.makeText(UserRegistrationActivity.this, "Registrasi gagal", Toast.LENGTH_LONG).show();
                                    mProgBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }
}