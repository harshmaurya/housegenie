package com.vanguard.housegenie.fragments;

import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.fragment.app.FragmentActivity;
import com.vanguard.housegenie.R;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class FragmentHelper {

    private static final String disclaimerLink = "https://bvnsasha.github.io/disclaimer/house-genie-disclaimer.html";
    private static final String privacyPolicyLink = "https://bvnsasha.github.io/privacy-policy/house-genie-policy.html";

    public static void SetupFooterLinks(@NotNull View view, FragmentActivity activity){
        Button disclaimer = view.findViewById(R.id.btnDisclaimer);
        disclaimer.setOnClickListener(view1 -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(disclaimerLink));
            activity.startActivity(browserIntent);
        });

        Button privacyPolicy = view.findViewById(R.id.btnPrivacyPolicy);
        privacyPolicy.setOnClickListener(view1 -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyLink));
            activity.startActivity(browserIntent);
        });
    }

    @NotNull
    public static View showDisclaimerPopup(@NotNull View view, FragmentActivity activity) {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                Objects.requireNonNull(activity).getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_disclaimer, null);
        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });

        Button dismissButton = popupView.findViewById(R.id.btnDismiss);
        dismissButton.setOnClickListener(v -> popupWindow.dismiss());
        return popupView;
    }
}
