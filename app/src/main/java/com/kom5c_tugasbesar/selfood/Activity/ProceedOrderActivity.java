package com.kom5c_tugasbesar.selfood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.kom5c_tugasbesar.selfood.Adapter.MenuConfirmAdapter;
import com.kom5c_tugasbesar.selfood.Adapter.MenuViewAdapter;
import com.kom5c_tugasbesar.selfood.Model.Menu;
import com.kom5c_tugasbesar.selfood.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ProceedOrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button confirmBtn;
    private TextView totalPriceText;
    private int total_price;

    // Firebase
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";
    private DatabaseReference orderRef;

    private FirebaseRecyclerOptions<Menu> options;
    private FirebaseRecyclerAdapter<Menu, MenuConfirmAdapter> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_order);

        confirmBtn = findViewById(R.id.confirm_order_button);
        totalPriceText = findViewById(R.id.total_price_confirm);
        recyclerView = findViewById(R.id.rv_confirm_order);
        recyclerView.setHasFixedSize(true);

        orderRef = FirebaseDatabase.getInstance(dbUrl).getReference("pesanan")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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

                NumberFormat format = new DecimalFormat("###,###");
                String food_price_total = format.format(snapshot.child("total_price").getValue());
                totalPriceText.setText("Rp. " + food_price_total);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ProceedOrderActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String resto_id = getIntent().getStringExtra("restoranid");
                int table_number = getIntent().getIntExtra("restotable", 0);

                orderRef.child("status").setValue("Menunggu Konfirmasi");
                FirebaseDatabase.getInstance(dbUrl).getReference("restoran").child(resto_id)
                        .child("table").child(String.valueOf(table_number)).child("status").setValue("Menunggu Konfirmasi");
            }
        });
    }
}