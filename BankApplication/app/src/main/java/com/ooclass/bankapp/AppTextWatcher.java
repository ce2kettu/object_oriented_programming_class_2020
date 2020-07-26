package com.ooclass.bankapp;

import android.text.Editable;
import android.text.TextWatcher;

public class AppTextWatcher implements TextWatcher {

    public void onContentChanged() {}

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        onContentChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {}
}