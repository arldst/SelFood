package com.kom5c_tugasbesar.selfood.Activity.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

public class RestoDashboardFragment extends Fragment {

    private RelativeLayout placeholder;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addTableBtn;
    private ProgressBar mProgBar;

    // Firebase
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";
    private DatabaseReference tableRef;

    private FirebaseRecyclerOptions<Table> options;
    private FirebaseRecyclerAdapter<Table, TableAdapter> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resto_dashboard, container, false);

        mProgBar = view.findViewById(R.id.resto_dashboard_progressbar);
        addTableBtn = view.findViewById(R.id.add_table_button);
        placeholder = view.findViewById(R.id.empty_table_placeholder);
        recyclerView = view.findViewById(R.id.rv_table);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getActivity(), 3);

        tableRef = FirebaseDatabase.getInstance(dbUrl).getReference("restoran").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("table");

        tableRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {

                    recyclerView.setVisibility(View.VISIBLE);

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
                                    Toast.makeText(getActivity(), "Testing...", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public TableAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tables, parent, false);

                            return new TableAdapter(v);
                        }
                    };

                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);
                }
                else {
                    placeholder.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        addTableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgBar.setVisibility(View.VISIBLE);

                tableRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()) {

                            long count = dataSnapshot.getChildrenCount() + 1;
                            Table newTable = new Table(count, "Available", "Tersedia");
                            tableRef.child(String.valueOf(count)).setValue(newTable).addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        mProgBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Berhasil menambah meja", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        mProgBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Gagal menambah meja", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            Table newTable = new Table(1, "Available", "Tersedia");
                            tableRef.child("1").setValue(newTable).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        mProgBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Berhasil menambah meja", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        mProgBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Gagal menambah meja", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;
    }
}