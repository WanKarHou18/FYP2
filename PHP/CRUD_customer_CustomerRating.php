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
//$action="create";
//$data = '{"cust_id":"4","printer_id":"4","rating_value":"3"}';

//$action="update";
//$data='{"rating_id":"2","cust_id":"2","printer_id":"4","rating_value":"2"}';

 //$action="read";
 //$data='{"cust_id":"1","printer_id":"6"}';

//If action is create
if($action ==="create"){
	$rating_OBJ= json_decode($data);
	$insert_rating_mysql_qry ="insert into rating(cust_id,printer_id,rating_value)values('$rating_OBJ->cust_id','$rating_OBJ->printer_id','$rating_OBJ->rating_value')";
	if ($conn->query($insert_rating_mysql_qry)=== TRUE) {
		$rating_id=mysqli_insert_id($conn);
		}
}

//If action is update
if($action=="update"){
	$rating_OBJ= json_decode($data);
	$update_rating_mysql_qry="UPDATE rating SET rating_value = '$rating_OBJ->rating_value' WHERE printer_id = '$rating_OBJ->printer_id' and cust_id like '$rating_OBJ->cust_id'";
	if ($conn->query($update_rating_mysql_qry)=== TRUE) {
			$rating_id=mysqli_insert_id($conn);
		}
		$action="read";
}
//If action is read 
$rating_OBJ= json_decode($data);
$select_rating_mysql_qry = "select * from rating where printer_id like '$rating_OBJ->printer_id';";
$select_rating_mysql_qry_result = mysqli_query($conn ,$select_rating_mysql_qry);
if(mysqli_num_rows($select_rating_mysql_qry_result) > 0){	
	
	$Rating_Array = array();
	$tempArray = array();
	
	while($row = $select_rating_mysql_qry_result->fetch_object()){
		
		//Fetech customers data based on the cust id.
		$select_customers_mysql_qry = "select * from customer where cust_id like '$row->cust_id';";
		$select_customers_mysql_qry_result = mysqli_query($conn ,$select_customers_mysql_qry);
		$row->Customer=$select_customers_mysql_qry_result->fetch_object();
		
		$cust_user_id = $row->Customer->user_id;
		
		//Fetch user data of customer based on user id
		$select_user_cust_mysql_qry = "select * from user where user_id like '$cust_user_id';";
		$select_user_cust_mysql_qry_result = mysqli_query($conn ,$select_user_cust_mysql_qry);
		$row->Customer->User=$select_user_cust_mysql_qry_result->fetch_object();
		
		//Fetech Printers data based on the cust id.
		$select_printer_mysql_qry = "select * from printer where printer_id like '$row->printer_id';";
		$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
		$row->Printer=$select_printer_mysql_qry_result->fetch_object();
		
		$printer_user_id = $row->Printer->user_id;
		
		//Fetch user data of printer based on printer id
		$select_user_printer_mysql_qry = "select * from user where user_id like '$printer_user_id';";
		$select_user_printer_mysql_qry_result = mysqli_query($conn ,$select_user_printer_mysql_qry);
		$row->Printer->User=$select_user_printer_mysql_qry_result->fetch_object();
		
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