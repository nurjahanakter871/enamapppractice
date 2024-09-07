<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

$con = mysqli_connect("localhost", "id21090089_admin", "En@mul#19!", "id21090089_participantmanager");

if (mysqli_connect_errno()) {
    echo "Couldn't connect to Database!<br>" . mysqli_connect_error();
} else {
    echo "Connected to the database successfully.<br>";

    $data = json_decode(file_get_contents('php://input'), true); // Read JSON data from the request body

    if (!empty($data)) {
        foreach ($data as $participant) {
            $id = isset($participant['id']) ? $participant['id'] : null;
            $name = isset($participant['name']) ? $participant['name'] : null;
            $gender = isset($participant['gender']) ? $participant['gender'] : null;
            $status = isset($participant['status']) ? $participant['status'] : null;
            $date_of_birth = isset($participant['date_of_birth']) ? $participant['date_of_birth'] : null;
            $date_of_death = isset($participant['date_of_death']) ? $participant['date_of_death'] : null;
            $occupation = isset($participant['occupation']) ? $participant['occupation'] : null;
            $hobbies = isset($participant['hobbies']) ? $participant['hobbies'] : null;

            // Check if the participant with the given ID already exists in the database
            $existingParticipantSql = "SELECT id FROM participants WHERE id = ?";
            $existingStmt = mysqli_prepare($con, $existingParticipantSql);
            mysqli_stmt_bind_param($existingStmt, "i", $id);
            mysqli_stmt_execute($existingStmt);
            mysqli_stmt_store_result($existingStmt);

            if (mysqli_stmt_num_rows($existingStmt) > 0) {
                // Update the existing participant's data
                $updateSql = "UPDATE participants SET name=?, gender=?, status=?, date_of_birth=?, date_of_death=?, occupation=?, hobbies=? WHERE id=?";
                $updateStmt = mysqli_prepare($con, $updateSql);
                mysqli_stmt_bind_param($updateStmt, "sssssssi", $name, $gender, $status, $date_of_birth, $date_of_death, $occupation, $hobbies, $id);
                mysqli_stmt_execute($updateStmt);
                echo "Data updated successfully!";
            } else {
                // Insert new participant data
                $insertSql = "INSERT INTO participants (id, name, gender, status, date_of_birth, date_of_death, occupation, hobbies) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                $insertStmt = mysqli_prepare($con, $insertSql);
                mysqli_stmt_bind_param($insertStmt, "isssssss", $id, $name, $gender, $status, $date_of_birth, $date_of_death, $occupation, $hobbies);
                mysqli_stmt_execute($insertStmt);
                echo "Data inserted successfully!";
            }
        }
    } else {
        echo "No data received.";
    }

    mysqli_close($con);
}
?>
