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
//$data='{"created_datetime":"2021-02-24 02:42:07","cust_id":"13","delivery_mode":"Self Pick","order_add_id":"1","order_code_reference":"12345","order_date":"2021-02-24 02:42:07","order_id":"default","order_status":"default","order_address":{"address":"Enter New Location","order_add_id":"default","distance":0},"payment_id":"1","prep_status_id":"1","printer_id":"6","sub_orders":[{"file_name":"image:18","file_type":"File","order_id":"default","printing_pref_id":"default","product_printing_preferences":{"printing_pref_id":"default","printing_preferences":"{\"ColorSelected\":\"color\",\"Edge\":\"longedge\",\"PL\":\"landscape\",\"PageRange\":\"\",\"SlidePerPage\":\"2\",\"Slided\":\"doubleslided\"}"},"sub_order_id":"default"}]}';
//$action="create";
 
//$data='15';
//$action="read";

//CREATE CLASS
$response = new Response();

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
//INSERT INTO PAYMENT 
//
$insert_payment_mysql_qry ="insert into payment(payment_cost,payment_type,payment_status,payment_complete_date)values('$payment_OBJ->payment_cost','$payment_OBJ->payment_type','$payment_OBJ->payment_status','$payment_OBJ->payment_complete_date')";
if ($conn->query($insert_payment_mysql_qry)=== TRUE) {
		$payment_id=mysqli_insert_id($conn);
		}
//
//INSERT INTO ORDER_ADDRESS
//
$insert_order_address_mysql_qry ="insert into order_address(customer_address,distance,printer_address)values('$order_address_OBJ->customer_address','$order_address_OBJ->distance','$order_address_OBJ->printer_address')";
if ($conn->query($insert_order_address_mysql_qry)=== TRUE) {
		$order_add_id=mysqli_insert_id($conn);
		}
//
//INSERT INTO PREPARATION STATUS
//
$insert_preparation_status_mysql_qry ="insert into preparation_status(prep_status)values('$prep_status_OBJ->prep_status')";
if ($conn->query($insert_preparation_status_mysql_qry)=== TRUE) {
		$prep_status_id=mysqli_insert_id($conn);
		}
//
//INSERT INTO ORDERS 
//
$insert_orders_mysql_qry ="insert into orders(cust_id,printer_id,prep_status_id,payment_id,order_add_id,order_status,delivery_mode,order_date,created_datetime,order_code_reference)values('$orders_OBJ->cust_id','$orders_OBJ->printer_id','$prep_status_id','$payment_id','$order_add_id','$orders_OBJ->order_status','$orders_OBJ->delivery_mode','$orders_OBJ->order_date','$now','$orders_OBJ->order_code_reference')";
if ($conn->query($insert_orders_mysql_qry)=== TRUE) {
		$order_id=mysqli_insert_id($conn);
		}
//
//INSERT INTO SUB_ORDERS
//

$sub_orders_arr=(array) $sub_orders_OBJ;

for($i=0;$i<count($sub_orders_arr);$i++){	
	//
	//INSERT INTO PPP FOR SUB_ORDER	
	//
	$ppp=$sub_orders_OBJ[$i]->product_printing_preferences->printing_preferences;
	$ppp_json=json_encode($ppp);
	$insert_ppp_mysql_qry ="INSERT INTO product_printing_preferences(printing_preferences)values('$ppp_json')";
	if ($conn->query($insert_ppp_mysql_qry)=== TRUE){
			$printing_pref_id=mysqli_insert_id($conn);
		}
	
	//
	//INSERT INTO SUB_ORDER
	//
	$sub_order_OBJ=$sub_orders_OBJ[$i];
	$insert_sub_order_mysql_qry ="INSERT INTO sub_orders(order_id,printing_pref_id,file_name,file_type)
							values('$order_id','$printing_pref_id','$sub_order_OBJ->file_name','$sub_order_OBJ->file_type')";
	if ($conn->query($insert_sub_order_mysql_qry)=== TRUE){
			$sub_order_id=mysqli_insert_id($conn);
		}

	
	}
}

//
//READ PART
//
if($action==="read"){
	$cust_id = $data;
//SELECT ORDER 
$select_orders_qry = "select * from orders where cust_id like '$cust_id';";
$select_orders_result = mysqli_query($conn ,$select_orders_qry);
if(mysqli_num_rows($select_orders_result) > 0){
	$orders_Array = array();
	$tempArray = array();
	$temp_Array = array();
	while($orders_OBJ = $select_orders_result->fetch_object()){
				
		//SELECT ORDER ADDRESS 
		$select_orders_address_qry = "select * from order_address where order_add_id like '$orders_OBJ->order_add_id';";
		$select_orders_address_result = mysqli_query($conn ,$select_orders_address_qry);
		$orders_OBJ->order_address = $select_orders_address_result->fetch_object();
				
		//SELECT PAYMENT 
		$select_payment_qry = "select * from payment where payment_id like '$orders_OBJ->payment_id';";
		$select_payment_result = mysqli_query($conn ,$select_payment_qry);
		$orders_OBJ->payment = $select_payment_result->fetch_object();
					
		//SELECT PREPARATION STATUS
		$select_preparation_status_qry = "select * from preparation_status where prep_status_id like '$orders_OBJ->prep_status_id';";
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
		$select_sub_orders_qry = "select * from sub_orders where sub_order_id like '$orders_OBJ->order_id';";
		$select_sub_orders_result = mysqli_query($conn ,$select_sub_orders_qry);
		$sub_orders_Array = array();
		$temp2Array = array();
		while($sub_orders_OBJ =$select_sub_orders_result->fetch_object()){
			//SELECT PPP  FOR SUB ORDER
			$select_ppp_qry = "select * from product_printing_preferences where printing_pref_id like '$sub_orders_OBJ->printing_pref_id';";
			$select_ppp_result = mysqli_query($conn ,$select_ppp_qry);
			$sub_orders_OBJ->product_printing_preferences=$select_ppp_result->fetch_object();
					
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
		$response->message =  "Fail";
		$response->data = null;
		echo json_encode($response);
	}
}
?>
