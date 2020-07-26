package com.ooclass.bankapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ooclass.bankapp.application.BankApplication;
import com.ooclass.bankapp.models.Bank;
import com.ooclass.bankapp.models.BankCard;
import com.ooclass.bankapp.models.Country;

import java.util.ArrayList;
import java.util.List;

public class BankCardDetailFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bank_card_detail_fragment, container, false);

        final BankCard bankCard = BankApplication.getInstance().getSelectedBankCard();

        final MaterialButton saveButton = view.findViewById(R.id.save_card_button);
        final TextInputEditText bankCardNameEditText = view.findViewById(R.id.card_name_edit_text);
        final TextInputLayout bankCardNameInput = view.findViewById(R.id.card_name_text_input);
        final TextInputEditText withdrawalLimitEditText = view.findViewById(R.id.withdrawal_limit_edit_text);
        final TextInputEditText payLimitEditText = view.findViewById(R.id.pay_limit_edit_text);
        final MaterialButton withdrawButton = view.findViewById(R.id.card_withdraw_button);
        final MaterialButton paymentButton = view.findViewById(R.id.card_payment_button);
        final TextView linkedAccountNameTextView = view.findViewById(R.id.bank_card_linked_account_name);

        final ChipGroup countryChipGroup = view.findViewById(R.id.country_chip_group);

        int i = 1;
        for (Country country : Country.values()) {
            Chip chip = new Chip(getContext());
            chip.setCheckable(true);
            chip.setId(i);

            if (isCountryAllowed(country, bankCard)) {
                chip.setChecked(true);
            }

            chip.setText(country.getName());
            countryChipGroup.addView(chip);
            i++;
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bankCardNameEditText.getText().toString().isEmpty()) {
                    bankCardNameInput.setError("Bank card name cannot be empty.");
                } else {
                    bankCardNameInput.setError(null);

                    bankCard.setName(bankCardNameEditText.getText().toString());
                    bankCard.setWithdrawalLimit(Float.parseFloat(withdrawalLimitEditText.getText().toString()));
                    bankCard.setPayLimit(Float.parseFloat(payLimitEditText.getText().toString()));
                    bankCard.setAllowedCountries(getSelectedCountries(countryChipGroup.getCheckedChipIds()));

                    Bank.saveBankCard(bankCard);

                    ((BankActivity)getActivity()).showSnackBar("Bank card saved!", Snackbar.LENGTH_LONG);

                    // Reload view
                    ((BankActivity)getActivity()).clearBackstack();
                    ((BankActivity)getActivity()).navigateTo(new BankCardDetailFragment(), false);
                }
            }
        });

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BankActivity)getActivity()).navigateTo(new BankCardPaymentFragment(), true);
            }
        });

        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BankActivity)getActivity()).navigateTo(new WithdrawFragment(), true);
            }
        });

        // Set values
        bankCardNameEditText.setText(bankCard.getName());
        withdrawalLimitEditText.setText(Float.toString(bankCard.getWithdrawalLimit()));
        payLimitEditText.setText(Float.toString(bankCard.getPayLimit()));
        linkedAccountNameTextView.setText("Linked account: " + bankCard.getLinkedAccount().getName());

        return view;
    }

    private boolean isCountryAllowed(Country country, BankCard bankCard) {
        for (Country allowedCountry : bankCard.getAllowedCountries()) {
            if (allowedCountry == country) {
                return true;
            }
        }

        return false;
    }

    private List<Country> getSelectedCountries(List<Integer> listIds) {
        List<Country> countries = new ArrayList<Country>();

        for (Integer id : listIds) {
            int i = 1;
            for (Country country : Country.values()) {
                if (i == id) {
                    countries.add(country);
                }
                i++;
            }
        }

        return countries;
    }
}
