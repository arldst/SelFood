package com.kom5c_tugasbesar.selfood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kom5c_tugasbesar.selfood.Model.Restoran;
import com.kom5c_tugasbesar.selfood.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RestaurantRegistrationActivity extends AppCompatActivity {

    private EditText mEmail, mPassword, mConfPassword, mRestoName, mPhonenumber, mRestoCat;
    private TextView mLocation;
    private Button mRegBtn;
    private RelativeLayout mLocBtn;
    private ProgressBar mProgBar;

    FusedLocationProviderClient fusedLocationProviderClient;

    // Firebase
    private FirebaseAuth mAuth;
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";

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
        mProgBar = findViewById(R.id.regRestoProgresBar);

        mAuth = FirebaseAuth.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mLocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ActivityCompat.checkSelfPermission(RestaurantRegistrationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    mProgBar.setVisibility(View.VISIBLE);

                    fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();

                            if(location != null) {
                                Geocoder geocoder = new Geocoder(RestaurantRegistrationActivity.this, Locale.getDefault());
                                try {
                                    List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    String address_line = String.valueOf(Html.fromHtml(address.get(0).getAddressLine(0)));

                                    mProgBar.setVisibility(View.GONE);

                                    mLocation.setText(address_line);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
                else {
                    // Ask location access permission
                    ActivityCompat.requestPermissions(RestaurantRegistrationActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });

        mRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get data from input
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String confPassword = mConfPassword.getText().toString().trim();
                final String name = mRestoName.getText().toString().trim();
                final String phoneNumber = mPhonenumber.getText().toString().trim();
                final String category = mRestoCat.getText().toString().trim();
                final String restoAddress = mLocation.getText().toString().trim();

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

                if(restoAddress.equals("Alamat Restoran")) {
                    mLocation.setError("Harap masukan alamat restoran");
                    mLocation.requestFocus();
                    return;
                }

                mProgBar.setVisibility(View.VISIBLE);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {

                                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    Restoran restoran = new Restoran(id, email, name, phoneNumber, category, restoAddress, null, null);

                                    FirebaseDatabase database = FirebaseDatabase.getInstance(dbUrl);
                                    DatabaseReference userRef = database.getReference("restoran");

                                    userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(restoran).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()) {
                                                Toast.makeText(RestaurantRegistrationActivity.this, "Registrasi berhasil!", Toast.LENGTH_LONG).show();
                                                mProgBar.setVisibility(View.GONE);

                                                Intent intent = new Intent(RestaurantRegistrationActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                            else {
                                                Toast.makeText(RestaurantRegistrationActivity.this, "Registrasi gagal", Toast.LENGTH_LONG).show();
                                                mProgBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(RestaurantRegistrationActivity.this, "Registrasi gagal", Toast.LENGTH_LONG).show();
                                    mProgBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }
}