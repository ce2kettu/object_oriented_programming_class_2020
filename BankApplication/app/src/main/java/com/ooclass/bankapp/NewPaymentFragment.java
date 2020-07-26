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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.ooclass.bankapp.application.BankApplication;
import com.ooclass.bankapp.models.Account;
import com.ooclass.bankapp.models.ActionResult;
import com.ooclass.bankapp.models.Bank;

import java.util.ArrayList;
import java.util.List;

public class NewPaymentFragment extends Fragment {
    private int selectedFromAccountIndex = 0;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_payment_fragment, container, false);

        List<Account> ownAccounts = Bank.getUserAccounts(BankApplication.getInstance().getCurrentUser());
        List<String> accountsList = new ArrayList<String>();

        for (Account account : ownAccounts) {
            accountsList.add(account.getName());
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.account_type_dropdown_menu,
                        accountsList);


        final AutoCompleteTextView fromAccountDropdown = view.findViewById(R.id.from_account_dropdown);
        fromAccountDropdown.setAdapter(adapter);
        fromAccountDropdown.setInputType(InputType.TYPE_NULL); // disable input
        fromAccountDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFromAccountIndex = position;
            }
        });

        // Default value
        fromAccountDropdown.setText(accountsList.get(selectedFromAccountIndex), false);

        final MaterialButton makeTransferButton = view.findViewById(R.id.make_transfer_button);
        final TextInputEditText amountEditText = view.findViewById(R.id.transfer_amount_edit_text);
        final TextInputEditText toAccountEditText = view.findViewById(R.id.to_account_edit_text);

        makeTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account fromAccount = selectedIndexToAccount(selectedFromAccountIndex);
                String message = "";
                float amount = Float.parseFloat(amountEditText.getText().toString());

                if (fromAccount != null) {
                    Account toAccount = findAccountFromString(toAccountEditText.getText().toString());

                    // Internal bank transfer
                    if (toAccount != null) {
                        ActionResult result = fromAccount.transferInternal(amount, toAccount);
                        message = result.message;

                        // External bank transfer
                    } else {
                        ActionResult result = fromAccount.transferExternal(amount, toAccountEditText.getText().toString());
                        message = result.message;
                    }

                } else {
                    message = "Select account to move money from";
                }

                ((BankActivity) getActivity()).showSnackBar(message, Snackbar.LENGTH_LONG);
                ((BankActivity) getActivity()).hideKeyboard();
            }
        });

        return view;
    }

    private Account findAccountFromString(String searchTerm) {
        try {
            String prefix = "FI_BANK";

            if (searchTerm.startsWith(prefix)) {
                int i = searchTerm.indexOf(prefix);
                int accountId = Integer.parseInt(searchTerm.substring(i + prefix.length()));
                List<Account> accounts = BankApplication.accounts;

                for (Account account : accounts) {
                    if (account.getAccountId() == accountId) {
                        return account;
                    }
                }
            }

            return null;
        } catch (Exception e){
            return null;
        }
    }

    private Account selectedIndexToAccount(int index) {
        List<Account> ownAccounts = Bank.getUserAccounts(BankApplication.getInstance().getCurrentUser());
        return ownAccounts.get(index);
    }
}
