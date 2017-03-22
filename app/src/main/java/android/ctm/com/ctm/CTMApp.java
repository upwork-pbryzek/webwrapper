package android.ctm.com.ctm;

import android.app.Application;
import android.ctm.com.ctm.network.CTMApi;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Paul on 3/9/17.
 */

public class CTMApp extends Application {

    // Singleton instance
    private static CTMApp sInstance = null;

    private CTMApi mApi;

    @Override
    public void onCreate() {
        super.onCreate();
        // Setup singleton instance
        sInstance = this;

        RequestQueue queue = Volley.newRequestQueue(this);
        mApi = new CTMApi(queue);
    }

    public CTMApi getmApi() {
        return mApi;
    }

    // Getter to access Singleton instance
    public static CTMApp getInstance() {
        return sInstance ;
    }
}