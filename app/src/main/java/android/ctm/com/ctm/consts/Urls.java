package android.ctm.com.ctm.consts;

import android.ctm.com.ctm.utils.CTMPrefs;
import android.util.Log;

/**
 * Created by Paul on 3/9/17.
 */

public class Urls {

    private static final String LOG_TAG = Urls.class.getName();

    private static final String CLINICAL_DOMAIN = "clinicaltm.com/";
    private static final String HTTPS_PROTOCOL = "https://";

    public static final String DEFAULT_URL = "https://clinicaltrainingmanager.com/";
    public static final String HELP_URL = "https://www.google.com/";

    public static final String SEARCH_PATH = "service";

    public static String buildClinicalUrl(){
        String keyPref = CTMPrefs.getKeyUrlPref();
        return buildClinicalUrl(keyPref);
    }

    public static String buildClinicalUrl(String userId){
        String url = HTTPS_PROTOCOL + userId + "." + CLINICAL_DOMAIN;
        return url;
    }

    public static String buildSearchUrl(){
        String url = HTTPS_PROTOCOL + "admin." + CLINICAL_DOMAIN + SEARCH_PATH;
        Log.e(LOG_TAG, "url = " + url);
        return url;
    }

}
