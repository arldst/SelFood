package com.kom5c_tugasbesar.selfood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.kom5c_tugasbesar.selfood.Adapter.MenuAdapter;
import com.kom5c_tugasbesar.selfood.Adapter.MenuOrderAdapter;
import com.kom5c_tugasbesar.selfood.Model.Menu;
import com.kom5c_tugasbesar.selfood.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class OrderActivity extends AppCompatActivity {

    private ImageView restoImg;
    private TextView restoName;
    private RecyclerView recyclerView;
    private FloatingActionButton orderBtn;

    // Firebase
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";
    private DatabaseReference restoRef;
    private DatabaseReference orderRef;
    private DatabaseReference userRef;

    private FirebaseRecyclerOptions<Menu> options;
    private FirebaseRecyclerAdapter<Menu, MenuOrderAdapter> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        final String resto_id = getIntent().getStringExtra("resto_id_order");
        final int table_number = getIntent().getIntExtra("table_number", 0);

        restoImg = findViewById(R.id.resto_img_order);
        restoName = findViewById(R.id.resto_name_order);
        recyclerView = findViewById(R.id.rv_menu_order);
        orderBtn = findViewById(R.id.fab_order);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        orderRef = FirebaseDatabase.getInstance(dbUrl).getReference("pesanan");
        restoRef = FirebaseDatabase.getInstance(dbUrl).getReference("restoran").child(resto_id);

        restoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Glide.with(OrderActivity.this)
                        .load(snapshot.child("imgUrl").getValue().toString())
                        .into(restoImg);

                restoName.setText(snapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        restoRef.child("menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    options = new FirebaseRecyclerOptions.Builder<Menu>()
                            .setQuery(restoRef.child("menu"), Menu.class).build();

                    adapter = new FirebaseRecyclerAdapter<Menu, MenuOrderAdapter>(options) {
                        @Override
                        protected void onBindViewHolder(final MenuOrderAdapter menuAdapter, final int i, final Menu menu) {

                            Glide.with(menuAdapter.itemView.getContext())
                                    .load(menu.getFoodImgUrl())
                                    .into(menuAdapter.foodPic);

                            menuAdapter.foodName.setText(menu.getName());
                            menuAdapter.foodDesc.setText(menu.getDescription());
                            NumberFormat format = new DecimalFormat("###,###");
                            int fp = menu.getPrice();
                            String food_price = format.format(fp);
                            menuAdapter.foodPrice.setText("Rp. " + food_price);

                            menuAdapter.add.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(Integer.parseInt(menuAdapter.itemCount.getText().toString()) >= 0) {
                                        String newItemCount = String.valueOf(Integer.parseInt(menuAdapter.itemCount.getText().toString()) + 1);
                                        menuAdapter.itemCount.setText(newItemCount);

                                        orderRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foods").child(String.valueOf(i+1)).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if(!snapshot.exists()) {

                                                    Menu food = new Menu(menu.getName(), menu.getDescription(), menu.getFoodImgUrl(), menu.getPrice());
                                                    food.setItemCount(Integer.parseInt(menuAdapter.itemCount.getText().toString()));
                                                    snapshot.getRef().setValue(food);
                                                    orderRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child("total_price").setValue(ServerValue.increment(food.getPrice()));
                                                }
                                                else {
                                                    snapshot.getRef().child("itemCount").setValue(Integer.parseInt(menuAdapter.itemCount.getText().toString().trim()));
                                                    orderRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                            .child("total_price").setValue(ServerValue.increment(menu.getPrice()));
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            });

                            menuAdapter.remove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(Integer.parseInt(menuAdapter.itemCount.getText().toString()) > 0) {
                                        String newItemCount = String.valueOf(Integer.parseInt(menuAdapter.itemCount.getText().toString()) - 1);
                                        menuAdapter.itemCount.setText(newItemCount);

                                        orderRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("foods").child(String.valueOf(i+1))
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                snapshot.getRef().child("itemCount").setValue(Integer.parseInt(menuAdapter.itemCount.getText().toString().trim()));
                                                if(menuAdapter.itemCount.getText().toString().equals("0")) {
                                                    snapshot.getRef().removeValue();
                                                }
                                                orderRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .child("total_price").setValue(ServerValue.increment(-menu.getPrice()));
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public MenuOrderAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menus_order, parent, false);

                            return new MenuOrderAdapter(v);
                        }
                    };

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                orderRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("foods").exists()) {
                            Intent proceedIntent = new Intent(OrderActivity.this, ProceedOrderActivity.class);
                            proceedIntent.putExtra("restoranid", resto_id);
                            proceedIntent.putExtra("restotable", table_number);
                            startActivity(proceedIntent);
                        }
                        else {
                            Toast.makeText(OrderActivity.this, "Kamu belum memilih makanan", Toast.LENGTH_LONG).show();
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
    public void onBackPressed() {
        super.onBackPressed();

        final String resto_id = getIntent().getStringExtra("resto_id_order");
        final int table_number = getIntent().getIntExtra("table_number", 0);

        orderRef = FirebaseDatabase.getInstance(dbUrl).getReference("pesanan");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userRef = FirebaseDatabase.getInstance(dbUrl).getReference("pelanggan");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("status").setValue(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference restoRef = FirebaseDatabase.getInstance(dbUrl).getReference("restoran");
        restoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshot.getRef().child(resto_id).child("table").child(String.valueOf(table_number)).child("status").setValue("Tersedia");
                snapshot.getRef().child(resto_id).child("table").child(String.valueOf(table_number)).child("pelanggan_id").setValue("Available");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}