package com.kom5c_tugasbesar.selfood.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kom5c_tugasbesar.selfood.R;

public class MenuOrderAdapter extends RecyclerView.ViewHolder{

    public ImageView foodPic;
    public TextView foodName, foodDesc, foodPrice, itemCount;
    public Button add, remove;

    public MenuOrderAdapter(@NonNull View itemView) {
        super(itemView);

        foodPic = itemView.findViewById(R.id.food_pic_order);
        foodName = itemView.findViewById(R.id.food_name_order);
        foodDesc = itemView.findViewById(R.id.food_description_order);
        foodPrice = itemView.findViewById(R.id.food_price_order);
        itemCount = itemView.findViewById(R.id.item_order_count);
        add = itemView.findViewById(R.id.addOrderBtn);
        remove = itemView.findViewById(R.id.removeOrderBtn);
    }
}
