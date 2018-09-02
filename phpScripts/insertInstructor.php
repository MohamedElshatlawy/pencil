<?php

	$fname=$_GET['fname'];
	$lname=$_GET['lname'];
	$email=$_GET['email'];
	$password=$_GET['password'];
	$dep_id=$_GET['department_id'];
	
	
	require_once('dbConnect.php');
	
	$sql = "INSERT INTO faculty_member (fname,lname, email,password,department_id)
VALUES ('$fname', '$lname', '$email','$password','$dep_id')";

	if (mysqli_query($con, $sql)) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . mysqli_error($con);
}

mysqli_close($con);