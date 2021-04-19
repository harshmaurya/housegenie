package com.vanguard.housegenie.utils;

public class StringUtils {
    public static Boolean isNullOrEmpty(String str){
        return str==null || str.equals("") || str.length()==0;
    }
}
