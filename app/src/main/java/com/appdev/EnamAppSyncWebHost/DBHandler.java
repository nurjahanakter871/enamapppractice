package com.appdev.EnamAppSyncWebHost;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 7;
    private static final String DATABASE_NAME = "participantManager";
    private static final String TABLE_PARTICIPANT = "participants";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DOB = "date_of_birth";
    private static final String KEY_DATE_OF_DEATH = "date_of_death";

    private static final String KEY_OCCUPATION = "occupation";
    private static final String KEY_HOBBIES = "hobbies";
    private static final String KEY_UPLOAD = "upload";
    private static final String KEY_MODIFYDATE = "modifyDate";
    private static final String TAG = "DBHandler";

    public DBHandler(Context context) {

        super(context, getCustomDatabasePath(context), null, DATABASE_VERSION);
    }

    private static String getCustomDatabasePath(Context context) {
        File directory = context.getExternalFilesDir(null);
        String customDbPath = directory.getAbsolutePath() + "/participantdatabase.db";
        Log.d(TAG, "Custom Database Path: " + customDbPath);
        return customDbPath;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PARTICIPANT_TABLE = "CREATE TABLE " + TABLE_PARTICIPANT + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_GENDER + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_DOB + " DATE DEFAULT NULL,"
                + KEY_DATE_OF_DEATH + " DATE DEFAULT NULL,"
                + KEY_OCCUPATION + " TEXT,"
                + KEY_HOBBIES + " TEXT,"
                + KEY_UPLOAD +" INTEGER DEFAULT 1,"
                +KEY_MODIFYDATE +" DATE DEFAULT (DATETIME('now', 'localtime'))"+ ")"; // Set default value to current date and time
        db.execSQL(CREATE_PARTICIPANT_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANT);
        onCreate(db);
    }


    public void addParticipant(String name, String gender, String status, String date_of_birth, String date_of_death, String occupation, String hobbies) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_GENDER, gender);
        values.put(KEY_STATUS, status);
        values.put(KEY_DOB, date_of_birth);
        values.put(KEY_DATE_OF_DEATH, date_of_death);
        values.put(KEY_OCCUPATION, occupation);
        values.put(KEY_HOBBIES, hobbies);
        values.put(KEY_UPLOAD, 1); // SET UPLOAD 1 WHEN INSERTING A NEW ROW
        db.insert(TABLE_PARTICIPANT, null, values);
        db.close();
    }


    @SuppressLint("Range")
    public String getParticipantHobbies(int participantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {KEY_HOBBIES};
        String selection = KEY_ID + "=?";
        String[] selectionArgs = {String.valueOf(participantId)};
        Cursor cursor = db.query(TABLE_PARTICIPANT, columns, selection, selectionArgs, null, null, null);
        String hobbies = "";
        if (cursor.moveToFirst()) {
            hobbies = cursor.getString(cursor.getColumnIndex(KEY_HOBBIES));
        }
        cursor.close();
        return hobbies;
    }


    public List<ParticipantModal> readParticipants() {
        List<ParticipantModal> participantList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] columns = {KEY_ID, KEY_NAME, KEY_GENDER, KEY_STATUS, KEY_DOB, KEY_DATE_OF_DEATH, KEY_OCCUPATION, KEY_HOBBIES, KEY_UPLOAD};
            cursor = db.query(TABLE_PARTICIPANT, columns, null, null, null, null, null);

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    String gender = cursor.getString(2);
                    String status = cursor.getString(3);
                    String date_of_birth = cursor.getString(4);
                    String date_of_death = cursor.getString(5);
                    String occupation = cursor.getString(6);
                    String hobbies = cursor.getString(7);
                    int upload = cursor.getInt(8);
                    ParticipantModal participant = new ParticipantModal(id, name, gender, status, date_of_birth, date_of_death, occupation, hobbies, upload);
                    participantList.add(participant);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return participantList;
    }


    public void updateParticipant(int id, String newName, String newGender, String newStatus, String newDate_of_birth, String newDate_of_death, String newOccupation, String newHobbies) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, newName);
        values.put(KEY_GENDER, newGender);
        values.put(KEY_STATUS, newStatus);
        values.put(KEY_DOB, newDate_of_birth);
        values.put(KEY_DATE_OF_DEATH, newDate_of_death);
        values.put(KEY_OCCUPATION, newOccupation);
        values.put(KEY_HOBBIES, newHobbies);
        values.put(KEY_UPLOAD, 1); // SET UPLOAD 1 WHEN UPDATING A  ROW

        // Update row
        int result = db.update(TABLE_PARTICIPANT, values, KEY_ID + " = ?", new String[] { String.valueOf(id) });


    //    db.update(TABLE_PARTICIPANT, values, KEY_ID + "=?", new String[]{String.valueOf(id)});

        db.close();
    }
    public long addParticipantLocal(int participantId, String name, String gender, String status, String date_of_birth, String date_of_death, String occupation, String hobbies) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, participantId); // Set the participant ID
        values.put(KEY_NAME, name);
        values.put(KEY_GENDER, gender);
        values.put(KEY_STATUS, status);
        values.put(KEY_DOB, date_of_birth);
        values.put(KEY_DATE_OF_DEATH, date_of_death);
        values.put(KEY_OCCUPATION, occupation);
        values.put(KEY_HOBBIES, hobbies);

        // Insert the record and return the row ID if successful, -1 if there's an error
        long result = db.insert(TABLE_PARTICIPANT, null, values);
        db.close();

        return result;
    }



    public boolean updateParticipantLocal(int participantId, String name, String gender, String status, String date_of_birth, String date_of_death, String occupation, String hobbies ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_GENDER, gender);
        values.put(KEY_STATUS, status);
        values.put(KEY_DOB, date_of_birth);
        values.put(KEY_DATE_OF_DEATH, date_of_death);
        values.put(KEY_OCCUPATION, occupation);
        values.put(KEY_HOBBIES, hobbies);

        // Explicitly set the modifydate to the current timestamp
      //  values.put(KEY_MODIFYDATE, "CURRENT_TIMESTAMP");

        // Update the record where KEY_ID matches the participantId
        int numRowsAffected = db.update(TABLE_PARTICIPANT, values, KEY_ID + "=?", new String[]{String.valueOf(participantId)});
        db.close();

        // Return true if at least one row was updated, false otherwise
        return numRowsAffected > 0;
    }


    public ParticipantModal getParticipant(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PARTICIPANT, new String[]{KEY_ID, KEY_NAME, KEY_GENDER, KEY_STATUS, KEY_DOB, KEY_DATE_OF_DEATH, KEY_OCCUPATION, KEY_HOBBIES,KEY_UPLOAD },
                KEY_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(KEY_ID);
            int nameIndex = cursor.getColumnIndex(KEY_NAME);
            int genderIndex = cursor.getColumnIndex(KEY_GENDER);
            int statusIndex = cursor.getColumnIndex(KEY_STATUS);
            int dobIndex = cursor.getColumnIndex(KEY_DOB);
            int dateOfDeathIndex = cursor.getColumnIndex(KEY_DATE_OF_DEATH);
            int occupationIndex = cursor.getColumnIndex(KEY_OCCUPATION);
            int hobbiesIndex = cursor.getColumnIndex(KEY_HOBBIES);
            int uploadIndex = cursor.getColumnIndex(KEY_UPLOAD); // Adjust with your column name for 'upload'

            if (idIndex != -1 && nameIndex != -1 && genderIndex != -1 && statusIndex != -1 && dobIndex != -1 && dateOfDeathIndex != -1 && occupationIndex != -1 && hobbiesIndex != -1 && uploadIndex != -1 ) {
                ParticipantModal participant = new ParticipantModal(
                        cursor.getInt(idIndex),
                        cursor.getString(nameIndex),
                        cursor.getString(genderIndex),
                        cursor.getString(statusIndex),
                        cursor.getString(dobIndex),
                        cursor.getString(dateOfDeathIndex),
                        cursor.getString(occupationIndex),
                        cursor.getString(hobbiesIndex),
                        cursor.getInt(uploadIndex));

                cursor.close();
                return participant;
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return null; // Return null if participant data is not found or columns are not available
    }

   /* public List<ParticipantModal> getParticipantsByOccupation(String occupation) {
        List<ParticipantModal> participantList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PARTICIPANT,
                new String[]{*//* columns you need *//*},
                KEY_OCCUPATION + "=?",
                new String[]{occupation},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // Extract data from cursor and add to participantList
                // Example: participantList.add(new ParticipantModal(...));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return participantList;
    }*/

    public List<ParticipantModal> getParticipantsByOccupation(String occupation) {
        List<ParticipantModal> participantList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] columns = {KEY_ID, KEY_NAME, KEY_GENDER, KEY_STATUS, KEY_DOB, KEY_DATE_OF_DEATH, KEY_OCCUPATION, KEY_HOBBIES, KEY_UPLOAD};
            String selection = KEY_OCCUPATION + "=?";
            String[] selectionArgs = {occupation};

            cursor = db.query(TABLE_PARTICIPANT, columns, selection, selectionArgs, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                final int ID_INDEX = cursor.getColumnIndex(KEY_ID);
                final int NAME_INDEX = cursor.getColumnIndex(KEY_NAME);
                final int GENDER_INDEX = cursor.getColumnIndex(KEY_GENDER);
                final int STATUS_INDEX = cursor.getColumnIndex(KEY_STATUS);
                final int DOB_INDEX = cursor.getColumnIndex(KEY_DOB);
                final int DATE_OF_DEATH_INDEX = cursor.getColumnIndex(KEY_DATE_OF_DEATH);
                final int OCCUPATION_INDEX = cursor.getColumnIndex(KEY_OCCUPATION);
                final int HOBBIES_INDEX = cursor.getColumnIndex(KEY_HOBBIES);
                final int UPLOAD_INDEX = cursor.getColumnIndex(KEY_UPLOAD);

                do {
                    int id = cursor.getInt(ID_INDEX);
                    String name = cursor.getString(NAME_INDEX);
                    String gender = cursor.getString(GENDER_INDEX);
                    String status = cursor.getString(STATUS_INDEX);
                    String dateOfBirth = cursor.getString(DOB_INDEX);
                    String dateOfDeath = cursor.getString(DATE_OF_DEATH_INDEX);
                    String participantOccupation = cursor.getString(OCCUPATION_INDEX);
                    String hobbies = cursor.getString(HOBBIES_INDEX);
                    int upload = cursor.getInt(UPLOAD_INDEX);

                    ParticipantModal participant = new ParticipantModal(id, name, gender, status, dateOfBirth, dateOfDeath, participantOccupation, hobbies, upload);
                    participantList.add(participant);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return participantList;
    }



    /*public List<ParticipantModal> getParticipantsByOccupation(String occupation) {
        List<ParticipantModal> participantList = new ArrayList<>();

        try (SQLiteDatabase db = this.getReadableDatabase();
             Cursor cursor = db.query(TABLE_PARTICIPANT,
                     new String[]{*//* columns you need *//*},
                     KEY_OCCUPATION + "=?",
                     new String[]{occupation},
                     null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(KEY_ID);
                    String name = cursor.getString(KEY_NAME);
                    String gender = cursor.getString(KEY_GENDER);
                    ParticipantModal participant = new ParticipantModal(id, name, gender);
                    participantList.add(participant);
                } while (cursor.moveToNext());
            }
        }

        return participantList;
    }*/


    public void deleteParticipant(String participantName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARTICIPANT, "name=?", new String[]{participantName});
        db.close();
    }

    public boolean isParticipantExists(int participantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM participants WHERE id = ?";
        String[] selectionArgs = { String.valueOf(participantId) };

        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean exists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return exists;
    }

    public String getModifyDate(int participantId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT " + KEY_MODIFYDATE + " FROM " + TABLE_PARTICIPANT + " WHERE " + KEY_ID + " = " + participantId;

        Cursor cursor = db.rawQuery(selectQuery, null);

        String modifyDate = null;
        if (cursor != null && cursor.moveToFirst()) {
            modifyDate = cursor.getString(0); // Assuming KEY_MODIFYDATE is a String field
            cursor.close();
        }

        db.close();

        return modifyDate;
    }
    public void setUploadValue(int participantId, int uploadValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_UPLOAD, uploadValue);

        int rowsAffected = db.update(TABLE_PARTICIPANT, values, KEY_ID + "=?", new String[]{String.valueOf(participantId)});
        db.close();

        if (rowsAffected > 0) {
            // Update was successful
        } else {
            // Handle the case where the update didn't work
        }
    }



    // Define an interface for synchronization completion listener
    public interface OnSynchronizationCompleteListener {
        void onSynchronizationComplete();
    }

    private OnSynchronizationCompleteListener synchronizationCompleteListener;

    // Set the synchronization completion listener
    public void setOnSynchronizationCompleteListener(OnSynchronizationCompleteListener listener) {
        synchronizationCompleteListener = listener;
    }

    public void synchronizeDataWithServer(Context context) {
        List<ParticipantModal> localParticipants = readParticipantsWithUploadStatus(1); // Filter participants with upload status 1
        if (localParticipants.isEmpty()) {
            // Nothing to synchronize as there are no participants with upload status 1
            return;
        }


        JSONArray jsonArray = new JSONArray();
        for (ParticipantModal participant : localParticipants) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", participant.getId());
                jsonObject.put("name", participant.getName());
                jsonObject.put("gender", participant.getGender());
                jsonObject.put("status", participant.getStatus());
                // Add fields with null values
                if (participant.getDateOfBirth() != null) {
                    jsonObject.put("date_of_birth", participant.getDateOfBirth());
                } else {
                    jsonObject.put("date_of_birth", JSONObject.NULL);
                }
                jsonObject.put("date_of_death", participant.getDateOfDeath());
                jsonObject.put("occupation", participant.getOccupation());
                jsonObject.put("hobbies", participant.getHobbies());

                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();


            }
        }

        // Debug print statement to log the constructed JSON payload
        Log.d(TAG, "Constructed JSON Payload: " + jsonArray);


        // Start the SynchronizeDataAsyncTask
        new SynchronizeDataAsyncTask(context, jsonArray, localParticipants).execute();



        // After the synchronization process is completed
        if (synchronizationCompleteListener != null) {
            synchronizationCompleteListener.onSynchronizationComplete();
        }
    }

    public List<ParticipantModal> readParticipantsWithUploadStatus(int uploadStatus) {
        List<ParticipantModal> participantList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String[] columns = {KEY_ID, KEY_NAME, KEY_GENDER, KEY_STATUS, KEY_DOB, KEY_DATE_OF_DEATH, KEY_OCCUPATION, KEY_HOBBIES, KEY_UPLOAD};
            String selection = KEY_UPLOAD + " = ?";
            String[] selectionArgs = {String.valueOf(uploadStatus)};

            cursor = db.query(TABLE_PARTICIPANT, columns, selection, selectionArgs, null, null, null);

            while (cursor != null && cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String gender = cursor.getString(2);
                String status = cursor.getString(3);
                String date_of_birth = cursor.getString(4);
                String date_of_death = cursor.getString(5);
                String occupation = cursor.getString(6);
                String hobbies = cursor.getString(7);
                int upload = cursor.getInt(8);

                ParticipantModal participant = new ParticipantModal(id, name, gender, status, date_of_birth, date_of_death, occupation, hobbies, upload);
                participantList.add(participant);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return participantList;
    }



    // Define SynchronizeDataAsyncTask as a nested class within DBHandler
    private static class SynchronizeDataAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Context context;
        private final JSONArray jsonArray;

        private final List<ParticipantModal> participantsToUpdate;

        public SynchronizeDataAsyncTask(Context context, JSONArray jsonArray, List<ParticipantModal> participantsToUpdate) {
            this.context = context;
            this.jsonArray = jsonArray;
            this.participantsToUpdate = participantsToUpdate;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Perform data synchronization here
            // Create a Volley request to send the JSON array to the server
         //   String serverUrl = "http://192.168.0.102/data.php"; // Replace with your server's API endpoint
              String serverUrl = "https://crystalloid-college.000webhostapp.com/apps/data.php";
            StringRequest request = new StringRequest(Request.Method.POST, serverUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Handle server response here
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error here
                        }
                    }) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    return jsonArray.toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };

            // Add the request to the Volley queue
            VolleySingleton.getInstance(context).addToRequestQueue(request); // Implement VolleySingleton if not done already

            // After successful synchronization, update 'upload' to 2 for each participant

            DBHandler dbHelper = new DBHandler(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_UPLOAD, 2); // Set the 'upload' value to 2

            for (ParticipantModal participant : participantsToUpdate) {
                int id = participant.getId();
                db.update(TABLE_PARTICIPANT, values, KEY_ID + " = ?", new String[]{String.valueOf(id)});
            }

           db.close();
            return null;
        }



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

}