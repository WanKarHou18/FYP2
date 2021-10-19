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


//Testing 

//$action="update";
//$data ='{"prep_status_id":"28","prep_status":"Accepted"}';

//$action="read";
//$data ='{"prep_status_id":"28"}';

//If action is update
if($action=="update"){
	$Prep_OBJ= json_decode($data);

	
	$update_preparation_status_mysql_qry="UPDATE preparation_status SET prep_status = '$Prep_OBJ->prep_status' WHERE prep_status_id like '$Prep_OBJ->prep_status_id'";
	if ($conn->query($update_preparation_status_mysql_qry)=== TRUE) {
		$prep_status_id=mysqli_insert_id($conn);
		$error =0;
		$action = "read";
	}
	
	
}

if($action=="read"){
	//If action is read 
	$Prep_OBJ= json_decode($data);
	$select_preparation_status_mysql_qry = "select * from preparation_status where prep_status_id like '$Prep_OBJ->prep_status_id';";
	$select_preparation_status_mysql_qry_result = mysqli_query($conn ,$select_preparation_status_mysql_qry);
	if(mysqli_num_rows($select_preparation_status_mysql_qry_result) > 0){	
		$error =0;

	} else{
		$error =1;
	}
}

if($error ==0){
	$response->message="Success";
	$response->data=json_encode($select_preparation_status_mysql_qry_result->fetch_object());
	
}else{
	$response->message="Fail";
	$response->data=null;
}


echo json_encode($response);
$conn->close();
?>