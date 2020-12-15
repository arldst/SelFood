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
import com.kom5c_tugasbesar.selfood.Model.Table;
import com.kom5c_tugasbesar.selfood.R;

public class PickTableActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton viewMenuBtn;

    // Firebase
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";
    private DatabaseReference tableRef;

    private FirebaseRecyclerOptions<Table> options;
    private FirebaseRecyclerAdapter<Table, TableAdapter> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_table);

        final String resto_id = getIntent().getStringExtra("key");

        viewMenuBtn = findViewById(R.id.view_menu_fab);
        recyclerView = findViewById(R.id.rv_pick_table);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(PickTableActivity.this, 3);

        tableRef = FirebaseDatabase.getInstance(dbUrl).getReference("restoran").child(resto_id).child("table");

        tableRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                options = new FirebaseRecyclerOptions.Builder<Table>()
                        .setQuery(tableRef, Table.class).build();

                adapter = new FirebaseRecyclerAdapter<Table, TableAdapter>(options) {
                    @Override
                    protected void onBindViewHolder(TableAdapter tableAdapter, int i, Table table) {

                        tableAdapter.tableNumber.setText(String.valueOf(table.getNumber()));
                        tableAdapter.tableStatus.setText(table.getStatus());

                        if(table.getPelanggan_id().equals("Available")) {
                            tableAdapter.statusIndicator.setBackgroundResource(R.drawable.background_available);
                        }
                        else {
                            tableAdapter.statusIndicator.setBackgroundResource(R.drawable.background_unavailable);
                        }

                        tableAdapter.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(PickTableActivity.this, "Testing...", Toast.LENGTH_SHORT).show();


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