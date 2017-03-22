package android.ctm.com.ctm.utils;

import android.content.Context;
import android.ctm.com.ctm.CTMApp;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Paul on 3/11/17.
 */

public class Utils {

    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) CTMApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
