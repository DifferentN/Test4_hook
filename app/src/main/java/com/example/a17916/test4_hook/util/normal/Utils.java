package com.example.a17916.test4_hook.util.normal;

import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vector on 16/8/12.
 */
public class Utils {
    public static boolean isEmpty(List l){
        if(l == null || l.size() == 0){
            return true;
        }
        return false;
    }


    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }


    public static String toURLEncoded(String paramString) {
        if (paramString == null || paramString.equals("")) {
            return "";
        }

        try
        {
            String str = new String(paramString.getBytes(), "utf-8");
            str = URLEncoder.encode(str, "utf-8");
            return str;
        }
        catch (Exception localException)
        {
        }

        return "";
    }
}
