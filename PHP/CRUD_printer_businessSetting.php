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

//$action="create";
//$data ='{"printer_id":"4","delivery_time_start":"0200","delivery_time_end":"1230"}';

//$action ="update";
//$data=' {"delivery_time_setting":[{"delivery_time_id":"3","delivery_time_start":"0200","printer_id":"4","delivery_time_end":"1300","printer":{"printer_id":"4","user_id":"9","user":{"user_id":"9","username":"john doe","user_email":"johndoe@gmail.com","user_role":"printer","user_address":"177,Jalan Perak 10,Taman Bandar Baru Selatan,31900,Kampar,Perak","user_hp":"013-4456677","user_password":"fyp2021","status":"Approved","bank_references":"default"}}}],"delivery_zone":[{"dev_zone_id":"1","delivery_zone_dist":"6","printer_id":"4","printer":{"printer_id":"4","user_id":"9","user":{"user_id":"9","username":"john doe","user_email":"johndoe@gmail.com","user_role":"printer","user_address":"177,Jalan Perak 10,Taman Bandar Baru Selatan,31900,Kampar,Perak","user_hp":"013-4456677","user_password":"fyp2021","status":"Approved","bank_references":"default"}}}],"shipping_option":[{"ship_option_id":"2","delivery_avail":"Yes","self_pick_up_avail":"Yes","printer_id":"4","printer":{"printer_id":"4","user_id":"9","user":{"user_id":"9","username":"john doe","user_email":"johndoe@gmail.com","user_role":"printer","user_address":"177,Jalan Perak 10,Taman Bandar Baru Selatan,31900,Kampar,Perak","user_hp":"013-4456677","user_password":"fyp2021","status":"Approved","bank_references":"default"}}}],"advanced_feature_setting":[{"adv_feature_id":"4","detect_blurry":"Yes","detect_adult":"Yes","detect_size":"Yes","printer_id":"4","printer":{"printer_id":"4","user_id":"9","user":{"user_id":"9","username":"john doe","user_email":"johndoe@gmail.com","user_role":"printer","user_address":"177,Jalan Perak 10,Taman Bandar Baru Selatan,31900,Kampar,Perak","user_hp":"013-4456677","user_password":"fyp2021","status":"Approved","bank_references":"default"}}}],"paymentSetting":[{"payment_setting_id":"1","online_payment":"Yes","COD":"No","printer_id":"4","printer":{"printer_id":"4","user_id":"9","user":{"user_id":"9","username":"john doe","user_email":"johndoe@gmail.com","user_role":"printer","user_address":"177,Jalan Perak 10,Taman Bandar Baru Selatan,31900,Kampar,Perak","user_hp":"013-4456677","user_password":"fyp2021","status":"Approved","bank_references":"default"}}}]}
//';

//$action = "read";
//$data='{"printer_id":"1"}';

//If action is create
if($action==="create"){
}

//If action is Update
if($action==="update"){
	$dataOBJ=json_decode($data);
	$deliveryTimeOBJ=$dataOBJ->delivery_time_setting[0];
	$shippingOptionOBJ = $dataOBJ->shipping_option[0];
	$deliveryZoneOBJ= $dataOBJ->delivery_zone[0];
	$advanced_feature_settingOBJ = $dataOBJ->advanced_feature_setting[0];
	$payment_settingOBJ=$dataOBJ->paymentSetting[0];


	$update_deliveryTime_qry = "UPDATE delivery_time_setting SET delivery_time_end= '$deliveryTimeOBJ->delivery_time_end',delivery_time_start='$deliveryTimeOBJ->delivery_time_start' WHERE delivery_time_id like '$deliveryTimeOBJ->delivery_time_id';";
	$update_deliveryTime_result=mysqli_query($conn,$update_deliveryTime_qry);
	if(!($update_deliveryTime_result)){
		$error ="1";
	}

	$update_shippingOption_qry = "UPDATE shipping_option SET delivery_avail = '$shippingOptionOBJ->delivery_avail', self_pick_up_avail ='$shippingOptionOBJ->self_pick_up_avail' WHERE ship_option_id like '$shippingOptionOBJ->ship_option_id';";
	$update_shippingOption_result=mysqli_query($conn,$update_shippingOption_qry);	
	if(!($update_shippingOption_result)){
		$error="1";
	}
	
	$update_deliveryZone_qry = "UPDATE delivery_zone SET delivery_zone_dist= '$deliveryZoneOBJ->delivery_zone_dist' WHERE dev_zone_id like '$deliveryZoneOBJ->dev_zone_id';";
	$update_deliveryZone_result=mysqli_query($conn,$update_deliveryZone_qry);	
	if(!($update_deliveryZone_result)){
		$error="1";
	}
	
	$update_advanced_feature_setting_qry = "UPDATE advanced_feature_setting SET detect_blurry='$advanced_feature_settingOBJ->detect_blurry' , detect_adult='$advanced_feature_settingOBJ->detect_adult', detect_size='$advanced_feature_settingOBJ->detect_size' WHERE adv_feature_id like '$advanced_feature_settingOBJ->adv_feature_id';";
	$update_advanced_feature_setting_result=mysqli_query($conn,$update_advanced_feature_setting_qry);	
	if(!($update_advanced_feature_setting_result)){
		$error="1";
	}
	
	$update_paymentSetting_qry = "UPDATE payment_setting SET online_payment='$payment_settingOBJ->online_payment' ,COD='$payment_settingOBJ->COD' WHERE  payment_setting_id like '$payment_settingOBJ->payment_setting_id';";
	$update_paymentSetting_result=mysqli_query($conn,$update_paymentSetting_qry);	
	if(!($update_paymentSetting_result)){
		$error="1";
	}
	
	
	/* Create response 
	if($error ==='1'){
		$response->message = "Fail";
		$response->data=null;
	}else{
		$response->message = "Success";
		$response->data= null;
	}
	*/
	
	$action = "read";
	$myObj = new stdClass;
	$myObj->printer_id = $dataOBJ->delivery_time_setting[0]->printer_id;
	$data = json_encode($myObj);
}


