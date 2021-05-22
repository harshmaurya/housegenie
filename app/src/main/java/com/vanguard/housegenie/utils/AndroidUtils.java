package com.vanguard.housegenie.utils;

import android.content.Context;
import android.os.Build;
import android.os.LocaleList;
import android.telephony.TelephonyManager;
import androidx.annotation.RequiresApi;

public class AndroidUtils {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String getCountryCode(Context context){
        String code;
        try {
            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            code = tm.getSimCountryIso();
        }
        catch (Exception e){
            LocaleList locales = context.getResources().getConfiguration().getLocales();
            return locales.get(0).getCountry().toUpperCase();
        }

        return code.toUpperCase();
    }
}
