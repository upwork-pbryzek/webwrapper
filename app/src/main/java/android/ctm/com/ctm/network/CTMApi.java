package android.ctm.com.ctm.network;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import org.json.JSONObject;

/**
 * Created by Paul on 3/16/17.
 */
public class CTMApi {

    private final RequestQueue mQueue;

    public CTMApi(RequestQueue queue) {
        mQueue = queue;
    }

    public Request<?> search(String search, Listener<JSONObject> listener,
                             ErrorListener errorListener) {
        return mQueue.add(new SearchRequest(search, listener, errorListener));
    }
}