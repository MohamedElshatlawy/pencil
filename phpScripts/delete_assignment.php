<?php



	$source=$_POST['url'];
	require_once('dbConnect.php');
	if(unlink($source)){
	
		echo "success";
		$sql="DELETE FROM assighments WHERE assignment_url='$source'";
		mysqli_query($con,$sql);
	
	}else{
		echo"error";
	}
	/*
	$sql="DELETE FROM courses where id=$id;";
	
	if(mysqli_query($con,$sql)){
		
		echo "Course deleted successfully";
	}else{
		echo "Course Not deleted Try Again";
	}
	*/
	mysqli_close($con);
	