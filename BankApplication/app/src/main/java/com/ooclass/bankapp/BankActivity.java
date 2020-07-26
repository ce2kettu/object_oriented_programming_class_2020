package com.ooclass.bankapp;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.ooclass.bankapp.application.BankApplication;

public class BankActivity extends AppCompatActivity implements NavigationHost {
    View mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_activity);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.bank_container, new HomeFragment())
                    .commit();
        }

        mainLayout = findViewById(R.id.bank_container_coordinator);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.page_home:
                        changePage(new HomeFragment());
                        break;
                    case R.id.page_transactions:
                        changePage(new TransactionsFragment());
                        break;
                    case R.id.page_profile:
                        changePage(new ProfileFragment());
                        break;
                    default:
                        return false;
                }

                return true;
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        BankApplication.saveDataToStorage();
    }

    public void showSnackBar(@NonNull CharSequence text, @BaseTransientBottomBar.Duration int duration) {
        Snackbar.make(mainLayout, text, duration)
                .show();
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        if (getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void clearBackstack() {
        FragmentManager fm = getSupportFragmentManager();

        // Clear backstack
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    /* Navigates to the given page and clears the backstack */
    public void changePage(Fragment fragment) {
        clearBackstack();
        navigateTo(fragment, false);
    }

    /* Navigate to the given fragment */
    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.bank_container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
