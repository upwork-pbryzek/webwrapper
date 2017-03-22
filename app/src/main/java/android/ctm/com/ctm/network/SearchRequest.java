package android.ctm.com.ctm.network;

import android.ctm.com.ctm.consts.Urls;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Paul on 3/16/17.
 */

public class SearchRequest extends Request<JSONObject> {

    private static final String LOG_TAG = SearchRequest.class.getName();

    private final Listener<JSONObject> mListener;

    private static final String PARAM_SERVICE_CODE = "serviceCode";
    private static final String PARAM_SEARCH = "search";
    private static final String VALUE_SEARCH_CUSTOMERS = "SearchCustomers";

    private JSONObject jsonBody;

    public SearchRequest(String search, Listener<JSONObject> listener, ErrorListener errorListener) {
        super(Method.POST, Urls.buildSearchUrl(), errorListener);
        mListener = listener;

        jsonBody = new JSONObject();
        try {
            jsonBody.put(PARAM_SERVICE_CODE, VALUE_SEARCH_CUSTOMERS);
            jsonBody.put(PARAM_SEARCH, search);
        } catch (Exception e){
            Log.e(LOG_TAG, "SearchRequest:" , e);
        }
    }

    @Override
    /**
     * Returns the raw POST or PUT body to be sent.
     *
     * @throws AuthFailureError in the event of auth failure
     */
    public byte[] getBody() throws AuthFailureError {
        final String requestBody = jsonBody.toString();
        try {
            return requestBody == null ? null : requestBody.getBytes("utf-8");
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
            return null;
        }
    }

    @Override
    public String getBodyContentType() {
        return "application/json; charset=utf-8";
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }

    @Override
    public String getUrl() {
        return Urls.buildSearchUrl();
    }

}