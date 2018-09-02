<?php



	$id=$_GET['department_id'];
	require_once('dbConnect.php');
	$sql="SELECT * FROM courses WHERE department_id ='$id'";
	
	$r=(mysqli_query($con,$sql));
	
	$result=array();
	while($row=mysqli_fetch_array($r)){
	array_push($result,array(
	
	"id"=>$row['id'],
	"name"=>$row['name'],
	"department_id"=>$row['department_id'],
	"course_url"=>$row['course_url'],
	"instructor_id"=>$row['instructor_id']

		));
	}
	echo json_encode(array('result'=>$result));
		
	
	mysqli_close($con);