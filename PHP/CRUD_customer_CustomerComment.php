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
//$data = '{"cust_id":"4","printer_id":"4","comment_content":"Services are shit!!!"}';

//$action="update";
//$data='{"comment_id":"2","cust_id":"4","printer_id":"4","comment_content":"Services bad!!"}';

//$action="read";
//$data='{"cust_id":"4","printer_id":"4"}';

//If action is create
if($action ==="create"){
	$comment_OBJ= json_decode($data);
	$insert_comment_mysql_qry ="insert into comments(cust_id,printer_id,comment_content)values('$comment_OBJ->cust_id','$comment_OBJ->printer_id','$comment_OBJ->comment_content')";
	if ($conn->query($insert_comment_mysql_qry)=== TRUE) {
		$comment_id=mysqli_insert_id($conn);
		}

//
	
}

//If action is update
if($action=="update"){
	$comment_OBJ= json_decode($data);
	$update_comment_mysql_qry="UPDATE comments SET comment_content = '$comment_OBJ->comment_content' WHERE cust_id like '$comment_OBJ->cust_id' and printer_id like '$comment_OBJ->printer_id' ";
	if ($conn->query($update_comment_mysql_qry)=== TRUE) {
			$comment_id=mysqli_insert_id($conn);
		}
}
//If action is read 
$comment_OBJ= json_decode($data);
$select_comment_mysql_qry = "select * from comments where printer_id like '$comment_OBJ->printer_id';";
$select_comment_mysql_qry_result = mysqli_query($conn ,$select_comment_mysql_qry);
if(mysqli_num_rows($select_comment_mysql_qry_result) > 0){	
	
	$Comment_Array = array();
	$tempArray = array();
	
	while($row = $select_comment_mysql_qry_result->fetch_object()){
		
		//Fetech customers data based on the cust id.
		$select_customers_mysql_qry = "select * from customer where cust_id like '$row->cust_id';";
		$select_customers_mysql_qry_result = mysqli_query($conn ,$select_customers_mysql_qry);
		$row->customer=$select_customers_mysql_qry_result->fetch_object();
		
		$cust_user_id = $row->customer->user_id;
		
		//Fetch user data of customer based on user id
		$select_user_cust_mysql_qry = "select * from user where user_id like '$cust_user_id';";
		$select_user_cust_mysql_qry_result = mysqli_query($conn ,$select_user_cust_mysql_qry);
		$row->customer->user=$select_user_cust_mysql_qry_result->fetch_object();
		
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
	    array_push($Comment_Array, $tempArray);
	}
	

	$response->message="Success";
    $response->data=json_encode($Comment_Array);

} else{
	$response->message="Fail";
    $response->data=null;
}
echo json_encode($response);
$conn->close();
?>