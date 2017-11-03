package xyz.bnayagrawal.android.icsapp.internet;

/**
 * Created by binay on 11/3/2017.
 */

public interface iVolleyCallback {
    void onResponseReceived(String response_data);
    void onResponseError(String message);
}
