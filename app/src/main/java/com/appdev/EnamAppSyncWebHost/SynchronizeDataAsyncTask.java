package com.appdev.EnamAppSyncWebHost;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SynchronizeDataAsyncTask extends AsyncTask<JSONArray, Void, String> {

    private static final String SERVER_URL = "https://crystalloid-college.000webhostapp.com/apps/data.php"; // Replace with your server's API endpoint URL

    @Override
    protected String doInBackground(@NonNull JSONArray... jsonArrays) {
        if (jsonArrays.length == 0) {
            return null;
        }

        JSONArray jsonArray = jsonArrays[0];
        String response = "";

        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            os.write(jsonArray.toString().getBytes("UTF-8"));
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder responseBuilder = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    responseBuilder.append(inputLine);
                }
                in.close();

                response = responseBuilder.toString();
            } else {
                response = "Server returned response code: " + responseCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
            response = "Error: " + e.getMessage();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        // Handle the server response here
        if (result != null) {
            // You can update the UI or perform other actions based on the response
            // For example, you can log the response or show a Toast message
            Log.d("ServerResponse", result);
            // You can also show a Toast with the response message
            // Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        } else {
            // Handle the case where the response is null (an error occurred)
            Log.e("ServerResponse", "Response is null");
        }
    }


}

