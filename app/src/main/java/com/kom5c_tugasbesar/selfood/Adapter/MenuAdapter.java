package com.kom5c_tugasbesar.selfood.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kom5c_tugasbesar.selfood.R;

public class MenuAdapter extends RecyclerView.ViewHolder{

    public ImageView foodPic;
    public TextView foodName, foodDescription, foodPrice;
    public Button deleteBtn;

    public MenuAdapter(@NonNull View itemView) {
        super(itemView);

        foodPic = itemView.findViewById(R.id.food_pic);
        foodName = itemView.findViewById(R.id.food_name);
        foodDescription = itemView.findViewById(R.id.food_description);
        foodPrice = itemView.findViewById(R.id.food_price);
        deleteBtn = itemView.findViewById(R.id.delete_menu_btn);
    }
}
