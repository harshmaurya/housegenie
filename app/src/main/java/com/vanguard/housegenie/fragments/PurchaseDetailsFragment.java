package com.vanguard.housegenie.fragments;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import com.vanguard.housegenie.R;
import com.vanguard.housegenie.analytics.FinancialCalculator;
import com.vanguard.housegenie.domain.LoanDetails;
import com.vanguard.housegenie.domain.PurchaseDetails;
import com.vanguard.housegenie.utils.AndroidUtils;
import com.vanguard.housegenie.utils.FinUtils;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

import static com.vanguard.housegenie.analytics.FinancialCalculator.toFractionalRate;
import static com.vanguard.housegenie.utils.StringUtils.isNullOrEmpty;

interface PurchaseDetailsFetcher{
    PurchaseDetails getPurchaseDetails();
}

public class PurchaseDetailsFragment {

    public static PurchaseDetailsFetcher SetupPurchaseView(@NotNull View view, Context context){

        RadioGroup radioGroup = view.findViewById(R.id.radioGroupLoan);
        FrameLayout cashDetails = view.findViewById(R.id.cashDetails);
        FrameLayout loanDetails = view.findViewById(R.id.loanDetails);

        setupLoanDetails(view, context, loanDetails, cashDetails, radioGroup.getCheckedRadioButtonId());
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            setupLoanDetails(view, context, loanDetails, cashDetails, checkedId);
        });

        return () -> {
            RadioGroup radioLoan = view.findViewById(R.id.radioGroupLoan);
            int checkedId = radioLoan.getCheckedRadioButtonId();
            if(checkedId == R.id.radioLoanDisable){
                int defaultTerm = 10;
                EditText txtCashPayment = view.findViewById(R.id.txtCashDownPayment);
                String cashPaymentStr = txtCashPayment.getText().toString();
                if(isNullOrEmpty(cashPaymentStr)) return null;
                BigDecimal cashPayment = new BigDecimal(cashPaymentStr);
                return new PurchaseDetails(cashPayment, new LoanDetails(BigDecimal.ZERO,defaultTerm,BigDecimal.ZERO));
            }
            else {
                EditText txtDownPayment = view.findViewById(R.id.txtDownPayment);
                String downPaymentString = txtDownPayment.getText().toString();
                EditText txtLoanPrincipal = view.findViewById(R.id.txtLoanPrincipal);
                String loanPrincipalString = txtLoanPrincipal.getText().toString();
                EditText txtLoanInterest = view.findViewById(R.id.txtLoanInterest);
                String loanInterestString = txtLoanInterest.getText().toString();
                EditText txtLoanTerm = view.findViewById(R.id.txtLoanTerm);
                String loanTermString = txtLoanTerm.getText().toString();
                if(isNullOrEmpty(downPaymentString) || isNullOrEmpty(loanPrincipalString)
                        || isNullOrEmpty(loanInterestString) || isNullOrEmpty(loanTermString))
                    return null;

                int loanTerm = Integer.parseInt(loanTermString);
                BigDecimal downPayment = new BigDecimal(downPaymentString);
                BigDecimal loanPrincipal = new BigDecimal(loanPrincipalString);
                BigDecimal loanInterest = new BigDecimal(loanInterestString);
                return new PurchaseDetails(downPayment, new LoanDetails(loanPrincipal,loanTerm, toFractionalRate(loanInterest)));
            }
        };
    }

    private static void setupLoanDetails(@NotNull View view, Context context, FrameLayout loanSection, FrameLayout cashSection, int checkedId) {
        if(checkedId == R.id.radioLoanDisable) {
            cashSection.setVisibility(View.VISIBLE);
            loanSection.setVisibility(View.GONE);
        }
        else {
            cashSection.setVisibility(View.GONE);
            loanSection.setVisibility(View.VISIBLE);
            setupLoanDefaults(view, context);
        }
    }

    private static void setupLoanDefaults(@NotNull View view, Context context){
        EditText txtLoanInterest = view.findViewById(R.id.txtLoanInterest);
        txtLoanInterest.setText("8");
    }
}
