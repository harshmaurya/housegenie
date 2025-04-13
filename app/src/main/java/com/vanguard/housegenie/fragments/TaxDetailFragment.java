package com.vanguard.housegenie.fragments;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import com.vanguard.housegenie.R;
import com.vanguard.housegenie.domain.AustraliaTaxDetails;
import com.vanguard.housegenie.domain.EmptyTaxDetails;
import com.vanguard.housegenie.domain.ITaxDetails;
import com.vanguard.housegenie.domain.IndiaTaxDetails;
import com.vanguard.housegenie.models.Countries;
import com.vanguard.housegenie.utils.AppSession;
import com.vanguard.housegenie.utils.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import static com.vanguard.housegenie.analytics.FinancialCalculator.toFractionalRate;

interface TaxDetailsFetcher {
    ITaxDetails getTaxDetails();
}

public class TaxDetailFragment {

    public static TaxDetailsFetcher SetupTaxView(@NotNull View view, Context context) {

        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        FrameLayout taxSection = view.findViewById(R.id.taxDetails);
        setupTaxDefaults(view);
        AtomicReference<Boolean> enableTax = new AtomicReference<>(false);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioTaxEnable) {
                taxSection.setVisibility(View.VISIBLE);
                enableTax.set(true);
            } else {
                taxSection.setVisibility(View.GONE);
                enableTax.set(false);
            }
        });

        return () -> {

            if (!enableTax.get()) {
                return new EmptyTaxDetails();
            }

            String country = AppSession.getSelectedCountry();

            if (country.equals(Countries.India)) {
                EditText txtTaxRate = view.findViewById(R.id.txtTaxRate);
                String taxRateString = txtTaxRate.getText().toString();

                EditText txtInterestCap = view.findViewById(R.id.txtInterestCap);
                String interestCapString = txtInterestCap.getText().toString();

                if (StringUtils.isNullOrEmpty(taxRateString) || StringUtils.isNullOrEmpty(interestCapString)) {
                    return new EmptyTaxDetails();
                }

                BigDecimal taxRate = new BigDecimal(taxRateString);
                BigDecimal interestCap = new BigDecimal(interestCapString);

                return new IndiaTaxDetails(toFractionalRate(taxRate), interestCap);
            }

            if (country.equals(Countries.Australia)) {
                EditText txtTaxRate = view.findViewById(R.id.txtAustraliaMarginalTaxRate);
                String taxRateString = txtTaxRate.getText().toString();
                if (StringUtils.isNullOrEmpty(taxRateString)) {
                    return new EmptyTaxDetails();
                }
                BigDecimal taxRate = new BigDecimal(taxRateString);
                return new AustraliaTaxDetails(toFractionalRate(taxRate));
            }

            return new EmptyTaxDetails();
        };
    }

    private static void setupTaxDefaults(@NotNull View view) {
        String country = AppSession.getSelectedCountry();
        FrameLayout topSection = view.findViewById(R.id.topTaxSection);
        FrameLayout indiaTaxSection = view.findViewById(R.id.indiaTaxDetails);
        FrameLayout australiaTaxSection = view.findViewById(R.id.australiaTaxDetails);
        indiaTaxSection.setVisibility(View.GONE);
        australiaTaxSection.setVisibility(View.GONE);

        if (country.equals(Countries.India)) {
            indiaTaxSection.setVisibility(View.VISIBLE);
            EditText txtTaxRate = view.findViewById(R.id.txtTaxRate);
            txtTaxRate.setText(R.string.india_marginal_tax);
            EditText txtInterestCap = view.findViewById(R.id.txtInterestCap);
            txtInterestCap.setText(R.string.india_interest_cap);
        } else if (country.equals(Countries.Australia)) {
            australiaTaxSection.setVisibility(View.VISIBLE);
            EditText txtTaxRate = view.findViewById(R.id.txtAustraliaMarginalTaxRate);
            txtTaxRate.setText(R.string.australia_marginal_tax);
        } else {
            topSection.setVisibility(View.GONE);
        }
    }
}
