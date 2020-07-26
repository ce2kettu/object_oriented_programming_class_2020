package com.ooclass.bankapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class AccountCardViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView balance;

    public AccountCardViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.account_name);
        balance = itemView.findViewById(R.id.account_balance);
    }
}