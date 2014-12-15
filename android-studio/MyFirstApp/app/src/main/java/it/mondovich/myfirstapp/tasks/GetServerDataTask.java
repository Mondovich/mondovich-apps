package it.mondovich.myfirstapp.tasks;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import it.mondovich.myfirstapp.DisplayMessageActivity;
import it.mondovich.myfirstapp.MainActivity;
import it.mondovich.myfirstapp.R;

/**
 * Created by RaimondoS on 15/12/2014.
 */
public class GetServerDataTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "GetServerDataTask";

    private Activity activity;
    private ProgressDialog dialog;
    private String json, error;

    public GetServerDataTask(Activity activity) {
        this.activity = activity;
        dialog = new ProgressDialog(activity);
    }

    @Override
    protected void onPreExecute() {
        //Start Progress Dialog (Message)

        dialog.setMessage(activity.getString(R.string.please_wait));
        dialog.show();

        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        InputStream is = null;
        // Send data
        try
        {
            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            json = readIt(is);

            JSONObject jsonObject = new JSONObject(json);
        }
        catch(Exception ex)
        {
            error = ex.getMessage();
        }
        finally
        {
            try
            {

                is.close();
            }

            catch(Exception ex) {}
        }

        return json;
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        String line = null;

        // Read Server Response
        while((line = reader.readLine()) != null)
        {
            // Append server response in string
            sb.append(line + "");
        };
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {

        // Close progress dialog
        dialog.dismiss();

        Intent intent = new Intent(activity, DisplayMessageActivity.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, json);
        activity.startActivity(intent);

        super.onPostExecute(s);
    }
}
