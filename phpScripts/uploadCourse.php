<?php

 $file_path = "courses/";
 $ins_id=$_POST['instructor_id'];
 $dep_id=$_POST['department_id'];
	require_once('dbConnect.php');
    $file_path = $file_path . basename( $_FILES['uploaded_file']['name']);
    if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
        echo "success";
		$name=basename($_FILES['uploaded_file']['name']);
	$sql ="INSERT INTO courses (name,department_id,course_url,instructor_id) VALUES('$name','$dep_id','$file_path','$ins_id')";
	if(mysqli_query($con, $sql)){
		//echo "done inserted";
		$myObj=new \stdClass();
	    $myObj->name = $name;
		$myObj->department_id = $dep_id;
		$myObj->path = $file_path;
		$myObj->instructor_id = $ins_id;
		
	    $myJSON = json_encode($myObj);
	echo $myJSON;
	
	}else{
	echo "error inserted";
	}
	}

	else{
        echo "fail upload";
    }
	
/*$c_name=$_POST['name'];

$dep_id=$_POST['dep_id'];
$course=$_POST['course'];

$ins_id=$_POST['ins_id'];

$encode_course = base64_encode($course);
 require_once('dbConnect.php');

$sql = "INSERT INTO courses (name,department_id,course,instructor_id)
VALUES ('$c_name', '$dep_id', '$encode_course','$ins_id')";
if (mysqli_query($con, $sql)) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . mysqli_error($con);
}
*/
mysqli_close($con);