package com.ooclass.bankapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ooclass.bankapp.application.BankApplication;
import com.ooclass.bankapp.models.Account;
import com.ooclass.bankapp.models.BankCard;

import java.util.List;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> dataset;
    private Context context;
    private static final int TYPE_ACCOUNT = 1;
    private static final int TYPE_BANK_CARD = 2;

    public HomeRecyclerViewAdapter(Context context, List<Object> dataset) {
        this.context = context;
        this.dataset = dataset;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case TYPE_ACCOUNT:
                View accountView = inflater.inflate(R.layout.account_view_holder, parent, false);
                return new AccountCardViewHolder(accountView);
            case TYPE_BANK_CARD:
                View bankCardView = inflater.inflate(R.layout.bank_card_view_holder, parent, false);
                return new BankCardViewHolder(bankCardView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_ACCOUNT:
                final Account account = (Account) dataset.get(position);
                AccountCardViewHolder accountViewHolder = (AccountCardViewHolder) holder;
                accountViewHolder.name.setText(account.getName());
                accountViewHolder.balance.setText(String.format("%.2f €", account.getBalance()));

                accountViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BankApplication.getInstance().setSelectedAccount(account);
                        ((BankActivity) context).navigateTo(new AccountDetailFragment(), true);
                    }
                });
                break;
            case TYPE_BANK_CARD:
                final BankCard bankCard = (BankCard) dataset.get(position);
                BankCardViewHolder bankCardViewHolder = (BankCardViewHolder) holder;
                bankCardViewHolder.name.setText(bankCard.getName());

                bankCardViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BankApplication.getInstance().setSelectedBankCard(bankCard);
                        ((BankActivity) context).navigateTo(new BankCardDetailFragment(), true);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (dataset.get(position) instanceof Account) {
            return TYPE_ACCOUNT;
        } else {
            return TYPE_BANK_CARD;
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}