package com.kom5c_tugasbesar.selfood.Activity.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.kom5c_tugasbesar.selfood.Activity.ProceedOrderActivity;
import com.kom5c_tugasbesar.selfood.Activity.UserActivity;
import com.kom5c_tugasbesar.selfood.Adapter.MenuConfirmAdapter;
import com.kom5c_tugasbesar.selfood.Model.Menu;
import com.kom5c_tugasbesar.selfood.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class UserOrderFragment extends Fragment {

    private TextView restoName, orderStatus, totalPriceText;
    private ImageView restoImg;
    private Button changeStatusBtn;
    private RecyclerView recyclerView;
    private FrameLayout nv;

    // Firebase
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";
    private DatabaseReference orderRef;
    private DatabaseReference restoRef;

    private FirebaseRecyclerOptions<Menu> options;
    private FirebaseRecyclerAdapter<Menu, MenuConfirmAdapter> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_order, container, false);

        nv = view.findViewById(R.id.no_order_user);
        restoName = view.findViewById(R.id.user_order_detail_name);
        orderStatus = view.findViewById(R.id.user_status_text);
        totalPriceText = view.findViewById(R.id.total_price_detail);
        restoImg = view.findViewById(R.id.user_order_detail_pic);
        changeStatusBtn = view.findViewById(R.id.user_change_status_button);
        recyclerView = view.findViewById(R.id.rv_user_order_detail);
        recyclerView.setHasFixedSize(true);

        orderRef = FirebaseDatabase.getInstance(dbUrl).getReference("pesanan")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    orderRef.child("restoId").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String resto_id = snapshot.getValue().toString();
                            FirebaseDatabase.getInstance(dbUrl).getReference("restoran").child(resto_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if(getActivity() == null) {
                                        return;
                                    }

                                    if(snapshot.exists()) {
                                        Glide.with(getActivity())
                                                .load(snapshot.child("imgUrl").getValue())
                                                .into(restoImg);

                                        restoName.setText(snapshot.child("name").getValue().toString());
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

                    orderStatus.setText(snapshot.child("status").getValue().toString().trim());

                    // Get Button Status
                    if(snapshot.child("status").getValue().toString().equals("Menunggu Konfirmasi") || snapshot.child("status").getValue().toString().equals("Pesanan Diproses")
                            || snapshot.child("status").getValue().toString().equals("Mengantar Makanan")) {
                        changeStatusBtn.setText("Bayar");
                        changeStatusBtn.setBackgroundResource(R.color.colorGrey);
                        changeStatusBtn.setEnabled(false);
                    }
                    if(snapshot.child("status").getValue().toString().equals("Makan")) {
                        changeStatusBtn.setText("Bayar");
                        changeStatusBtn.setBackgroundResource(R.color.colorRed);
                        changeStatusBtn.setEnabled(true);
                    }
                    if(snapshot.child("status").getValue().toString().equals("Request Pembayaran")) {
                        changeStatusBtn.setText("Selesai");
                        changeStatusBtn.setBackgroundResource(R.color.colorGrey);
                        changeStatusBtn.setEnabled(false);
                    }
                    if(snapshot.child("status").getValue().toString().equals("Pembayaran Selesai")) {
                        changeStatusBtn.setText("Selesai");
                        changeStatusBtn.setBackgroundResource(R.color.colorRed);
                        changeStatusBtn.setEnabled(true);
                    }

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

                    if(snapshot.child("total_price").getValue() != null) {
                        NumberFormat format = new DecimalFormat("###,###");
                        String food_price_total = format.format(snapshot.child("total_price").getValue());
                        totalPriceText.setText("Total Harga : Rp. " + food_price_total);
                    }

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);
                }
                else {
                    nv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        changeStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(changeStatusBtn.getText().toString().equals("Bayar")) {
                    orderRef.child("status").setValue("Request Pembayaran");
                    orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String resto_id = snapshot.child("restoId").getValue().toString();
                            FirebaseDatabase.getInstance(dbUrl).getReference("restoran")
                                    .child(resto_id).child("table").child(snapshot.child("tableNum").getValue().toString())
                                    .child("status").setValue("Request Pembayaran");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                if(changeStatusBtn.getText().toString().equals("Selesai")) {
                    orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            startActivity(new Intent(getActivity(), UserActivity.class));
                            String resto_id = snapshot.child("restoId").getValue().toString();
                            FirebaseDatabase.getInstance(dbUrl).getReference("restoran")
                                    .child(resto_id).child("table").child(snapshot.child("tableNum").getValue().toString())
                                    .child("pelanggan_id").setValue("Available");
                            FirebaseDatabase.getInstance(dbUrl).getReference("restoran")
                                    .child(resto_id).child("table").child(snapshot.child("tableNum").getValue().toString())
                                    .child("status").setValue("Tersedia");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    orderRef.getRef().removeValue();
                    FirebaseDatabase.getInstance(dbUrl).getReference("pelanggan")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("status").setValue(0);
                }
            }
        });

        return view;
    }
}