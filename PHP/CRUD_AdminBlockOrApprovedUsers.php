<?php 
require "conn.php";
require "Response.php";

$conn = mysqli_connect("localhost","id16288491_jameswan","\Pc[SL9}[g~~!V1P","id16288491_fyp2");

if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  exit();  
}

// POST method here
if(isset($_POST["action"])){
    $action = $_POST["action"];
}

// POST Data
if(isset($_POST["data"])){
    $data = $_POST["data"];
}

//Declare Class
$response= new Response();

//Initialize Variable:


//Testing 
//$printer_id = "6";
//$action="update";
//$data='{"user_id":"1","status":"Approved"}';
//$data = '{"order_id":"1","cust_id":"15","order_status":"Accepted","printer_id":"6"}';
//$action = 'update';
//$printer_id=$data;


if($action==="update"){
	$data_OBJ=json_decode($data);
	$update_user_status_qry = "UPDATE user SET status = '$data_OBJ->status' WHERE user_id = '$data_OBJ->user_id';";
	$update_user_status_result=mysqli_query($conn,$update_user_status_qry);	
	
}
//
//READ 
//
$select_user_mysql_qry = "select * from user where user_role != 'admin';";
$select_user_mysql_qry_result = mysqli_query($conn ,$select_user_mysql_qry);
if(mysqli_num_rows($select_user_mysql_qry_result) > 0){
	$user_array = array();
	$tempArray = array();
	while($row = $select_user_mysql_qry_result->fetch_object())
	{
		if($row->user_role=="customer"){
		$select_customer_mysql_qry = "select * from customer where user_id like '$row->user_id';";
		$select_customer_mysql_qry_result = mysqli_query($conn ,$select_customer_mysql_qry);
		$row->customer = $select_customer_mysql_qry_result->fetch_object();
		}else{
			$select_printer_mysql_qry = "select * from printer where user_id like '$row->user_id';";
			$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
			$row->printer = $select_printer_mysql_qry_result->fetch_object();
		}
		$tempArray=$row;
		array_push($user_array,$tempArray);
	}
	$response->message="Success";
    $response->data=json_encode($user_array);
			

} else{
		$response->message="Fail";
		$response->data=null;
}
		echo json_encode($response);
		

$conn->close();
?>