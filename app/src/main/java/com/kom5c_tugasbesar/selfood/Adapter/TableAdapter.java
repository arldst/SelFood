package com.kom5c_tugasbesar.selfood.Adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kom5c_tugasbesar.selfood.R;

public class TableAdapter extends RecyclerView.ViewHolder {

    public TextView tableNumber, tableStatus;
    public View statusIndicator;

    public TableAdapter(@NonNull View itemView) {
        super(itemView);

        tableNumber = itemView.findViewById(R.id.table_number);
        tableStatus = itemView.findViewById(R.id.table_status);
        statusIndicator = itemView.findViewById(R.id.table_status_indicator);
    }
}
