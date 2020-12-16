package com.kom5c_tugasbesar.selfood.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kom5c_tugasbesar.selfood.R;

public class MenuConfirmAdapter extends RecyclerView.ViewHolder{

    public ImageView foodPic;
    public TextView foodName, foodDescription, foodPrice, foodCount;

    public MenuConfirmAdapter(@NonNull View itemView) {
        super(itemView);

        foodPic = itemView.findViewById(R.id.food_pic_menu_detail);
        foodName = itemView.findViewById(R.id.food_name_menu_detail);
        foodDescription = itemView.findViewById(R.id.food_description_menu_detail);
        foodPrice = itemView.findViewById(R.id.food_price_menu_detail);
        foodCount = itemView.findViewById(R.id.food_count_menu_detail);
    }
}
