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

//Testing 
//$printer_id = "6";
//$action="read";
//$data='{"printer_id":"4"}';
//$data = '{"order_id":"1","cust_id":"15","order_status":"Accepted","printer_id":"6"}';
//$action = 'update';
//$printer_id=$data;
//
//UPDATE PART 
//
if($action==="update"){
}	
//
//READ PART
//
if($action==="read"){
		$dataOBJ = json_decode($data);
//SELECT ORDER 
$select_orders_qry = "select * from orders where printer_id like '$dataOBJ->printer_id';";
$select_orders_result = mysqli_query($conn ,$select_orders_qry);
if(mysqli_num_rows($select_orders_result) > 0){
	$orders_Array = array();
	$tempArray = array();
	$temp_Array = array();
	while($orders_OBJ = $select_orders_result->fetch_object()){
				
		//SELECT ORDER ADDRESS 
		$select_orders_address_qry = "select * from order_address where order_id like '$orders_OBJ->order_id';";
		$select_orders_address_result = mysqli_query($conn ,$select_orders_address_qry);
		$orders_OBJ->order_address = $select_orders_address_result->fetch_object();
				
		//SELECT PAYMENT 
		$select_payment_qry = "select * from payment where order_id like '$orders_OBJ->order_id';";
		$select_payment_result = mysqli_query($conn ,$select_payment_qry);
		$orders_OBJ->payment = $select_payment_result->fetch_object();
					
		//SELECT PREPARATION STATUS
		$select_preparation_status_qry = "select * from preparation_status where order_id like '$orders_OBJ->order_id';";
		$select_preparation_status_result = mysqli_query($conn ,$select_preparation_status_qry);
		$orders_OBJ->preparation_status=$select_preparation_status_result->fetch_object();
		
		//SELECT CUSTOMER
		$select_customer_qry = "select * from customer where cust_id like '$orders_OBJ->cust_id';";
		$select_customer_result = mysqli_query($conn ,$select_customer_qry);
		$orders_OBJ->customer = $select_customer_result->fetch_object();
		
		//SELECT USER AND PUT IT IN CUSTOMER
		$user_id = $orders_OBJ->customer->user_id;
		$select_user_qry = "select * from user where user_id like '$user_id';";
		$select_user_result = mysqli_query($conn ,$select_user_qry);
		$orders_OBJ->customer->user=$select_user_result->fetch_object();
				
		//SELECT printer
		$select_printer_qry = "select * from printer where printer_id like '$orders_OBJ->printer_id';";
		$select_printer_result = mysqli_query($conn ,$select_printer_qry);
		$orders_OBJ->printer = $select_printer_result->fetch_object();
		
				
		//SELECT USER AND PUT IT IN printer
		$user_id = $orders_OBJ->printer->user_id;
		$select_user_qry = "select * from user where user_id like '$user_id';";
		$select_user_result = mysqli_query($conn ,$select_user_qry);
		$orders_OBJ->printer->user=$select_user_result->fetch_object();
			
				
		//SELECT SUB ORDERS - LIST
		$select_sub_orders_qry = "select * from sub_orders where order_id like '$orders_OBJ->order_id';";
		$select_sub_orders_result = mysqli_query($conn ,$select_sub_orders_qry);
		$sub_orders_Array = array();
		$temp2Array = array();
		while($sub_orders_OBJ =$select_sub_orders_result->fetch_object()){
			//SELECT PPP  FOR SUB ORDER
			$select_ppp_qry = "select * from product_printing_preferences where sub_order_id like '$sub_orders_OBJ->sub_order_id';";
			$select_ppp_result = mysqli_query($conn ,$select_ppp_qry);
			$sub_orders_OBJ->product_printing_preferences=$select_ppp_result->fetch_object();
			
			//SELECT FROM RESOURCES RECORD
			$select_rr_qry = "select * from  resources_record where sub_order_id like '$sub_orders_OBJ->sub_order_id';";
			$select_rr_result = mysqli_query($conn ,$select_rr_qry);
			$sub_orders_OBJ->resourcesRecord=$select_rr_result->fetch_object();
					
			$temp2Array = $sub_orders_OBJ;
			array_push($sub_orders_Array, $temp2Array);
			}
			$orders_OBJ->sub_orders =$sub_orders_Array;
				
				
			$tempArray = $orders_OBJ;
			array_push($orders_Array, $tempArray);
		}
		
	$response->message =  "Success";
		$response->data = json_encode($orders_Array);
		echo json_encode($response);
			
	} else{
		$response->message = "Fail";
		$response->data = null;
		echo json_encode($response);
	}	
}
		

$conn->close();
?>