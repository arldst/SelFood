package com.kom5c_tugasbesar.selfood.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kom5c_tugasbesar.selfood.R;

public class MenuViewAdapter extends RecyclerView.ViewHolder{

    public ImageView foodPic;
    public TextView foodName, foodDescription, foodPrice;

    public MenuViewAdapter(@NonNull View itemView) {
        super(itemView);

        foodPic = itemView.findViewById(R.id.food_pic_menu_view);
        foodName = itemView.findViewById(R.id.food_name_menu_view);
        foodDescription = itemView.findViewById(R.id.food_description_menu_view);
        foodPrice = itemView.findViewById(R.id.food_price_menu_view);
    }
}
