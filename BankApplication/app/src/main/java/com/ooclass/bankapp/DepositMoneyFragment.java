package com.ooclass.bankapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ooclass.bankapp.application.BankApplication;

public class DepositMoneyFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deposit_money_fragment, container, false);

        MaterialButton depositButton = view.findViewById(R.id.deposit_button);
        final TextInputEditText amountEditText = view.findViewById(R.id.deposit_amount_edit_text);

        depositButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankApplication.getInstance().getSelectedAccount().deposit(Float.parseFloat(amountEditText.getText().toString()));
                ((BankActivity)getActivity()).navigateTo(new AccountDetailFragment(), true);
            }
        });

        return view;
    }
}
