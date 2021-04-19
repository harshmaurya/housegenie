package com.vanguard.housegenie.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.NumberFormat;

public class MoneyTextWatcher implements TextWatcher {
    private final WeakReference<TextView> textViewWeakReference;

    public MoneyTextWatcher(TextView editText) {
        textViewWeakReference = new WeakReference<>(editText);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        TextView textView = textViewWeakReference.get();
        if (textView == null) return;
        String s = editable.toString();
        if (s.isEmpty()) return;
        textView.removeTextChangedListener(this);
        String cleanString = s.replaceAll("[$,.]", "");
        BigDecimal parsed = new BigDecimal(cleanString).setScale(0, BigDecimal.ROUND_FLOOR).divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
        String formatted = NumberFormat.getCurrencyInstance().format(parsed);
        textView.setText(formatted);
        textView.addTextChangedListener(this);
    }
}