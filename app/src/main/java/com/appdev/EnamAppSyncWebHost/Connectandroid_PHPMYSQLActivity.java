package com.appdev.EnamAppSyncWebHost;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;

public class Connectandroid_PHPMYSQLActivity extends AppCompatActivity {

    Connection connect;
    String ConnectionResult = "";

    private Button goToViewParticipant;
    private Button exitButton;
    private Button BtnScrToolChild;
    private Button syncButton;
    private Button syncButtonDown;
    private Button fragmentHost;
    private ProgressBar progressBar;
 //   int progressPercentage = ...;


    private ParticipantModal participant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connectandroid_sql);

        goToViewParticipant = findViewById(R.id.idBtnList);
        BtnScrToolChild= findViewById(R.id.idBtnScrToolChild);
        exitButton = findViewById(R.id.idBtnExit);

        syncButton = findViewById(R.id.idBtnSync);
        syncButton.setVisibility(View.GONE);

        syncButtonDown =findViewById(R.id.idBtnSyncDownload);
        syncButtonDown.setVisibility(View.GONE);

        fragmentHost = findViewById(R.id.idBtnFragmentHost);
        progressBar = findViewById(R.id.progressBar);

        goToViewParticipant.setOnClickListener(View -> {
            startActivity(new Intent(this, ViewParticipantActivity.class));
        });

        BtnScrToolChild.setOnClickListener(View -> {
            startActivity(new Intent(this, EligibilityScrToolforChild.class));
        } );

        fragmentHost.setOnClickListener(View -> {
            startActivity(new Intent(this, OccuCatFragmentHost.class));
        });



        exitButton.setOnClickListener(View -> {
            finish();
        });

        // Create an instance of the DBHandler class
        DBHandler dbHandler = new DBHandler(this);

        // Update the progress bar with the calculated percentage
      //  progressBar.setProgress(progressPercentage);

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show a confirmation dialog
                showSyncConfirmationDialog();
            }
        });


        RequestQueue queue = Volley.newRequestQueue(this);

        syncButtonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://crystalloid-college.000webhostapp.com/apps/get_data.php";

                JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    SQLiteDatabase db = dbHandler.getWritableDatabase(); // Open the database

                                    for (int x = 0; x < response.length(); x++) {
                                        JSONObject jsonObject = response.getJSONObject(x);
                                        int participantId = jsonObject.getInt("id"); // Replace "id" with the actual key in your JSON
                                        String name = jsonObject.getString("name");
                                        String gender = jsonObject.getString("gender");
                                        String status = jsonObject.getString("status");
                                        String date_of_birth = jsonObject.getString("date_of_birth");
                                        String date_of_death = jsonObject.getString("date_of_death");
                                        String occupation = jsonObject.getString("occupation");
                                        String hobbies = jsonObject.getString("hobbies");

                                        // Get the modifydate from the server data
                                        String serverModifyDate = jsonObject.getString("modifydate");

                                        // Check if the participant already exists in the database
                                        if (dbHandler.isParticipantExists(participantId)) {
                                            // Participant exists, get the local modifydate
                                            String localModifyDate = dbHandler.getModifyDate(participantId);

                                            // Compare dates
                                            if (serverModifyDate.compareTo(localModifyDate) > 0) {
                                                // Server data is newer, update the record
                                                boolean updated = dbHandler.updateParticipantLocal(participantId, name, gender, status, date_of_birth, date_of_death, occupation, hobbies);

                                                if (updated) {
                                                    // Record updated successfully
                                                    dbHandler.setUploadValue(participantId, 1);
                                                }
                                            }
                                        } else {
                                            // Participant doesn't exist, add a new record
                                            long added = dbHandler.addParticipantLocal(participantId, name, gender, status, date_of_birth, date_of_death, occupation, hobbies);

                                            if (added != -1) {
                                                // Record added successfully
                                                dbHandler.setUploadValue(participantId, 1);
                                            }
                                        }
                                    }

                                    db.close(); // Close the database
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error, e.g., display an error message
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(arrayRequest);
            }
        });




    }

    private void showSyncConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Synchronize Data")
                .setMessage("Do you want to synchronize data to the server?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Start the synchronization process
                        startSynchronization();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel synchronization
                        dialog.dismiss();
                    }
                })
                .show();
    }



    private void startSynchronization() {
        // Check if internet connection is available
        if (!isInternetAvailable()) {
            showNoInternetAlert();
            return; // Stop further execution
        }

        // Show the progress bar
        progressBar.setVisibility(View.VISIBLE);

        // Create an instance of the DBHandler class
        DBHandler dbHandler = new DBHandler(this);

        // Call your existing synchronization logic here
        dbHandler.synchronizeDataWithServer(getApplicationContext());

        // Delayed action using Handler
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Hide the progress bar after 5 seconds
                progressBar.setVisibility(View.GONE);

                // Show an alert dialog indicating successful synchronization
                showSyncSuccessAlert();
            }
        }, 5000); // Delay of 5000 milliseconds (5 seconds)
    }
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showNoInternetAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection")
                .setMessage("Internet connection is not available for Data Sync.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the dialog (optional)
                        dialog.dismiss();
                    }
                })
                .show();
    }



    private void showSyncSuccessAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sync Successful")
                .setMessage("Data has been successfully synchronized.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the dialog (optional)
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
