package com.ooclass.bankapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ooclass.bankapp.models.Auth;

public class SignupFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup_fragment, container, false);

        MaterialButton createAccountButton = view.findViewById(R.id.button_create_account);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        final TextInputLayout confirmPasswordTextInput = view.findViewById(R.id.confirm_password_text_input);
        final TextInputEditText confirmPasswordEditText = view.findViewById(R.id.confirm_password_edit_text);
        final TextInputEditText emailEditText = view.findViewById(R.id.email_edit_text);
        final TextInputEditText firstNameEditText = view.findViewById(R.id.first_name_edit_text);
        final TextInputEditText lastNameEditText = view.findViewById(R.id.last_name_edit_text);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Auth.isPasswordValid(passwordEditText.getText().toString())) {
                    passwordTextInput.setError(getString(R.string.error_password));
                } else {
                    passwordTextInput.setError(null); // Clear the error

                    // Check that the passwords match
                    if (!confirmPasswordEditText.getText().toString().equals(passwordEditText.getText().toString())) {
                        confirmPasswordTextInput.setError(getString(R.string.error_password_mismatch));
                        return;
                    } else {
                        confirmPasswordTextInput.setError(null); // Clear the error
                    }

                    // Create account
                    boolean signupResult = Auth.register(emailEditText.getText().toString(),
                            passwordEditText.getText().toString(), firstNameEditText.getText().toString(), lastNameEditText.getText().toString());

                    if (signupResult) {
                        // Go to login
                        ((NavigationHost) getActivity()).navigateTo(new LoginFragment(), false);

                        Snackbar.make(view, R.string.signup_success, Snackbar.LENGTH_LONG)
                                .show();
                    } else {
                        Snackbar.make(view, R.string.error_signup_failed, Snackbar.LENGTH_LONG)
                                .show();
                    }
                }

                ((MainActivity)getActivity()).hideKeyboard();
            }
        });

        // Clear the error once it's valid
        passwordEditText.addTextChangedListener(new AppTextWatcher() {
            @Override
            public void onContentChanged() {
                if (Auth.isPasswordValid(passwordEditText.getText().toString())) {
                    passwordTextInput.setError(null); // Clear the error
                }
            }
        });

        confirmPasswordEditText.addTextChangedListener(new AppTextWatcher() {
            @Override
            public void onContentChanged() {
                confirmPasswordTextInput.setError(null); // Clear the error
            }
        });

        return view;
    }
}
