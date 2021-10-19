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

//Testing
//$data='{"created_datetime":"default","cust_id":"1","delivery_mode":"Delivery","order_code_reference":"default","order_date":"30-7-2021","order_id":"default","order_status":"Pending","order_address":{"customer_address":"177,Jalan Perak 10,Taman Bandar Baru Selatan,31900,Kampar,Perak","distance":"0","order_add_id":"default","order_id":"default","printer_address":"177,Jalan Perak 10,Taman Bandar Baru Selatan,31900,Kampar,Perak"},"payment":{"order_id":"default","payment_cost":"default","payment_id":"default","payment_status":"Pending","payment_type":"COD"},"preparation_status":{"order_id":"default","prep_status_id":"default","prep_status":"default"},"printer_id":"4","sub_orders":[{"cost":"420.0","order_id":"default","product_printing_preferences":{"printing_pref_id":"default","printing_preferences":"{\"Copies\":\"300\",\"PaperSize\":\"3.5x5(3R)\",\"PaperType\":\"Glossy\",\"addOn\":\"RoundWoodenStand\"}","sub_order_id":"default"},"resourcesRecord":{"file_description":"default","file_name":"image:18","file_type":"Image","resources_id":"default","sub_order_id":"default"},"sub_order_id":"default"}],"time":"default"}';
//$action="create";

//$dataOBJ= json_decode($data);
//ECHO json_encode(json_decode($dataOBJ->sub_orders[0]->product_printing_preferences->printing_preferences));
 
//$data='{"cust_id":"1"}';
//$action="read";

//CREATE CLASS
$response = new Response();

//Initialize Variable
$error=0;

//
//GET CURRENT DATETIME 
//
date_default_timezone_set('Asia/Kuala_Lumpur');
$now = (new DateTime())->format('Y-m-d h:m:s');

