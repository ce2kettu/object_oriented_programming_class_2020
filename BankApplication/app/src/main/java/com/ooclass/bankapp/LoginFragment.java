package com.ooclass.bankapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ooclass.bankapp.application.BankApplication;
import com.ooclass.bankapp.models.Auth;
import com.ooclass.bankapp.models.User;

import java.util.List;

public class LoginFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        final TextInputEditText emailEditText = view.findViewById(R.id.email_edit_text);
        MaterialButton nextButton = view.findViewById(R.id.login_button);
        MaterialButton signupButton = view.findViewById(R.id.signup_button);

        // Set an error if the password is less than 8 characters.
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Auth.isPasswordValid(passwordEditText.getText().toString())) {
                    passwordTextInput.setError(getString(R.string.error_password));
                } else {
                    passwordTextInput.setError(null); // Clear the error

                    boolean authResult = Auth.login(emailEditText.getText().toString(), passwordEditText.getText().toString());

                    // Auth successful
                    if (authResult) {
                        List<User> users = BankApplication.users;

                        // Save as the current user
                        for (User user : users) {
                            // Email address is unique
                            if (user.getEmailAddress().contains(emailEditText.getText().toString())) {
                                BankApplication.getInstance().setCurrentUser(user);
                            }
                        }

                        final String authCode = Auth.generate2FACode();

                        // Create dialog to handle 2FA code
                        final AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                                .setTitle("2FA confirmation")
                                .setMessage("Enter code: " + authCode)
                                .setView(R.layout.code_dialog_layout)
                                .setNegativeButton("Cancel", null)
                                .setPositiveButton("Submit", null)
                                .show();

                        final TextInputLayout codeTextInput = dialog.findViewById(R.id.code_input);
                        final TextInputEditText codeEditText = dialog.findViewById(R.id.code_edit_text);

                        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Correct code
                                if (codeEditText.getText().toString().equals(authCode)) {
                                    passwordTextInput.setError(null);
                                    dialog.dismiss();

                                    List<User> users = BankApplication.users;

                                    // Save as the current user
                                    for (User user : users) {
                                        // Email address is unique
                                        if (user.getEmailAddress().contains(emailEditText.getText().toString())) {
                                            BankApplication.getInstance().setCurrentUser(user);
                                        }
                                    }

                                    ((MainActivity) getActivity()).startHomeActivity();
                                } else {
                                    codeTextInput.setError(getString(R.string.error_2fa_code));
                                }
                            }
                        });

                        // Clear error when typing
                        codeEditText.addTextChangedListener(new AppTextWatcher() {
                            @Override
                            public void onContentChanged() {
                                codeTextInput.setError(null);
                            }
                        });
                    } else {
                        Snackbar.make(view, R.string.error_login_failed, Snackbar.LENGTH_LONG)
                                .show();
                    }
                }

                ((MainActivity)getActivity()).hideKeyboard();
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((NavigationHost) getActivity()).navigateTo(new SignupFragment(), true); // Navigate to the next Fragment
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

        return view;
    }
}
