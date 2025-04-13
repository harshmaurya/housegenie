package com.vanguard.housegenie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.vanguard.housegenie.models.CountryCurrency;

import java.util.List;

public class CountryCurrencyAdapter extends ArrayAdapter<CountryCurrency> {
    public CountryCurrencyAdapter(Context context, List<CountryCurrency> countryCurrencyList) {
        super(context, 0, countryCurrencyList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        CountryCurrency countryCurrency = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(countryCurrency.getCountry());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        CountryCurrency countryCurrency = getItem(position);
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(countryCurrency.getCountry());

        return convertView;
    }
}
