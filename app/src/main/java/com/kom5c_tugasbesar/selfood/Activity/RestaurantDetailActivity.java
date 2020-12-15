package com.kom5c_tugasbesar.selfood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kom5c_tugasbesar.selfood.Adapter.MenuAdapter;
import com.kom5c_tugasbesar.selfood.Adapter.MenuViewAdapter;
import com.kom5c_tugasbesar.selfood.Model.Menu;
import com.kom5c_tugasbesar.selfood.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RestaurantDetailActivity extends AppCompatActivity {

    private ImageView restoImg;
    private TextView restoName, restoType, restoAddress;
    private Button contactBtn;
    private RecyclerView recyclerView;

    private Uri filePathUri;

    // Firebase
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";
    private DatabaseReference restoRef;

    private FirebaseRecyclerOptions<Menu> options;
    private FirebaseRecyclerAdapter<Menu, MenuViewAdapter> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        final String resto_id = getIntent().getStringExtra("resto_id");

        restoImg = findViewById(R.id.resto_img_view_menu);
        restoName = findViewById(R.id.resto_name_view_menu);
        restoType = findViewById(R.id.resto_type_menu_view);
        restoAddress = findViewById(R.id.resto_address_menu_view);
        contactBtn = findViewById(R.id.resto_contact_button);
        recyclerView = findViewById(R.id.rv_menu_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        restoRef = FirebaseDatabase.getInstance(dbUrl).getReference("restoran").child(resto_id);

        restoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Glide.with(RestaurantDetailActivity.this)
                        .load(snapshot.child("imgUrl").getValue().toString())
                        .into(restoImg);

                restoName.setText(snapshot.child("name").getValue().toString());
                restoType.setText(snapshot.child("type").getValue().toString());
                restoAddress.setText(snapshot.child("address").getValue().toString());

                options = new FirebaseRecyclerOptions.Builder<Menu>()
                        .setQuery(restoRef.child("menu"), Menu.class).build();

                adapter = new FirebaseRecyclerAdapter<Menu, MenuViewAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(MenuViewAdapter menuAdapter, final int i, Menu menu) {

                        Glide.with(menuAdapter.itemView.getContext())
                                .load(menu.getFoodImgUrl())
                                .into(menuAdapter.foodPic);

                        menuAdapter.foodName.setText(menu.getName());
                        menuAdapter.foodDescription.setText(menu.getDescription());
                        NumberFormat format = new DecimalFormat("###,###");
                        int fp = menu.getPrice();
                        String food_price = format.format(fp);
                        menuAdapter.foodPrice.setText("Rp. " + food_price);
                    }

                    @NonNull
                    @Override
                    public MenuViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menus_view, parent, false);

                        return new MenuViewAdapter(v);
                    }
                };

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RestaurantDetailActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                restoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                        dialIntent.setData(Uri.parse("tel:" + snapshot.child("phoneNumber").getValue()));
                        startActivity(dialIntent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }
}