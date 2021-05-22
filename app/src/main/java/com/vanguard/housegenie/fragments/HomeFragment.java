package com.vanguard.housegenie.fragments;

import android.os.Bundle;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import com.vanguard.housegenie.R;
import org.jetbrains.annotations.NotNull;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {

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

        Button disclaimer = view.findViewById(R.id.btnDisclaimer);
        disclaimer.setOnClickListener(view1 -> {
            FragmentHelper.showDisclaimerPopup(view, getActivity());
        });
    }
}