//If action is read 

if($action==="read"){
$deliveryTimeOBJ = json_decode($data);


/*Delivery Time*/
$select_deliveryTime_mysql_qry = "select * from delivery_time_setting where printer_id like '$deliveryTimeOBJ->printer_id';";
$select_deliveryTime_mysql_qry_result = mysqli_query($conn ,$select_deliveryTime_mysql_qry);
if(mysqli_num_rows($select_deliveryTime_mysql_qry_result) > 0){	
	
	$deliveryTime_Array = array();
	$tempArray = array();

	
	while($row= $select_deliveryTime_mysql_qry_result->fetch_object()){
	
		
		//Fetech Printers data based on the cust id.
		$select_printer_mysql_qry = "select * from printer where printer_id like '$row->printer_id';";
		$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
		$row->printer=$select_printer_mysql_qry_result->fetch_object();
		
		$printer_user_id = $row->printer->user_id;
		
		//Fetch user data of printer based on printer id
		$select_user_printer_mysql_qry = "select * from user where user_id like '$printer_user_id';";
		$select_user_printer_mysql_qry_result = mysqli_query($conn ,$select_user_printer_mysql_qry);
		$row->printer->user=$select_user_printer_mysql_qry_result->fetch_object();
		
		
		$tempArray = $row;
	    array_push($deliveryTime_Array, $tempArray);
	}
	
	
	
} else{
	$error = "1";

	
}

/* Shipping Availability */
$shippingOptionOBJ = json_decode($data);
$select_shippingOption_mysql_qry = "select * from shipping_option where printer_id like '$shippingOptionOBJ->printer_id';";
$select_shippingOption_mysql_qry_result = mysqli_query($conn ,$select_shippingOption_mysql_qry);
if(mysqli_num_rows($select_shippingOption_mysql_qry_result) > 0){	
	
	$shippingOption_Array = array();
	$tempArray = array();
	
	while($row = $select_shippingOption_mysql_qry_result->fetch_object()){
	
		
		//Fetech Printers data based on the cust id.
		$select_printer_mysql_qry = "select * from printer where printer_id like '$row->printer_id';";
		$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
		$row->printer=$select_printer_mysql_qry_result->fetch_object();
		
		$printer_user_id = $row->printer->user_id;
		
		//Fetch user data of printer based on printer id
		$select_user_printer_mysql_qry = "select * from user where user_id like '$printer_user_id';";
		$select_user_printer_mysql_qry_result = mysqli_query($conn ,$select_user_printer_mysql_qry);
		$row->printer->user=$select_user_printer_mysql_qry_result->fetch_object();
		
		
		$tempArray = $row;
	    array_push($shippingOption_Array, $tempArray);
	}
	




} else{
	$error = "1";
}

/*Delivery Zone*/
$deliveryZoneOBJ = json_decode($data);
$select_deliveryZone_mysql_qry = "select * from delivery_zone where printer_id like '$deliveryZoneOBJ->printer_id';";
$select_deliveryZone_mysql_qry_result = mysqli_query($conn ,$select_deliveryZone_mysql_qry);
if(mysqli_num_rows($select_deliveryZone_mysql_qry_result) > 0){	
	
	$deliveryZone_Array = array();
	$tempArray = array();
	
	while($row = $select_deliveryZone_mysql_qry_result->fetch_object()){
	
		
		//Fetech Printers data based on the cust id.
		$select_printer_mysql_qry = "select * from printer where printer_id like '$row->printer_id';";
		$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
		$row->printer=$select_printer_mysql_qry_result->fetch_object();
		
		$printer_user_id = $row->printer->user_id;
		
		//Fetch user data of printer based on printer id
		$select_user_printer_mysql_qry = "select * from user where user_id like '$printer_user_id';";
		$select_user_printer_mysql_qry_result = mysqli_query($conn ,$select_user_printer_mysql_qry);
		$row->printer->user=$select_user_printer_mysql_qry_result->fetch_object();
		
		
		$tempArray = $row;
	    array_push($deliveryZone_Array, $tempArray);
	
	}
	

} else{
	$error ="1";
}

/* Advanced Functionalities */

$advanced_feature_settingOBJ = json_decode($data);
$select_advanced_feature_setting_mysql_qry = "select * from advanced_feature_setting where printer_id like '$advanced_feature_settingOBJ->printer_id';";
$select_advanced_feature_setting_mysql_qry_result = mysqli_query($conn ,$select_advanced_feature_setting_mysql_qry);
if(mysqli_num_rows($select_advanced_feature_setting_mysql_qry_result) > 0){	
	
	$advanced_feature_setting_Array = array();
	$tempArray = array();
	
	while($row = $select_advanced_feature_setting_mysql_qry_result->fetch_object()){
	
		
		//Fetech Printers data based on the cust id.
		$select_printer_mysql_qry = "select * from printer where printer_id like '$row->printer_id';";
		$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
		$row->printer=$select_printer_mysql_qry_result->fetch_object();
		
		$printer_user_id = $row->printer->user_id;
		
		//Fetch user data of printer based on printer id
		$select_user_printer_mysql_qry = "select * from user where user_id like '$printer_user_id';";
		$select_user_printer_mysql_qry_result = mysqli_query($conn ,$select_user_printer_mysql_qry);
		$row->printer->user=$select_user_printer_mysql_qry_result->fetch_object();
		
		
		$tempArray = $row;
	    array_push($advanced_feature_setting_Array, $tempArray);
	}
	

} else{
	$error = "1";
}
/* PaymentSetting*/
$payment_settingOBJ = json_decode($data);
$select_payment_setting_mysql_qry = "select * from payment_setting where printer_id like '$payment_settingOBJ->printer_id';";
$select_payment_setting_mysql_qry_result = mysqli_query($conn ,$select_payment_setting_mysql_qry);
if(mysqli_num_rows($select_payment_setting_mysql_qry_result) > 0){	
	
	$payment_setting_Array = array();
	$tempArray = array();
	
	while($row = $select_payment_setting_mysql_qry_result->fetch_object()){
	
		
		//Fetech Printers data based on the cust id.
		$select_printer_mysql_qry = "select * from printer where printer_id like '$row->printer_id';";
		$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
		$row->printer=$select_printer_mysql_qry_result->fetch_object();
		
		$printer_user_id = $row->printer->user_id;
		
		//Fetch user data of printer based on printer id
		$select_user_printer_mysql_qry = "select * from user where user_id like '$printer_user_id';";
		$select_user_printer_mysql_qry_result = mysqli_query($conn ,$select_user_printer_mysql_qry);
		$row->printer->user=$select_user_printer_mysql_qry_result->fetch_object();
		
		
		$tempArray = $row;
	    array_push($payment_setting_Array, $tempArray);
	}
	

} else{
	$error = "1";
}


/* Create response */
	if($error ==='1'){
		$response->message = "Fail";
		$response->data=null;
	}else{
		$response->message = "Success";
		
		$businessSetting= new stdClass;
		$businessSetting->delivery_time_setting = $deliveryTime_Array;
		$businessSetting->delivery_zone = $deliveryZone_Array;
		$businessSetting->shipping_option = $shippingOption_Array;
		$businessSetting->advanced_feature_setting=$advanced_feature_setting_Array;
		$businessSetting->paymentSetting=$payment_setting_Array;
		$response->data =json_encode($businessSetting);
		//echo $response->data;
	}

}


echo json_encode($response);
$conn->close();
?>