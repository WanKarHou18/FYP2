<?php 
include 'Response.php';
include 'conn.php';

$conn = mysqli_connect("localhost","id16288491_jameswan","\Pc[SL9}[g~~!V1P","id16288491_fyp2");

if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  exit();  
}

/*
	Declare Class variable
*/
$response= new Response();

//POST method here
if(isset($_POST["action"])){
    $action = $_POST["action"];
}

// POST Data
if(isset($_POST["data"])){
    $data = $_POST["data"];
}

//Variables declared and Initialized

$error = "0";


//Testing 
//$action = "read";
//$data ='{"distance":"6"}';

//If action is read 
if($action==="read"){
	$dataOBJ=json_decode($data);
	
	$select_deliveryZone_mysql_qry = "select * from delivery_zone where delivery_zone_dist <= '$dataOBJ->distance';";
	$select_deliveryZone_mysql_qry_result = mysqli_query($conn ,$select_deliveryZone_mysql_qry);
	if(mysqli_num_rows($select_deliveryZone_mysql_qry_result) > 0){	
	
		$deliveryZone_Array = array();
		$tempArray = array();
		$dataOBJ = new stdClass();
	
		while($row = $select_deliveryZone_mysql_qry_result->fetch_object()){
	
		
			//Fetech Printers data based on the cust id.
			$select_printer_mysql_qry = "select * from printer where printer_id like '$row->printer_id';";
			$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
			$dataOBJ->printer=$select_printer_mysql_qry_result->fetch_object();
			
			$printer_user_id = $dataOBJ->printer->user_id;
			
			//Fetch user data of printer based on printer id
			$select_user_printer_mysql_qry = "select * from user where user_id like '$printer_user_id';";
			$select_user_printer_mysql_qry_result = mysqli_query($conn ,$select_user_printer_mysql_qry);
			$dataOBJ->printer->user=$select_user_printer_mysql_qry_result->fetch_object();
			
			
			$tempArray = $dataOBJ;
			array_push($deliveryZone_Array, $tempArray);
	
		}
	

	} else{
		$error ="1";
	}

	
	
	

}

/*Create Response */
if($error == "1"){
	$response->message = "Fail";
	$response->data = null;
}else{
	$response->message = "Success";
	$response->data = $deliveryZone_Array;
}


echo json_encode($response);
$conn->close();
?>