package com.kom5a_tugasbesar.selfood.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kom5a_tugasbesar.selfood.R;

public class RestaurantRegistrationActivity extends AppCompatActivity {

    private EditText mEmail, mPassword, mConfPassword, mRestoName, mPhonenumber, mRestoCat;
    private TextView mLocation;
    private Button mRegBtn;
    private RelativeLayout mLocBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_registration);

        mEmail = findViewById(R.id.regInputEmailResto);
        mPassword = findViewById(R.id.regInputPasswordResto);
        mConfPassword = findViewById(R.id.regInputConfPasswordResto);
        mRestoName = findViewById(R.id.regInputRestoName);
        mPhonenumber = findViewById(R.id.regInputPhoneNumberResto);
        mRestoCat = findViewById(R.id.regRestoCategory);
        mLocation = findViewById(R.id.regLocText);
        mLocBtn = findViewById(R.id.regRestoLocBtn);
        mRegBtn = findViewById(R.id.regButtonResto);

        mLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get data from input
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confPassword = mConfPassword.getText().toString().trim();
                String name = mRestoName.getText().toString().trim();
                String phoneNumber = mPhonenumber.getText().toString().trim();
                String category = mRestoCat.getText().toString().trim();

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

                if(name.isEmpty()) {
                    mRestoName.setError("Harap masukan nama lengkap");
                    mRestoName.requestFocus();
                    return;
                }

                if(phoneNumber.isEmpty()) {
                    mPhonenumber.setError("Harap masukan nomor telepon");
                    mPhonenumber.requestFocus();
                    return;
                }

                if(!Patterns.PHONE.matcher(phoneNumber).matches()) {
                    mPhonenumber.setError("Harap masukan nomor yang valid");
                    mPhonenumber.requestFocus();
                    return;
                }

                if(category.isEmpty()) {
                    mRestoCat.setError("Harap masukan jenis resto");
                    mRestoCat.requestFocus();
                    return;
                }
            }
        });
    }
}