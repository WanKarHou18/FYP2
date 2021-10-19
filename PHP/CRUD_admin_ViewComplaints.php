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
$error =0;
$Complaint_Array = array();

//Testing 

//$action="read";
//$action="update";
//$data ='{"complaint_rec_id":"1","complaint_status":"read"}';

//If action is update
if($action=="update"){
	$complaint_OBJ= json_decode($data);
	$update_complaint_mysql_qry="UPDATE complaint_record SET complaint_status = '$complaint_OBJ->complaint_status' WHERE complaint_rec_id = '$complaint_OBJ->complaint_rec_id'";
	if ($conn->query($update_complaint_mysql_qry)=== TRUE) {
			$complaint_rec_id=mysqli_insert_id($conn);
			$error=0;
	}else{
		$error=1;
			
	}
}

//If action is read 
if($action==="read"){
		$select_complaint_mysql_qry = "select * from complaint_record where complaint_status like 'default' ;";
		$select_complaint_mysql_qry_result = mysqli_query($conn ,$select_complaint_mysql_qry);
		if(mysqli_num_rows($select_complaint_mysql_qry_result) > 0){	
	
			
			$tempArray = array();
		
			while($row = $select_complaint_mysql_qry_result->fetch_object()){
			
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
				array_push($Complaint_Array, $tempArray);
			}
			$error = 0;
		}else{
			$error =1;
		}
	}
		

if($error===0){
	$response->message="Success";
	if($Complaint_Array!=null){
		$response->data=json_encode($Complaint_Array);
	}else{
		$response->data =null;
	}

} else{
	$response->message="Fail";
    $response->data=null;
}
echo json_encode($response);
$conn->close();
?>