package com.ooclass.bankapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.ooclass.bankapp.application.BankApplication;
import com.ooclass.bankapp.models.Bank;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.home_fragment, container, false);

        final MaterialButton createAccountButton = view.findViewById(R.id.create_bank_account_button);
        final MaterialButton ownTransferButton = view.findViewById(R.id.own_transfer_button);
        final MaterialButton newPaymentButton = view.findViewById(R.id.new_payment_button);
        final RecyclerView recyclerView = view.findViewById(R.id.account_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        // Create dataset with all user accounts and bank cards and pass them to the recyclerview
        List<Object> dataset = new ArrayList<Object>();
        dataset.addAll(Bank.getUserAccounts(BankApplication.getInstance().getCurrentUser()));
        dataset.addAll(Bank.getUserBankCards(BankApplication.getInstance().getCurrentUser()));

        HomeRecyclerViewAdapter mAdapter = new HomeRecyclerViewAdapter(getActivity(), dataset);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new CreateAccountFragment(), true);
            }
        });

        ownTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new OwnTransferFragment(), true);
            }
        });

        newPaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavigationHost) getActivity()).navigateTo(new NewPaymentFragment(), true);
            }
        });

       return view;
    }
}
