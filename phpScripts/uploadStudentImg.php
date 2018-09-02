<?php

	if($_SERVER['REQUEST_METHOD']=='POST'){
		
		$image = $_POST['image'];
		$email=$_POST['email'];
		
		require_once('dbConnect.php');
		
		$sql ="SELECT id FROM student ORDER BY id ASC";
		
		$res = mysqli_query($con,$sql);
		
		$id = 0;
		
		while($row = mysqli_fetch_array($res)){
				$id = $row['id'];
		}
		
		$path = "imgs/$id.png";
		
		$actualpath = "http://192.168.1.2/proj/$path";
		
		$sql = "UPDATE student SET img_url ='$actualpath' WHERE email='$email'";
		
		if(mysqli_query($con,$sql)){
			file_put_contents($path,base64_decode($image));
			echo "Successfully Uploaded";
		}
		
		mysqli_close($con);
	}else{
		echo "Error";
	}