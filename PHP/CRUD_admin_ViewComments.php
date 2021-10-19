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


//Testing 

//$action="read";


//If action is read 

$select_comments_mysql_qry = "select * from comments ;";
$select_comments_mysql_qry_result = mysqli_query($conn ,$select_comments_mysql_qry);
if(mysqli_num_rows($select_comments_mysql_qry_result) > 0){	
	
	$Rating_Array = array();
	$tempArray = array();
	
	while($row = $select_comments_mysql_qry_result->fetch_object()){
		
		//Fetech Printers data based on the printer id.
		$select_printer_mysql_qry = "select * from printer where printer_id like '$row->printer_id';";
		$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
		$row->printer=$select_printer_mysql_qry_result->fetch_object();
		
		$printer_user_id = $row->printer->user_id;
		
		//Fetch user data of printer based on printer id
		$select_user_printer_mysql_qry = "select * from user where user_id like '$printer_user_id';";
		$select_user_printer_mysql_qry_result = mysqli_query($conn ,$select_user_printer_mysql_qry);
		$row->printer->user=$select_user_printer_mysql_qry_result->fetch_object();
		
		//Fetech customers data based on the cust id.
		$select_customers_mysql_qry = "select * from customer where cust_id like '$row->cust_id';";
		$select_customers_mysql_qry_result = mysqli_query($conn ,$select_customers_mysql_qry);
		$row->customer=$select_customers_mysql_qry_result->fetch_object();
		
		$cust_user_id = $row->customer->user_id;
		
		//Fetch user data of customer based on user id
		$select_user_cust_mysql_qry = "select * from user where user_id like '$cust_user_id';";
		$select_user_cust_mysql_qry_result = mysqli_query($conn ,$select_user_cust_mysql_qry);
		$row->customer->user=$select_user_cust_mysql_qry_result->fetch_object();
		
		$tempArray = $row;
	    array_push($Rating_Array, $tempArray);
	}
	

	$response->message="Success";
    $response->data=json_encode($Rating_Array);

} else{
	$response->message="Fail";
    $response->data=null;
}
echo json_encode($response);
$conn->close();
?>