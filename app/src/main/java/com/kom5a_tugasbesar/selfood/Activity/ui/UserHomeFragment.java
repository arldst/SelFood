package com.kom5a_tugasbesar.selfood.Activity.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kom5a_tugasbesar.selfood.Adapter.RestoranAdapter;
import com.kom5a_tugasbesar.selfood.Model.Restoran;
import com.kom5a_tugasbesar.selfood.R;

public class UserHomeFragment extends Fragment {

    private RecyclerView recyclerView;

    // Firebase
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";
    private DatabaseReference restoRef;

    private FirebaseRecyclerOptions<Restoran> options;
    private FirebaseRecyclerAdapter<Restoran, RestoranAdapter> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        recyclerView = view.findViewById(R.id.rv_menu_user);
        recyclerView.setHasFixedSize(true);

        restoRef = FirebaseDatabase.getInstance(dbUrl).getReference("restoran");

        restoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot snapshot) {
                if(snapshot.exists()) {

                    options = new FirebaseRecyclerOptions.Builder<Restoran>()
                            .setQuery(restoRef, Restoran.class).build();

                    adapter = new FirebaseRecyclerAdapter<Restoran, RestoranAdapter>(options) {
                        @Override
                        protected void onBindViewHolder(RestoranAdapter restoranAdapter, int i, Restoran restoran) {

                            final String resto_id = restoran.getId();

                            Glide.with(restoranAdapter.itemView.getContext())
                                    .load(restoran.getImgUrl())
                                    .apply(new RequestOptions().override(360, 140))
                                    .into(restoranAdapter.restoImg);

                            restoranAdapter.restoName.setText(restoran.getName());
                            restoranAdapter.restoType.setText(restoran.getType());

                            restoranAdapter.orderButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d("Tes", "onClick: " + resto_id);
                                }
                            });
                        }

                        @NonNull
                        @Override
                        public RestoranAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurants, parent, false);

                            return new RestoranAdapter(v);
                        }
                    };

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}