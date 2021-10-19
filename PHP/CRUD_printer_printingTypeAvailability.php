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
$tempClass =new stdClass;
$availableClass =new stdClass;
		
//Testing 

//$action = "read";
//$data = '{"printer_id":"4"}';


//If action is read 
if($action =="read"){
	$dps = json_decode($data);

		//Fetch from image printing pref
		$select_image_printing_setting_mysql_qry = "select * from image_printing_setting where printer_id like '$dps->printer_id';";
		$select_image_printing_setting_mysql_qry_result = mysqli_query($conn ,$select_image_printing_setting_mysql_qry);
		if(mysqli_num_rows($select_image_printing_setting_mysql_qry_result) > 0){	
	
			$row1 = $select_image_printing_setting_mysql_qry_result->fetch_object();
			if($row1->img_print_pref_json==="default"){
			     $availableClass->img_available="No";
			}else{
		    	$tempClass=json_decode($row1->img_print_pref_json);
		    	$availableClass->img_available=$tempClass->available;
			}
		}else{
			$error =1 ;
		}
		
		//Fetch from doc printing pref
		$select_document_printing_setting_mysql_qry = "select * from document_printing_setting where printer_id like '$dps->printer_id';";
		$select_document_printing_setting_mysql_qry_result = mysqli_query($conn ,$select_document_printing_setting_mysql_qry);
		if(mysqli_num_rows($select_document_printing_setting_mysql_qry_result) > 0){	
	
			$row2 = $select_document_printing_setting_mysql_qry_result->fetch_object();
			
			if($row2->doc_print_pref_json==="default"){
			     $availableClass->doc_available="No";
			}else{
			    $tempClass=json_decode($row2->doc_print_pref_json);
			    $availableClass->doc_available=$tempClass->available;
			}
		}else{
			$error =1 ;
		}
		

}

if($error ==0){
	$response->message="Success";
	$response->data=json_encode($availableClass);

} else{
	$response->message="Fail";
    $response->data=null;
}

echo json_encode($response);
$conn->close();
?>