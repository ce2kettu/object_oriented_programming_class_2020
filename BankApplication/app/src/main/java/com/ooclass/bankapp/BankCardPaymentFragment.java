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
import com.ooclass.bankapp.models.ActionResult;
import com.ooclass.bankapp.models.BankCard;
import com.ooclass.bankapp.models.Country;

import java.util.ArrayList;
import java.util.List;

public class BankCardPaymentFragment extends Fragment implements AdapterView.OnItemClickListener {
    private int selectedIndex = 0;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bank_card_payment_fragment, container, false);

        final MaterialButton payButton = view.findViewById(R.id.pay_button);
        final TextInputEditText amountEditText = view.findViewById(R.id.pay_amount_edit_text);
        final TextInputEditText paymentToEditText = view.findViewById(R.id.bank_card_payment_name_edit_text);

        // Populate list
        List<String> countries = new ArrayList<String>();
        for (Country country : Country.values()) {
            countries.add(country.getName());
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        getContext(),
                        R.layout.account_type_dropdown_menu,
                        countries);

        final BankCard bankCard = BankApplication.getInstance().getSelectedBankCard();

        final AutoCompleteTextView editTextFilledExposedDropdown = view.findViewById(R.id.filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(adapter);
        editTextFilledExposedDropdown.setInputType(InputType.TYPE_NULL); // disable input
        editTextFilledExposedDropdown.setOnItemClickListener(this);

        // Default value
        editTextFilledExposedDropdown.setText(countries.get(selectedIndex), false);

        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionResult result = bankCard.payInCountry(paymentToEditText.getText().toString(),
                        Float.parseFloat(amountEditText.getText().toString()), intToCountry(selectedIndex));

                ((BankActivity)getActivity()).showSnackBar(result.message, Snackbar.LENGTH_LONG);
                ((BankActivity)getActivity()).hideKeyboard();
            }
        });

        return view;
    }

    private Country intToCountry(int pos) {
        int i = 0;
        for (Country country : Country.values()) {
            if (i == pos) {
                return country;
            }
            i++;
        }

        return Country.FINLAND;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedIndex = position;
    }
}
