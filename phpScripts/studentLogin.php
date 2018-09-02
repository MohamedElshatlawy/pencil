<?php


$email=$_POST['email'];
$pass=$_POST['password'];


require_once('dbConnect.php');

$sql="SELECT * FROM student WHERE email='$email' and password='$pass'";
$r=(mysqli_query($con,$sql));
if(mysqli_num_rows($r) <= 0){
	//echo json_encode(array('result'=>'Wrong'));
	$myObj=new \stdClass();
	$myObj->result = "WRONG";
	$myJSON = json_encode($myObj);
	echo $myJSON;
}else{
	$row=mysqli_fetch_array($r);
	$myObj=new \stdClass();
	$myObj->result = "OK";
	$myObj->id = $row['id'];
$myObj->fname = $row['fname'];
$myObj->lname = $row['lname'];
$myObj->email = $row['email'];
$myObj->password = $row['password'];
$myObj->img_url=$row['img_url'];
$myObj->code=$row['code'];

$myObj->department_id=$row['department_id'];
$depId=$row['department_id'];
$sql2="SELECT name FROM department WHERE id='$depId'";
$r=(mysqli_query($con,$sql2));
$row=mysqli_fetch_array($r);
$myObj->department=$row['name'];



//$myObj->city = "New York";

$myJSON = json_encode($myObj);

echo $myJSON;
}

mysqli_close($con);