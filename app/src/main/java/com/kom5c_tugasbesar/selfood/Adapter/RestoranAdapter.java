package com.kom5c_tugasbesar.selfood.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kom5c_tugasbesar.selfood.R;

public class RestoranAdapter extends RecyclerView.ViewHolder{

    public ImageView restoImg;
    public TextView restoName, restoType;
    public Button orderButton;

    public RestoranAdapter(@NonNull View itemView) {
        super(itemView);

        restoImg = itemView.findViewById(R.id.resto_img_view);
        restoName = itemView.findViewById(R.id.resto_name_view);
        restoType = itemView.findViewById(R.id.resto_type_view);
        orderButton = itemView.findViewById(R.id.view_resto_button);
    }
}
