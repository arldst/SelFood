package com.kom5a_tugasbesar.selfood.Activity.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.kom5a_tugasbesar.selfood.Activity.RestaurantRegistrationActivity;
import com.kom5a_tugasbesar.selfood.Activity.UserRegistrationActivity;
import com.kom5a_tugasbesar.selfood.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterOptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterOptionFragment extends Fragment {

    ImageView user_icon, restaurant_icon;

    public RegisterOptionFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RegisterOptionFragment newInstance() {
        RegisterOptionFragment fragment = new RegisterOptionFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_register_option, container, false);

        user_icon = v.findViewById(R.id.user_icon);
        restaurant_icon = v.findViewById(R.id.restaurant_icon);

        user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_user = new Intent(getActivity(), UserRegistrationActivity.class);
                startActivity(intent_user);
            }
        });

        restaurant_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_resto = new Intent(getActivity(), RestaurantRegistrationActivity.class);
                startActivity(intent_resto);
            }
        });

        return v;
    }
}