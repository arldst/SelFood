package com.kom5c_tugasbesar.selfood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kom5c_tugasbesar.selfood.Adapter.TableAdapter;
import com.kom5c_tugasbesar.selfood.Model.Pelanggan;
import com.kom5c_tugasbesar.selfood.Model.Pesanan;
import com.kom5c_tugasbesar.selfood.Model.Table;
import com.kom5c_tugasbesar.selfood.R;

public class PickTableActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton viewMenuBtn;

    // Firebase
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";
    private DatabaseReference tableRef;
    private DatabaseReference orderRef;
    private DatabaseReference userRef;

    private FirebaseRecyclerOptions<Table> options;
    private FirebaseRecyclerAdapter<Table, TableAdapter> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_table);

        final String resto_id = getIntent().getStringExtra("key");
        final long[] user_status = new long[1];
        final String[] user_name = new String[1];
        final String[] user_id = new String[1];

        viewMenuBtn = findViewById(R.id.view_menu_fab);
        recyclerView = findViewById(R.id.rv_pick_table);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(PickTableActivity.this, 3);

        orderRef = FirebaseDatabase.getInstance(dbUrl).getReference("pesanan");
        userRef = FirebaseDatabase.getInstance(dbUrl).getReference("pelanggan").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        tableRef = FirebaseDatabase.getInstance(dbUrl).getReference("restoran").child(resto_id).child("table");
        FirebaseDatabase.getInstance(dbUrl).getReference("pelanggan").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_name[0] = snapshot.child("fullName").getValue().toString();
                user_status[0] = (long) snapshot.child("status").getValue();
                user_id[0] = snapshot.getKey().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {

                options = new FirebaseRecyclerOptions.Builder<Table>()
                        .setQuery(tableRef, Table.class).build();

                adapter = new FirebaseRecyclerAdapter<Table, TableAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(TableAdapter tableAdapter, int i, Table table) {

                        final String table_status = table.getStatus();
                        final int table_num = (int) table.getNumber();
                        tableAdapter.tableNumber.setText(String.valueOf(table.getNumber()));
                        tableAdapter.tableStatus.setText(table_status);

                        if(table.getPelanggan_id().equals("Available")) {
                            tableAdapter.statusIndicator.setBackgroundResource(R.drawable.background_available);
                        }
                        else {
                            tableAdapter.statusIndicator.setBackgroundResource(R.drawable.background_unavailable);
                        }

                        tableAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(table_status.equals("Tersedia") && user_status[0] == 0) {

                                    tableRef.child(String.valueOf(table_num)).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            snapshot.getRef().child("status").setValue("Sedang Memesan");
                                            snapshot.getRef().child("pelanggan_id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    userRef.child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            user_status[0] = (long) snapshot.getValue();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    orderRef.getRef().child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Pesanan newPesanan = new Pesanan(user_name[0], "Sedang Memesan", table_num);
                                            snapshot.getRef().setValue(newPesanan);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    FirebaseDatabase.getInstance(dbUrl).getReference("pelanggan").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            snapshot.getRef().child("status").setValue(1);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                    Intent startOrderIntent = new Intent(PickTableActivity.this, OrderActivity.class);
                                    startOrderIntent.putExtra("resto_id_order", resto_id);
                                    startOrderIntent.putExtra("table_number", table_num);
                                    startActivity(startOrderIntent);
                                }
                                else {
                                    Toast.makeText(PickTableActivity.this, "Pesanan anda masih dalam proses", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }

                    @Override
                    public TableAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tables, parent, false);

                        return new TableAdapter(v);
                    }
                };

                GridLayoutManager gridLayoutManager = new GridLayoutManager(PickTableActivity.this, 3);
                recyclerView.setLayoutManager(gridLayoutManager);
                adapter.startListening();
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewMenuIntent = new Intent(PickTableActivity.this, RestaurantDetailActivity.class);
                viewMenuIntent.putExtra("resto_id",  resto_id);
                startActivity(viewMenuIntent);
            }
        });
    }
}