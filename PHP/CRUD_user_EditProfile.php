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

/*
    Declare Variable
 */
 $error =0;
 $message="";


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

//$action="update";


//$action="update";
//$data='{"user_id":"2","username":"Elon Wan","user_email":"elonwan@gmail.com","user_role":"customer","user_address":"176,Jln Bandar Baru,Kampar,Perak","user_hp":"012-5727853","user_password":"fyp2021","status":"Approved","bank_references":"default"}';
//$data='{"user_id":"3","username":"Jimmy Wan","user_email":"jimmywan@gmail.com","user_role":"printer","user_address":"172, Jln Bandar Baru,Kampar ,Perak","user_hp":"017-7113456","user_password":"fyp2021","status":"Approved","bank_references":"1112222333"}';

//If action is update
if($action=="update"){
	$User_OBJ= json_decode($data);
	//Check Email if it is duplicated
	$select_user_email_mysql_qry = "select * from user where user_email like '$User_OBJ->user_email' and user_id not like '$User_OBJ->user_id';";
	$select_user_email_mysql_qry_result = mysqli_query($conn ,$select_user_email_mysql_qry);
	if(mysqli_num_rows($select_user_email_mysql_qry_result) > 0){
		$message="Email Duplicated";
	}else{
		if(($User_OBJ->user_role) == "customer")	{
		$update_user_mysql_qry="UPDATE user SET username = '$User_OBJ->username' ,user_email='$User_OBJ->user_email',user_address='$User_OBJ->user_address', user_hp='$User_OBJ->user_hp',user_password ='$User_OBJ->user_password' WHERE user_id = '$User_OBJ->user_id'";
		if ($conn->query($update_user_mysql_qry)=== TRUE) {
				$user_id=mysqli_insert_id($conn);
			}
		}else{
		$update_user_mysql_qry="UPDATE user SET username = '$User_OBJ->username' ,user_email='$User_OBJ->user_email',user_address='$User_OBJ->user_address', user_hp='$User_OBJ->user_hp',user_password ='$User_OBJ->user_password', bank_references  = '$User_OBJ->bank_references' WHERE user_id = '$User_OBJ->user_id'";
		if ($conn->query($update_user_mysql_qry)=== TRUE) {
				$user_id=mysqli_insert_id($conn);
			}
		}
	}
}
//If action is read 
$User_OBJ= json_decode($data);
$select_user_mysql_qry = "select * from user where user_id like '$User_OBJ->user_id';";
$select_user_mysql_qry_result = mysqli_query($conn ,$select_user_mysql_qry);
if(mysqli_num_rows($select_user_mysql_qry_result) > 0){	
	
	$User_Array = array();
	$tempArray = array();
	
	while($row = $select_user_mysql_qry_result->fetch_object()){
		if($row->user_role=="customer"){
			$select_customer_mysql_qry = "select * from customer where user_id like '$row->user_id';";
			$select_customer_mysql_qry_result = mysqli_query($conn ,$select_customer_mysql_qry);
			$row->customer = $select_customer_mysql_qry_result->fetch_object();
		}else{
			$select_printer_mysql_qry = "select * from printer where user_id like '$row->user_id';";
			$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
			$row->printer = $select_printer_mysql_qry_result->fetch_object();
		}
		$tempArray = $row;
	    array_push($User_Array, $tempArray);
	}
	
	$response->message="Success";
    $response->data=json_encode($User_Array);
	

} else{
	$response->message="Fail";
    $response->data=null;
}
if($message=="Email Duplicated"){
	$response->message="Email Duplicated";
    $response->data=null;
}
echo json_encode($response);
$conn->close();
?>