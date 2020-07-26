package com.ooclass.bankapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class BankCardViewHolder extends RecyclerView.ViewHolder {
    public TextView name;

    public BankCardViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.bank_card_name);
    }
}