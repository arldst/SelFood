package com.kom5a_tugasbesar.selfood.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kom5a_tugasbesar.selfood.Model.Menu;
import com.kom5a_tugasbesar.selfood.R;

import java.io.IOException;

public class AddMenuActivity extends AppCompatActivity {

    private EditText mName, mDescription, mPrice;
    private ImageView mPic;
    private Button submitBtn;
    private ProgressBar mProgBar;

    private Uri filePathUri;

    // Firebase
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";
    private DatabaseReference menuRef;
    private StorageReference imgRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        mName = findViewById(R.id.food_name_input);
        mDescription = findViewById(R.id.food_description_input);
        mPrice = findViewById(R.id.food_price_input);
        mPic = findViewById(R.id.food_pic_input);
        submitBtn = findViewById(R.id.add_menu_submit_button);
        mProgBar = findViewById(R.id.add_menu_progressbar);

        imgRef = FirebaseStorage.getInstance().getReference("menu_img");
        menuRef = FirebaseDatabase.getInstance(dbUrl).getReference("restoran").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("menu");

        mPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browseImg = new Intent();
                browseImg.setType("image/*");
                browseImg.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(browseImg, "Pilih Gambar"), 7);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get data from input
                final String name = mName.getText().toString().trim();
                final String description = mDescription.getText().toString().trim();
                final int price = Integer.parseInt(mPrice.getText().toString().trim());

                if(name.isEmpty()) {
                    mName.setError("Harap masukan nama makanan");
                    mName.requestFocus();
                    return;
                }

                if(description.isEmpty()) {
                    mDescription.setError("Harap masukan deskripsi makanan");
                    mDescription.requestFocus();
                    return;
                }

                if(mPrice.getText().toString().isEmpty()) {
                    mPrice.setError("Harap masukan harga");
                    mPrice.requestFocus();
                    return;
                }

                mProgBar.setVisibility(View.VISIBLE);
                menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            final long count = snapshot.getChildrenCount() + 1;

                            if(filePathUri != null) {

                                final StorageReference storageReference = imgRef.child(System.currentTimeMillis() + "." + getFileExtension(filePathUri));
                                storageReference.putFile(filePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                final Uri downloadUrl = uri;
                                                String sUrl = downloadUrl.toString();

                                                Menu newMenu = new Menu(name, description, sUrl, price);

                                                menuRef.child(String.valueOf(count)).setValue(newMenu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()) {
                                                            mProgBar.setVisibility(View.GONE);
                                                            Toast.makeText(AddMenuActivity.this, "Berhasil menambah menu", Toast.LENGTH_LONG).show();
                                                            startActivity(new Intent(AddMenuActivity.this, RestaurantActivity.class));
                                                        }
                                                        else {
                                                            mProgBar.setVisibility(View.GONE);
                                                            Toast.makeText(AddMenuActivity.this, "Gagal menambah menu", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }
                        else {

                            if(filePathUri != null) {

                                final StorageReference storageReference = imgRef.child(System.currentTimeMillis() + "." + getFileExtension(filePathUri));
                                storageReference.putFile(filePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                final Uri downloadUrl = uri;
                                                String sUrl = downloadUrl.toString();

                                                Menu newMenu = new Menu(name, description, sUrl, price);
                                                menuRef.child("1").setValue(newMenu).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()) {
                                                            mProgBar.setVisibility(View.GONE);
                                                            Toast.makeText(AddMenuActivity.this, "Berhasil menambah menu", Toast.LENGTH_LONG).show();
                                                            startActivity(new Intent(AddMenuActivity.this, RestaurantActivity.class));
                                                        }
                                                        else {
                                                            mProgBar.setVisibility(View.GONE);
                                                            Toast.makeText(AddMenuActivity.this, "Gagal menambah menu", Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 7 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathUri);
                mPic.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}