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

public class OwnTransferFragment extends Fragment  {
    private int selectedToAccountIndex = 0;
    private int selectedFromAccountIndex = 0;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.own_transfer_fragment, container, false);

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

        final AutoCompleteTextView toAccountDropdown = view.findViewById(R.id.to_account_dropdown);
        toAccountDropdown.setAdapter(adapter);
        toAccountDropdown.setInputType(InputType.TYPE_NULL); // disable input
        toAccountDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedToAccountIndex = position;
            }
        });

        // Default value
        toAccountDropdown.setText(accountsList.get(selectedToAccountIndex), false);

        final MaterialButton makeTransferButton = view.findViewById(R.id.make_transfer_button);
        final TextInputEditText amountEditText = view.findViewById(R.id.transfer_amount_edit_text);

        makeTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account fromAccount = selectedIndexToAccount(selectedFromAccountIndex);
                Account toAccount = selectedIndexToAccount(selectedToAccountIndex);
                String message = "";

                if (fromAccount != null && toAccount != null) {
                    if (fromAccount == toAccount) {
                        message = "Can't transfer between the same account";
                    } else {
                        ActionResult result = fromAccount.transferPersonal(Float.parseFloat(amountEditText.getText().toString()), toAccount);
                        message = result.message;
                    }
                } else {
                    message = "Select accounts";
                }

                ((BankActivity)getActivity()).showSnackBar(message, Snackbar.LENGTH_LONG);
                ((BankActivity)getActivity()).hideKeyboard();
            }
        });

        return view;
    }

    private Account selectedIndexToAccount(int index) {
        List<Account> ownAccounts = Bank.getUserAccounts(BankApplication.getInstance().getCurrentUser());
        return ownAccounts.get(index);
    }
}
