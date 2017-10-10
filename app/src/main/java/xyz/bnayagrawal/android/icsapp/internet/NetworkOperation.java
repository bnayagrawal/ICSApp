package xyz.bnayagrawal.android.icsapp.internet;

/**
 * Created by binay on 10/9/2017.
 */

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkOperation {
    private iNetCallback netCallback;
    private Downloader downloader;

    public NetworkOperation(iNetCallback context) {
        this.netCallback = context;
    }

    public void beginOperation(Object... params) {
        cancelOperation();
        downloader = new Downloader();
        downloader.execute(params);
    }

    public void cancelOperation() {
        if(downloader != null) {
            downloader.cancel(true);
            downloader = null;
        }
    }

    private class Downloader extends AsyncTask<Object,Integer,Downloader.Result> {
        class Result {
            public String operationResult;
            public Exception exception;
            public Result(String operationResult) {
                this.operationResult = operationResult;
            }
            public Result(Exception exception) {
                this.exception = exception;
            }
        }

        @Override
        protected void onPreExecute() {
            if(netCallback != null) {
                NetworkInfo networkInfo = netCallback.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected() ||
                        (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
                    // If no connectivity, cancel task and update Callback with null data.
                    netCallback.netUpdateResult(null);
                    cancel(true);
                }
            }
        }

        @Override
        protected Result doInBackground(Object... data) {
            Result result = null;

            if (!isCancelled() && data != null && data.length > 1) {
                String url_string = data[0].toString();
                String post_data = data[1].toString();
                try {
                    URL url = new URL(url_string);
                    String resultString = download(url,post_data);
                    if (resultString != null) {
                        result = new Result(resultString);
                    } else {
                        throw new IOException("No response received.");
                    }
                } catch(Exception e) {
                    result = new Result(e);
                }
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values.length >= 2) {
                netCallback.netOnProgressUpdate(values[0], values[1]);
            }
        }

        @Override
        protected void onPostExecute(Result result) {
            if (result != null && netCallback != null) {
                if (result.exception != null) {
                    netCallback.netUpdateResult(result.exception.getMessage());
                } else if (result.operationResult != null) {
                    netCallback.netUpdateResult(result.operationResult);
                }
                netCallback.netTaskComplete();
            }
        }

        @Override
        protected void onCancelled(Result result) {
            //Do something
        }

        private String download(URL url, String post_data) throws IOException {
            InputStream stream = null;
            HttpURLConnection connection = null;
            String result = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(3000);
                connection.setConnectTimeout(10000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                //set some http header props
                connection.setRequestProperty("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
                connection.setRequestProperty("Accept-Encoding","gzip, deflate, br");
                connection.setRequestProperty("Accept-Language","en-US,en;q=0.8,hi;q=0.6");
                connection.setRequestProperty("Content-Type", "application/json");

                //set post data
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                connection.connect();
                publishProgress(iNetCallback.iNetProgress.CONNECT_SUCCESS);
                int responseCode = connection.getResponseCode();

                if (responseCode != HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream(),"UTF-8"));
                    String error_msg = "",ln;
                    while((ln=br.readLine()) != null)
                        error_msg += ln;
                    Log.i("Server response",error_msg);
                    throw new IOException("HTTP error code: " + responseCode);
                }

                // Retrieve the response body as an InputStream.
                stream = connection.getInputStream();
                publishProgress(iNetCallback.iNetProgress.GET_INPUT_STREAM_SUCCESS, 0);
                if (stream != null) {
                    // Converts Stream to String with max length of 500.
                    result = getTextStream(stream);
                    publishProgress(iNetCallback.iNetProgress.PROCESS_INPUT_STREAM_SUCCESS, 0);
                }
            } finally {
                // Close Stream and disconnect HTTPS connection.
                if (stream != null) {
                    stream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return result;
        }

        /* From InputStream to String */
        private String getTextStream(InputStream stream) throws IOException {
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            Reader in = new InputStreamReader(stream, "UTF-8");
            for (; ; ) {
                int rsz = in.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            publishProgress(iNetCallback.iNetProgress.PROCESS_INPUT_STREAM_IN_PROGRESS, 100);
            return out.toString();
        }
    }
}
