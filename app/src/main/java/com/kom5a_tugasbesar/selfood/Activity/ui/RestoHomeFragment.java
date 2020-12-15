package com.kom5a_tugasbesar.selfood.Activity.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kom5a_tugasbesar.selfood.Activity.AddMenuActivity;
import com.kom5a_tugasbesar.selfood.Adapter.MenuAdapter;
import com.kom5a_tugasbesar.selfood.Model.Menu;
import com.kom5a_tugasbesar.selfood.Model.Restoran;
import com.kom5a_tugasbesar.selfood.Model.Table;
import com.kom5a_tugasbesar.selfood.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import static android.app.Activity.RESULT_OK;

public class RestoHomeFragment extends Fragment {

    private ImageView mRestoImg;
    private TextView mRestoName, noMenuPlaceholder;
    private Button addImgBtn;
    private RecyclerView recyclerView;
    private FloatingActionButton addMenuBtn;
    private ProgressBar mProgBar;

    private Uri filePathUri;

    // Firebase
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";
    private DatabaseReference restoRef;
    private StorageReference imgStorageRef;

    private FirebaseRecyclerOptions<Menu> options;
    private FirebaseRecyclerAdapter<Menu, MenuAdapter> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resto_home, container, false);

        mRestoImg = view.findViewById(R.id.resto_img_home);
        mRestoName = view.findViewById(R.id.resto_name_home);
        addImgBtn = view.findViewById(R.id.upload_img_resto_btn);
        recyclerView = view.findViewById(R.id.rv_menu_resto);
        addMenuBtn = view.findViewById(R.id.add_menu_button);
        mProgBar = view.findViewById(R.id.resto_home_progressbar);
        noMenuPlaceholder = view.findViewById(R.id.no_menu_placeholder);

        recyclerView.setHasFixedSize(true);

        imgStorageRef = FirebaseStorage.getInstance().getReference("resto_img");
        restoRef = FirebaseDatabase.getInstance(dbUrl).getReference("restoran").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        restoRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mRestoName.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        restoRef.child("menu").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    recyclerView.setVisibility(View.VISIBLE);

                    options = new FirebaseRecyclerOptions.Builder<Menu>()
                            .setQuery(restoRef.child("menu"), Menu.class).build();

                    adapter = new FirebaseRecyclerAdapter<Menu, MenuAdapter>(options) {
                        @Override
                        protected void onBindViewHolder(MenuAdapter menuAdapter, final int i, Menu menu) {

                            Glide.with(menuAdapter.itemView.getContext())
                                    .load(menu.getFoodImgUrl())
                                    .apply(new RequestOptions().override(100, 100))
                                    .into(menuAdapter.foodPic);

                            menuAdapter.foodName.setText(menu.getName());
                            menuAdapter.foodDescription.setText(menu.getDescription());
                            NumberFormat format = new DecimalFormat("###,###");
                            int fp = menu.getPrice();
                            String food_price = format.format(fp);
                            menuAdapter.foodPrice.setText("Rp. " + food_price);

                            menuAdapter.deleteBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    snapshot.getRef().child(String.valueOf(i+1)).removeValue();
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public MenuAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menus_resto, parent, false);

                            return new MenuAdapter(v);
                        }
                    };

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);
                }
                else {
                    noMenuPlaceholder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        restoRef.child("imgUrl").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    Glide.with(getActivity())
                            .load(dataSnapshot.getValue())
                            .apply(new RequestOptions().override(360, 140))
                            .into(mRestoImg);
                }
                else {
                    mRestoImg.setImageResource(R.drawable.restaurant_placeholder);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browseImg = new Intent();
                browseImg.setType("image/*");
                browseImg.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(browseImg, "Pilih Gambar"), 7);

            }
        });

        addMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddMenuActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 7 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePathUri = data.getData();

            try {

                uploadImage();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePathUri);
                mRestoImg.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage() {

        if(filePathUri != null) {
            mProgBar.setVisibility(View.VISIBLE);
            final StorageReference storageReference = imgStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(filePathUri));
            storageReference.putFile(filePathUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final Uri downloadUrl = uri;
                            String sUrl = downloadUrl.toString();

                            restoRef.child("imgUrl").setValue(sUrl);
                            mProgBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Gambar berhasil diupload", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }
}