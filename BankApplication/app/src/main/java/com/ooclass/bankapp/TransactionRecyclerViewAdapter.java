package com.ooclass.bankapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ooclass.bankapp.application.BankApplication;
import com.ooclass.bankapp.models.Account;
import com.ooclass.bankapp.models.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionRecyclerViewAdapter extends RecyclerView.Adapter<TransactionViewHolder> {
    private List<Transaction> transactions;
    private Context context;

    public TransactionRecyclerViewAdapter(Context context, List<Transaction> transactions) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_view_holder, parent, false);
        return new TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        final Transaction transaction = transactions.get(position);

        holder.type.setText(transaction.getType().getName().toUpperCase());

        Account toAccount = transaction.getToAccount();
        Account fromAccount = transaction.getFromAccount();
        String amountPrefix = "+";
        String dateSuffix = "";
        String transactionName = "";

        if (toAccount != null && toAccount.getOwner() == BankApplication.getInstance().getCurrentUser()) {
            holder.amount.setTextColor(Color.parseColor("#aed581"));
            holder.name.setText("To: " + toAccount.getOwner().getFullName());
            holder.accountName.setText("Account: " + toAccount.getName());

            if (transaction.getName() == null || transaction.getName().isEmpty()) {
                if (fromAccount != null) {
                    transactionName = fromAccount.getOwner().getFullName();
                } else {
                    transactionName = transaction.getName();
                }
            } else {
                transactionName = transaction.getName();
            }

            dateSuffix = "from: " + transactionName;
        } else {
            amountPrefix = "-";
            holder.amount.setTextColor(Color.parseColor("#ef5350"));

            if (transaction.getName() == null || transaction.getName().isEmpty()) {
                if (toAccount != null) {
                    transactionName = toAccount.getOwner().getFullName();
                } else {
                    transactionName = transaction.getName();
                }
            } else {
                transactionName = transaction.getName();
            }

            holder.name.setText("To: " + transactionName);
            dateSuffix = "from: " + transaction.getFromAccount().getOwner().getFullName();
            holder.accountName.setText("Account: " + transaction.getFromAccount().getName());
        }

        holder.amount.setText(amountPrefix + String.format("%.2f €", transaction.getAmount()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        holder.date.setText("On " + sdf.format(transaction.getDate()) + " " + dateSuffix);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
