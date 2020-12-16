package com.kom5c_tugasbesar.selfood.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kom5c_tugasbesar.selfood.Adapter.RestoranAdapter;
import com.kom5c_tugasbesar.selfood.Model.Restoran;
import com.kom5c_tugasbesar.selfood.R;

public class SearchResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView notFound;

    // Firebase
    private static final String dbUrl = "https://selfood-9d3b0-default-rtdb.firebaseio.com/";
    private DatabaseReference restoRef;

    private FirebaseRecyclerOptions<Restoran> options;
    private FirebaseRecyclerAdapter<Restoran, RestoranAdapter> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        String searchKeyword = getIntent().getStringExtra("search_key");

        notFound = findViewById(R.id.not_found_text);
        recyclerView = findViewById(R.id.rv_search_result);
        recyclerView.setHasFixedSize(true);

        restoRef = FirebaseDatabase.getInstance(dbUrl).getReference("restoran");

        restoRef.orderByChild("name")
                .startAt(searchKeyword)
                .endAt(searchKeyword + "\uf88ff").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                                            Intent pickTableIntent = new Intent(SearchResultActivity.this, PickTableActivity.class);
                                            pickTableIntent.putExtra("key", resto_id);
                                            startActivity(pickTableIntent);
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

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchResultActivity.this);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            adapter.startListening();
                            recyclerView.setAdapter(adapter);
                        }
                        else {

                            notFound.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}