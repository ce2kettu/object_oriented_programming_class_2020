package com.ooclass.bankapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView type;
    public TextView amount;
    public TextView date;
    public TextView accountName;

    public TransactionViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.transaction_name);
        amount = itemView.findViewById(R.id.transaction_amount);
        type = itemView.findViewById(R.id.transaction_type);
        date = itemView.findViewById(R.id.transaction_date);
        accountName = itemView.findViewById(R.id.transaction_account_name);
    }
}