package com.ooclass.bankapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ooclass.bankapp.application.BankApplication;
import com.ooclass.bankapp.models.Address;
import com.ooclass.bankapp.models.Auth;
import com.ooclass.bankapp.models.User;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.profile_fragment, container, false);

        final MaterialButton logoutButton = view.findViewById(R.id.logout_button);
        final ExtendedFloatingActionButton saveButton = view.findViewById(R.id.profile_save_button);
        final TextInputEditText emailEditText = view.findViewById(R.id.email_edit_text);
        final TextInputEditText firstNameEditText = view.findViewById(R.id.first_name_edit_text);
        final TextInputEditText lastNameEditText = view.findViewById(R.id.last_name_edit_text);
        final TextInputEditText phoneNumberEditText = view.findViewById(R.id.phone_number_edit_text);
        final TextInputEditText addressEditText = view.findViewById(R.id.street_address_edit_text);
        final TextInputEditText cityEditText = view.findViewById(R.id.city_edit_text);
        final TextInputEditText postCodeEditText = view.findViewById(R.id.post_code_edit_text);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        final TextInputEditText currentPasswordEditText = view.findViewById(R.id.current_password_edit_text);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);

        final User currentUser = BankApplication.getInstance().getCurrentUser();

        // Set initial values
        emailEditText.setText(currentUser.getEmailAddress());
        firstNameEditText.setText(currentUser.getFirstName());
        lastNameEditText.setText(currentUser.getLastName());
        phoneNumberEditText.setText(currentUser.getPhoneNumber());
        addressEditText.setText(currentUser.getAddress().street);
        cityEditText.setText(currentUser.getAddress().city);
        postCodeEditText.setText(currentUser.getAddress().postCode);

        // Save profile
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Auth.comparePassword(currentPasswordEditText.getText().toString(), currentUser.getPassword(), currentUser.getSalt())) {
                    ((BankActivity)getActivity()).showSnackBar("Password does not match the current password.", Snackbar.LENGTH_LONG);
                    return;
                }

                // Check if new password is valid if the field is not empty
                if (!passwordEditText.getText().toString().isEmpty()) {
                    if (!Auth.isPasswordValid(passwordEditText.getText().toString())) {
                        passwordTextInput.setError(getString(R.string.error_password));
                        return;
                    } else {
                        Auth.updatePassword(currentUser, passwordEditText.getText().toString());
                    }
                }

                Address newAddress = new Address();
                newAddress.city = cityEditText.getText().toString();
                newAddress.street = addressEditText.getText().toString();
                newAddress.postCode = postCodeEditText.getText().toString();

                currentUser.setEmailAddress(emailEditText.getText().toString());
                currentUser.setFirstName(firstNameEditText.getText().toString());
                currentUser.setLastName(lastNameEditText.getText().toString());
                currentUser.setPhoneNumber(phoneNumberEditText.getText().toString());
                currentUser.setAddress(newAddress);

                Auth.updateProfile(currentUser);

                ((BankActivity)getActivity()).showSnackBar("Profile updated.", Snackbar.LENGTH_LONG);
                ((BankActivity)getActivity()).hideKeyboard();

                // Reload view
                ((NavigationHost) getActivity()).navigateTo(new ProfileFragment(), false);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.logout();

                Intent i = new Intent(getActivity(), MainActivity.class);
                getActivity().finish();
                startActivity(i);
            }
        });

        return view;
    }
}