if($action==="create"){
	

	$orders_OBJ = new stdClass();
	$orders_OBJ = json_decode($data);
	$order_address_OBJ = $orders_OBJ->order_address;
	$sub_orders_OBJ=$orders_OBJ->sub_orders;
	$payment_OBJ = $orders_OBJ->payment;
	$prep_status_OBJ=$orders_OBJ->preparation_status;
	
//
//INSERT INTO ORDERS 
//
$insert_orders_mysql_qry ="insert into orders(cust_id,printer_id,order_status,delivery_mode,order_date,time,created_datetime,order_code_reference)values('$orders_OBJ->cust_id','$orders_OBJ->printer_id','$orders_OBJ->order_status','$orders_OBJ->delivery_mode','$orders_OBJ->order_date','$orders_OBJ->time','$now','$orders_OBJ->order_code_reference')";
if ($conn->query($insert_orders_mysql_qry)=== TRUE) {
		$order_id=mysqli_insert_id($conn);
		}else{
			$error=1;
		}
//
//INSERT INTO PAYMENT 
//
$insert_payment_mysql_qry ="insert into payment(payment_cost,payment_type,payment_status,order_id)values('$payment_OBJ->payment_cost','$payment_OBJ->payment_type','$payment_OBJ->payment_status',$order_id)";
if ($conn->query($insert_payment_mysql_qry)=== TRUE) {
		$payment_id=mysqli_insert_id($conn);
		}else{
			$error=1;
		}
//
//INSERT INTO ORDER_ADDRESS
//
$insert_order_address_mysql_qry ="insert into order_address(customer_address,distance,printer_address,order_id)values('$order_address_OBJ->customer_address','$order_address_OBJ->distance','$order_address_OBJ->printer_address','$order_id')";
if ($conn->query($insert_order_address_mysql_qry)=== TRUE) {
		$order_add_id=mysqli_insert_id($conn);
		}else{
			$error=1;
		}
//
//INSERT INTO PREPARATION STATUS
//
$insert_preparation_status_mysql_qry ="insert into preparation_status(prep_status,order_id)values('$prep_status_OBJ->prep_status','$order_id')";
if ($conn->query($insert_preparation_status_mysql_qry)=== TRUE) {
		$prep_status_id=mysqli_insert_id($conn);
		}else{
			$error=1;
		}

//
//INSERT INTO SUB_ORDERS
//

$sub_orders_arr=(array) $sub_orders_OBJ;

for($i=0;$i<count($sub_orders_arr);$i++){	

		//
		//INSERT INTO SUB_ORDER
		//
		$sub_order_OBJ=$sub_orders_OBJ[$i];
		$insert_sub_order_mysql_qry ="INSERT INTO sub_orders(order_id,cost)
								values('$order_id','$sub_order_OBJ->cost')";
		if ($conn->query($insert_sub_order_mysql_qry)=== TRUE){
				$sub_order_id=mysqli_insert_id($conn);
			}else{
				$error=1;
			}
		//
		//INSERT INTO PPP FOR SUB_ORDER	
		//
		$ppp=json_encode(json_decode($sub_order_OBJ->product_printing_preferences->printing_preferences));
		$insert_ppp_mysql_qry ="INSERT INTO product_printing_preferences(printing_preferences,sub_order_id)values('$ppp','$sub_order_id')";
		if ($conn->query($insert_ppp_mysql_qry)=== TRUE){
				$printing_pref_id=mysqli_insert_id($conn);
			}else{
				$error=1;
			}
			
		//INSERT INTO Resources_Record FOR SUB_ORDER;
		
		$rr=$sub_order_OBJ->resourcesRecord;
		$ppp_json=json_encode($ppp);
		$insert_rr_mysql_qry ="INSERT INTO resources_record(file_name,file_type,file_description,sub_order_id)values('$rr->file_name','$rr->file_type','$rr->file_description','$sub_order_id')";
		if ($conn->query($insert_rr_mysql_qry)=== TRUE){
				$resources_id=mysqli_insert_id($conn);
			}else{
				$error=1;
			}
	
	}
	
	
	//Create Response
	if($error==0){
		$response->message = "Success";
			//Fetch all details of order that just inserted
			//SELECT ORDER 
			$select_orders_qry = "select * from orders where order_id like '$order_id';";
			$select_orders_result = mysqli_query($conn ,$select_orders_qry);
			if(mysqli_num_rows($select_orders_result) > 0){
				$orders_Array = array();
				$tempArray = array();
				$temp_Array = array();
				while($orders_OBJ = $select_orders_result->fetch_object()){
				
					//SELECT ORDER ADDRESS 
					$select_orders_address_qry = "select * from order_address where order_id like '$order_id';";
					$select_orders_address_result = mysqli_query($conn ,$select_orders_address_qry);
					$orders_OBJ->order_address = $select_orders_address_result->fetch_object();
						
					//SELECT PAYMENT 
					$select_payment_qry = "select * from payment where order_id like '$order_id';";
					$select_payment_result = mysqli_query($conn ,$select_payment_qry);
					$orders_OBJ->payment = $select_payment_result->fetch_object();
							
					//SELECT PREPARATION STATUS
					$select_preparation_status_qry = "select * from preparation_status where order_id  like '$order_id';";
					$select_preparation_status_result = mysqli_query($conn ,$select_preparation_status_qry);
					$orders_OBJ->preparation_status=$select_preparation_status_result->fetch_object();
						
					//SELECT printer
					$select_printer_qry = "select * from printer where printer_id like '$orders_OBJ->printer_id';";
					$select_printer_result = mysqli_query($conn ,$select_printer_qry);
					$orders_OBJ->printer = $select_printer_result->fetch_object();
				
						
					//SELECT USER AND PUT IT IN printer
					$user_id = $orders_OBJ->printer->user_id;
					$select_user_qry = "select * from user where user_id like '$user_id';";
					$select_user_result = mysqli_query($conn ,$select_user_qry);
					$orders_OBJ->printer->User=$select_user_result->fetch_object();
					
						
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
				
					
			}
			$response->data =json_encode($orders_Array);
			
	}else{
		$response->message = "Fail";
		$response->data=null;
	}
	echo json_encode($response);
}

//
//READ PART
//
if($action==="read"){
	$dataOBJ = json_decode($data);
//SELECT ORDER 
$select_orders_qry = "select * from orders where cust_id like '$dataOBJ->cust_id';";
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
?>
