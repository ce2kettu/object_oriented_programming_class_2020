package com.ooclass.bankapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ooclass.bankapp.application.BankApplication;
import com.ooclass.bankapp.models.Bank;

public class TransactionsFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transactions_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.transaction_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        TransactionRecyclerViewAdapter mAdapter = new TransactionRecyclerViewAdapter(getActivity(), Bank.getUserTransactions(BankApplication.getInstance().getCurrentUser()));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        return view;
    }
}
