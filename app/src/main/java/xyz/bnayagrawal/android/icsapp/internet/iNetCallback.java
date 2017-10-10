package xyz.bnayagrawal.android.icsapp.internet;

/**
 * Created by binay on 10/9/2017.
 */

import android.net.NetworkInfo;

public interface iNetCallback {
    /* connection states */
    interface iNetProgress {
        int ERROR = -1;
        int CONNECT_SUCCESS = 0;
        int GET_INPUT_STREAM_SUCCESS = 1;
        int PROCESS_INPUT_STREAM_IN_PROGRESS = 2;
        int PROCESS_INPUT_STREAM_SUCCESS = 3;
    }

    /* task result */
    void netUpdateResult(String result);

    /* Get the device's active network status in the form of a NetworkInfo object */
    NetworkInfo getActiveNetworkInfo();

    /**
     * task progress
     * @param progressCode a constant defined in iProgress
     * @param percentComplete progress between 0-100
     */
    void netOnProgressUpdate(int progressCode, int percentComplete);

    /* network operation finished */
    void netTaskComplete();
}
