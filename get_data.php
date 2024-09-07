<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

// Database connection settings
$host = "localhost";
$username = "id21090089_admin";
$password = "En@mul#19!";
$database = "id21090089_participantmanager";

// Create a connection to the database
$con = mysqli_connect($host, $username, $password, $database);

if (mysqli_connect_errno()) {
    die("Couldn't connect to Database!<br>" . mysqli_connect_error());
} else {
    // Connected to the database successfully

    // Perform a SELECT query to retrieve data from the 'participants' table
    $selectSql = "SELECT id, name, gender, status, date_of_birth, date_of_death, occupation, hobbies, upload, modifydate FROM participants";
    $result = mysqli_query($con, $selectSql);

    if (!$result) {
        die("Query failed: " . mysqli_error($con));
    }

    // Create an array to store the retrieved data
    $data = array();

    while ($item = mysqli_fetch_assoc($result)) {
        $userInfo = array(
            "id" => $item['id'],
            "name" => $item['name'],
            "gender" => $item['gender'],
            "status" => $item['status'],
            "date_of_birth" => $item['date_of_birth'],
            "date_of_death" => $item['date_of_death'],
            "occupation" => $item['occupation'],
            "hobbies" => $item['hobbies'],
            "upload" => $item['upload'],
            "modifydate" => $item['modifydate']
        );

        $data[] = $userInfo;
    }

    // Update the 'upload' column value to 1
    updateUploadStatus();


    // Set the response content type to JSON
    header('Content-Type: application/json; charset=utf-8');

    // Encode the data as JSON and send it in the response
    echo json_encode($data, JSON_PRETTY_PRINT);
}

// Function to update the 'upload' column to 1
function updateUploadStatus() {
    global $host, $username, $password, $database; // Add global variables
    $con = mysqli_connect($host, $username, $password, $database); // Re-establish the database connection
    $updateSql = "UPDATE participants SET upload = 1";
    $updateResult = mysqli_query($con, $updateSql);
    if (!$updateResult) {
        die("Update query failed: " . mysqli_error($con));
    }
    mysqli_close($con); // Close the connection
}
?>
