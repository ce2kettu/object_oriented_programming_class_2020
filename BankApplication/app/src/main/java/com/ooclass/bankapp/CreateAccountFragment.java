package com.ooclass.bankapp;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ooclass.bankapp.application.BankApplication;
import com.ooclass.bankapp.models.AccountType;
import com.ooclass.bankapp.models.Bank;

import java.util.ArrayList;
import java.util.List;

public class CreateAccountFragment extends Fragment implements AdapterView.OnItemClickListener {
    private int selectedIndex = 0;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_account_fragment, container, false);

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

        // Default value
        editTextFilledExposedDropdown.setText(accountTypes.get(selectedIndex), false);

        final MaterialButton createAccountButton = view.findViewById(R.id.create_account_button);
        final TextInputEditText accountNameEditText = view.findViewById(R.id.account_name_edit_text);
        final TextInputLayout accountNameInput = view.findViewById(R.id.account_name_text_input);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountNameEditText.getText().toString().isEmpty()) {
                    accountNameInput.setError("Account name cannot be empty.");
                } else {
                    accountNameInput.setError(null);
                    Bank.createAccount(accountNameEditText.getText().toString(), intToAccountType(selectedIndex), BankApplication.getInstance().getCurrentUser());

                    // Get lastly inserted account
                    BankApplication.getInstance().setSelectedAccount(BankApplication.accounts.get(BankApplication.accounts.size() - 1));

                    // Show newly created account
                    ((BankActivity)getActivity()).navigateTo(new AccountDetailFragment(), true);
                }
            }
        });

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
