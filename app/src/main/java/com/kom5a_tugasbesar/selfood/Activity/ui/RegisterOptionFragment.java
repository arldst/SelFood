package com.kom5a_tugasbesar.selfood.Activity.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kom5a_tugasbesar.selfood.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterOptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterOptionFragment extends Fragment {

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
        return inflater.inflate(R.layout.fragment_register_option, container, false);
    }
}