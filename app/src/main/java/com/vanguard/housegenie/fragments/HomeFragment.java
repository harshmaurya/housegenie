package com.vanguard.housegenie.fragments;

import android.content.Context;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import com.vanguard.housegenie.R;
import com.vanguard.housegenie.models.Countries;
import com.vanguard.housegenie.utils.AndroidUtils;
import com.vanguard.housegenie.utils.CurrencyCountryMapping;
import com.vanguard.housegenie.utils.AppSession;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setupLocale(View view) {
        // Create the list of CountryCurrency objects
        List<String> countries = CurrencyCountryMapping.getCountryToCurrencyMapping().keySet()
                .stream().sorted().collect(Collectors.toList());

        // Bind the list to the Spinner
        Spinner spinner = view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, countries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // Set dropdown layout
        spinner.setAdapter(adapter);

        setDefaultCountry(view, countries, spinner);

        // Handle item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Context context = (view != null) ? view.getContext() : parent.getContext();
                String selectedCountry = adapter.getItem(position);
                AppSession.setSelectedCountry(selectedCountry);
                AndroidUtils.saveUserPreference(context, AndroidUtils.countryPreference, selectedCountry);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional
            }
        });
    }

    private static void setDefaultCountry(View view, List<String> countryCurrencyList, Spinner spinner) {
        String defaultCountry = AndroidUtils.getUserPreference(view.getContext(), AndroidUtils.countryPreference,
                Locale.getDefault().getCountry());
        int defaultIndex = 0;

        for (int i = 0; i < countryCurrencyList.size(); i++) {
            if (countryCurrencyList.get(i).equalsIgnoreCase(defaultCountry)) {
                defaultIndex = i;
                break;
            }
        }
        spinner.setSelection(defaultIndex);
        AppSession.setSelectedCountry(defaultCountry);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {

        setupLocale(view);
        final NavController navController = NavHostFragment.findNavController(this);

        Button rentvsBuy = view.findViewById(R.id.btnRentvsbuy);
        final NavDirections action = HomeFragmentDirections.actionHomeFragmentToRentVsBuyFragment();
        rentvsBuy.setOnClickListener(view1 -> navController.navigate(action));

        Button houseVsOthers = view.findViewById(R.id.btnHomeVsOthers);
        final NavDirections action2 = HomeFragmentDirections.actionHomeFragmentToHouseVsOtherInvestmentFragment();
        houseVsOthers.setOnClickListener(view1 -> navController.navigate(action2));

        Button houseValue = view.findViewById(R.id.btnHouseValue);
        final NavDirections action3 = HomeFragmentDirections.actionHomeFragmentToHouseValueFragment();
        houseValue.setOnClickListener(view1 -> navController.navigate(action3));

        FragmentHelper.SetupFooterLinks(view, getActivity());
    }
}