package fastcurrencyconverter.mient.com.fastcurrencyconverter.services;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Mircea-Ionel on 11/10/2016.
 */
public class DataService {

    private static DataService mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    public static DataService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataService(context);
        }
        return mInstance;
    }

    private DataService(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
