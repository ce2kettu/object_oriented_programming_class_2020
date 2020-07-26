package com.ooclass.bankapp;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ooclass.bankapp.application.BankApplication;
import com.ooclass.bankapp.models.Account;
import com.ooclass.bankapp.models.AccountType;
import com.ooclass.bankapp.models.Bank;

import java.util.ArrayList;
import java.util.List;

public class AccountDetailFragment extends Fragment implements AdapterView.OnItemClickListener {
    private int selectedIndex = 0;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_detail_fragment, container, false);

        final Account account = BankApplication.getInstance().getSelectedAccount();

        TextView balanceTextView = view.findViewById(R.id.account_balance_title);

        // Populate list
        List<String> accountTypes = new ArrayList<String>();
        for (AccountType type : AccountType.values()) {
            accountTypes.add(type.getType());
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.account_type_dropdown_menu,
                        accountTypes);

        final AutoCompleteTextView editTextFilledExposedDropdown = view.findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);
        editTextFilledExposedDropdown.setInputType(InputType.TYPE_NULL); // disable input
        editTextFilledExposedDropdown.setOnItemClickListener(this);

        final MaterialButton depositMoneyButton = view.findViewById(R.id.deposit_money_button);
        final MaterialButton linkBankCardButton = view.findViewById(R.id.link_bank_card_button);
        final MaterialButton saveAccountButton = view.findViewById(R.id.save_account_button);
        final TextInputEditText accountNameEditText = view.findViewById(R.id.account_name_edit_text);
        final TextInputLayout accountNameInput = view.findViewById(R.id.account_name_text_input);
        final MaterialCheckBox transferableCheckbox = view.findViewById(R.id.can_transfer_money);
        final MaterialCheckBox payableCheckbox = view.findViewById(R.id.can_do_payments);

        depositMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BankActivity)getActivity()).navigateTo(new DepositMoneyFragment(), true);
            }
        });

        saveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountNameEditText.getText().toString().isEmpty()) {
                    accountNameInput.setError("Account name cannot be empty.");
                } else {
                    accountNameInput.setError(null);

                    account.setType(intToAccountType(selectedIndex));
                    account.setName(accountNameEditText.getText().toString());
                    account.setPayable(payableCheckbox.isChecked());
                    account.setTransferable(transferableCheckbox.isChecked());

                    Bank.saveAccount(account);

                    ((BankActivity)getActivity()).showSnackBar("Account saved!", Snackbar.LENGTH_LONG);

                    // Reload view
                    ((BankActivity)getActivity()).clearBackstack();
                    ((BankActivity)getActivity()).navigateTo(new AccountDetailFragment(), false);
                }
            }
        });

        linkBankCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bank.createBankCard(account);

                // Set active bank card
                BankApplication.getInstance().setSelectedBankCard(BankApplication.bankCards.get(BankApplication.bankCards.size() - 1));
                ((BankActivity)getActivity()).clearBackstack();
                ((BankActivity)getActivity()).navigateTo(new BankCardDetailFragment(), false);
            }
        });

        // Set account values
        balanceTextView.setText(String.format("Balance: %.2f €", account.getBalance()));
        editTextFilledExposedDropdown.setText(accountTypes.get(account.getType().getCode()), false);
        accountNameEditText.setText(account.getName());
        transferableCheckbox.setChecked(account.isTransferable());
        payableCheckbox.setChecked(account.isPayable());

        return view;
    }

    private AccountType intToAccountType(int pos) {
        for (AccountType type : AccountType.values()) {
            if (type.getCode() == pos) {
                return type;
            }
        }

        return AccountType.CURRENT_ACCOUNT;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedIndex = position;
    }
}
