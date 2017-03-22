package android.ctm.com.ctm.utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.ctm.com.ctm.CTMApp;

/**
 * Created by Paul on 3/9/17.
 */

public class CTMPrefs {

    private static final String LOG_TAG = CTMPrefs.class.getName();

    private static final String PREF_FILE = "ctm_prefs";

    private static final String KEY_URL_ID = "url_id";

    public static String getStringPref(String key, String defaultVal){
        return getStringPref(key, defaultVal);
    }

    public static String getStringPref(String key){
        SharedPreferences sp = CTMApp.getInstance().getSharedPreferences(PREF_FILE, Activity.MODE_PRIVATE);
        String strVal = sp.getString(key, null);
        return strVal;
    }

    public static void saveStringPref(String key, String val){
        SharedPreferences sp = CTMApp.getInstance().getSharedPreferences(PREF_FILE, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }

    public static void saveIntPref(String key, int val){
        SharedPreferences sp = CTMApp.getInstance().getSharedPreferences(PREF_FILE, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_URL_ID, val);
        editor.commit();
    }

    public static int getIntPref(String key){
        SharedPreferences sp = CTMApp.getInstance().getSharedPreferences(PREF_FILE, Activity.MODE_PRIVATE);
        int intVal = sp.getInt(key , -1);
        return intVal;
    }

    public static String getKeyUrlPref(){
        return getStringPref(KEY_URL_ID);
    }

    public static void saveKeyUrlPref(String val){
        saveStringPref(KEY_URL_ID, val);
    }

}
