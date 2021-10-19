<?php 

include 'Response.php';
include 'conn.php';

$conn = mysqli_connect("localhost","id16288491_jameswan","\Pc[SL9}[g~~!V1P","id16288491_fyp2");

if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  exit();  
}

// Declare Class variable
$response= new Response();


// POST method here
if(isset($_POST["action"])){
    $action = $_POST["action"];
}


// POST Data
if(isset($_POST["data"])){
    $data = $_POST["data"];
}

//Variables Declared
$error_occur="0";

// Testing
//For Login
//$action="login";
//$data = new stdclass();
//$data->user_email = "elonwan@gmail.com";
//$data->user_password = "fyp2021";
//$data = json_encode($data);

//For register
//$action="register";
//$data = new stdclass();
//$data->user_email = "Richardwan1998@gmail.com";
//$data->username = "RichardWan";
//$data->user_role = "printer";
//$data->status= "Approved";
//$data->user_address = "31900,Kampar";
//$data->user_password = "iloveyou2020";
//$data->user_hp = "012-4494472";
//$data->bank_references="1112111";
//$data = json_encode($data);


        
if($action==="login")
{
        $user= json_decode($data);
		$select_user_mysql_qry = "select * from user where user_email like '$user->user_email' and user_password like '$user->user_password'and status like 'Approved';";
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
            $response->data=json_encode($user_array[0]);
			

		} else{
			$response->message="Fail";
            $response->data=null;
		}
		
}
/*
	if the action is register
*/
else{	
		$user= json_decode($data);
		/*
			Check does the email duplicated in database
		*/
		$select_user_mysql_qry = "select * from user where user_email like '$user->user_email';";
		$select_user_mysql_qry_result = mysqli_query($conn ,$select_user_mysql_qry);
		$user_id = mysqli_insert_id($conn);
		
		if(mysqli_num_rows($select_user_mysql_qry_result) > 0){
			//Display that the email existed in database
			$response->message="Email Duplicated";
			$response->data=null;
		}
		else{
			$insert_user_mysql_qry ="insert into user(username,user_email,user_role,user_address,user_hp,user_password,status,bank_references)
									values('$user->username','$user->user_email','$user->user_role','$user->user_address','$user->user_hp','$user->user_password','$user->status','$user->bank_references')";
			if ($conn->query($insert_user_mysql_qry)=== TRUE) {
				$user_id=mysqli_insert_id($conn);
				$select_user_mysql_qry = "select * from user where user_id='$user_id';";
				$select_user_mysql_qry_result = mysqli_query($conn ,$select_user_mysql_qry);
				if(mysqli_num_rows($select_user_mysql_qry_result) > 0){
					$User_Array = array();
					$Temp_Array = array();	
					while($row = $select_user_mysql_qry_result->fetch_object()){
						$Temp_Array = $row;
						array_push($User_Array, $Temp_Array);
					}
					$user_json=json_encode($User_Array[0]);
				}
					
			}
			else{
				$error_occur = "1" ;
			}
			
			if(($user->user_role)==="customer"){
				$insert_customer_mysql_qry = "insert into customer(user_id)values('$user_id')";
				if ($conn->query($insert_customer_mysql_qry) === TRUE) {
				} else{
					$error_occur = "1";
				}
					
				
			}
			else{
				//Create Row - Table:Printer
				$insert_printer_mysql_qry = "insert into printer(user_id)values('$user_id')";
				if ($conn->query($insert_printer_mysql_qry) === TRUE) {
					$printer_id =mysqli_insert_id($conn);
				} else{
					$error_occur = "1";
				}
				
				//Create Row - Table: delivery_zone
				$insert_deliveryZone_mysql_qry ="insert into delivery_zone(delivery_zone_dist,printer_id)values('default','$printer_id')";
				if ($conn->query($insert_deliveryZone_mysql_qry)=== TRUE) {
				} else{
					$error_occur = "1";
				}
				
				//Create Row - Table: delivery_time
				$insert_deliveryTime_mysql_qry ="insert into delivery_time_setting(delivery_time_start,delivery_time_end,printer_id)values('default','default','$printer_id')";
				if ($conn->query($insert_deliveryTime_mysql_qry)=== TRUE) {
				}else{
					$error_occur = "1";
				}
				
				//Create Row - Table: shipping_option 
				$insert_shippingOption_mysql_qry ="insert into shipping_option(delivery_avail,self_pick_up_avail,printer_id)values('default','default','$printer_id')";
				if ($conn->query($insert_shippingOption_mysql_qry)=== TRUE) {
				}else{
					$error_occur = "1";
				}
				
				//Create Row - Table: advanced_feature_setting
				$insert_advanced_feature_setting_mysql_qry ="insert into advanced_feature_setting(detect_blurry,detect_size,detect_adult,printer_id)values('default','default','default','$printer_id')";
				if ($conn->query($insert_advanced_feature_setting_mysql_qry)=== TRUE) {
				}else{
					$error_occur = "1";
				}
				//Create Row - Table:payment_setting
				$insert_payment_setting_mysql_qry ="insert into payment_setting(online_payment,COD,printer_id)values('default','default','$printer_id')";
				if ($conn->query($insert_payment_setting_mysql_qry)=== TRUE) {
				}else{
					$error_occur = "1";
				}
				
				//Create Row-Table:image printing setting
				$insert_image_printing_setting_mysql_qry ="insert into image_printing_setting(img_print_pref_json,printer_id)values('default','$printer_id')";
				if ($conn->query($insert_image_printing_setting_mysql_qry)=== TRUE) {
				}else{
					$error_occur = "1";
				}
				
				//Create Row-Table:document_printing_setting
					$insert_document_printing_setting_mysql_qry ="insert into document_printing_setting(doc_print_pref_json,printer_id)values('default','$printer_id')";
				if ($conn->query($insert_document_printing_setting_mysql_qry)=== TRUE) {
				}else{
					$error_occur = "1";
				}
			}
			/*
				Check if any process in user registration Success or Fail
			*/
			if(($error_occur) ==="1"){
				$response->message="Fail";
				$response->data=null;
			}
			else{
				$response->message="Success";
				$response->data=$user_json;
			}
		
		}
}
		$conn->close();
		echo json_encode($response);
?>