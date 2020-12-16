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
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kom5c_tugasbesar.selfood.Adapter.MenuConfirmAdapter;
import com.kom5c_tugasbesar.selfood.Model.Menu;
import com.kom5c_tugasbesar.selfood.Model.Pesanan;
import com.kom5c_tugasbesar.selfood.Model.Restoran;
import com.kom5c_tugasbesar.selfood.Model.Table;
import com.kom5c_tugasbesar.selfood.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class RestaurantTableDetailActivity extends AppCompatActivity {

    private TextView userName, totalPriceText, orderStatus;
    private Button changeStatusBtn;
    private RecyclerView recyclerView;

    // Firebase
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";
    private DatabaseReference orderRef;
    private DatabaseReference restoRef;

    private FirebaseRecyclerOptions<Menu> options;
    private FirebaseRecyclerAdapter<Menu, MenuConfirmAdapter> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_table_detail);

        final int tableNumber = getIntent().getIntExtra("table_number", 0);

        userName = findViewById(R.id.resto_table_detail_name);
        totalPriceText = findViewById(R.id.total_price_table_detail);
        orderStatus = findViewById(R.id.table_status_text);
        changeStatusBtn = findViewById(R.id.resto_change_status_button);
        recyclerView = findViewById(R.id.rv_table_detail);
        recyclerView.setHasFixedSize(true);

        restoRef = FirebaseDatabase.getInstance(dbUrl).getReference("restoran").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        restoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Table table = snapshot.child("table").child(String.valueOf(tableNumber)).getValue(Table.class);

                orderRef = FirebaseDatabase.getInstance(dbUrl).getReference("pesanan").child(table.getPelanggan_id());
                orderRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.exists()) {

                            userName.setText(snapshot.child("name").getValue().toString());

                            if(snapshot.child("total_price").getValue() != null) {
                                NumberFormat format = new DecimalFormat("###,###");
                                String food_price_total = format.format(snapshot.child("total_price").getValue());
                                totalPriceText.setText("Total Harga : Rp. " + food_price_total);
                            }
                            orderStatus.setText(snapshot.child("status").getValue().toString());

                            options = new FirebaseRecyclerOptions.Builder<Menu>()
                                    .setQuery(orderRef.child("foods"), Menu.class).build();

                            adapter = new FirebaseRecyclerAdapter<Menu, MenuConfirmAdapter>(options) {
                                @Override
                                protected void onBindViewHolder(final MenuConfirmAdapter menuAdapter, final int i, final Menu menu) {

                                    Glide.with(menuAdapter.itemView.getContext())
                                            .load(menu.getFoodImgUrl())
                                            .into(menuAdapter.foodPic);

                                    menuAdapter.foodName.setText(menu.getName());
                                    menuAdapter.foodDescription.setText(menu.getDescription());
                                    NumberFormat format = new DecimalFormat("###,###");
                                    final int fp = menu.getPrice();
                                    String food_price = format.format(fp);
                                    menuAdapter.foodPrice.setText("Rp. " + food_price);
                                    menuAdapter.foodCount.setText("x" + String.valueOf(menu.getItemCount()));
                                }

                                @NonNull
                                @Override
                                public MenuConfirmAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menus_detail, parent, false);

                                    return new MenuConfirmAdapter(v);
                                }
                            };

                            if(snapshot.child("status").getValue().toString().equals("Sedang Memesan")) {
                                changeStatusBtn.setText("Konfirmasi");
                                changeStatusBtn.setBackgroundResource(R.color.colorGrey);
                                changeStatusBtn.setEnabled(false);
                            }

                            if(snapshot.child("status").getValue().toString().equals("Menunggu Konfirmasi")) {
                                changeStatusBtn.setText("Konfirmasi");
                                changeStatusBtn.setBackgroundResource(R.color.colorRed);
                                changeStatusBtn.setEnabled(true);
                            }

                            if(snapshot.child("status").getValue().toString().equals("Pesanan Diproses")) {
                                changeStatusBtn.setText("Antar Pesanan");
                                changeStatusBtn.setBackgroundResource(R.color.colorRed);
                                changeStatusBtn.setEnabled(true);
                            }

                            if(snapshot.child("status").getValue().toString().equals("Mengantar Pesanan")) {
                                changeStatusBtn.setText("Selesai Antar");
                                changeStatusBtn.setBackgroundResource(R.color.colorRed);
                                changeStatusBtn.setEnabled(true);
                            }

                            if(snapshot.child("status").getValue().toString().equals("Makan")) {
                                changeStatusBtn.setText("Selesai Pembayaran");
                                changeStatusBtn.setBackgroundResource(R.color.colorGrey);
                                changeStatusBtn.setEnabled(false);
                            }

                            if(snapshot.child("status").getValue().toString().equals("Request Pembayaran")) {
                                changeStatusBtn.setText("Selesai Pembayaran");
                                changeStatusBtn.setBackgroundResource(R.color.colorRed);
                                changeStatusBtn.setEnabled(true);
                            }

                            if(snapshot.child("status").getValue().toString().equals("Pembayaran Selesai")) {
                                changeStatusBtn.setText("Selesai");
                                changeStatusBtn.setBackgroundResource(R.color.colorGrey);
                                changeStatusBtn.setEnabled(false);
                            }

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RestaurantTableDetailActivity.this);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            adapter.startListening();
                            recyclerView.setAdapter(adapter);
                        }
                        else {
                            startActivity(new Intent(RestaurantTableDetailActivity.this, RestaurantActivity.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        changeStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changeStatusBtn.getText().toString().equals("Konfirmasi")) {
                    orderRef.child("status").setValue("Pesanan Diproses");
                    restoRef.child("table").child(String.valueOf(tableNumber)).child("status").setValue("Pesanan Diproses");
                }
                if(changeStatusBtn.getText().toString().equals("Antar Pesanan")) {
                    orderRef.child("status").setValue("Mengantar Pesanan");
                    restoRef.child("table").child(String.valueOf(tableNumber)).child("status").setValue("Mengantar Pesanan");
                }
                if(changeStatusBtn.getText().toString().equals("Selesai Antar")) {
                    orderRef.child("status").setValue("Makan");
                    restoRef.child("table").child(String.valueOf(tableNumber)).child("status").setValue("Makan");
                }
                if(changeStatusBtn.getText().toString().equals("Selesai Pembayaran")) {
                    orderRef.child("status").setValue("Pembayaran Selesai");
                    restoRef.child("table").child(String.valueOf(tableNumber)).child("status").setValue("Pembayaran Selesai");
                }
            }
        });
    }
}