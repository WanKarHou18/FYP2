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
$dps_Array = array();
//Testing 

//$action="update";
//$data = '{"doc_printing_id":"1","doc_print_pref_json":"1"}';

//$action = "read";
//$data = '{"printer_id":"6"}';

//If action is create
/*
if($action=="create"){
	$insert_document_printing_setting_mysql_qry = "insert into document_printing_setting (doc_print_pref_json,doc_printing_avail)values('dps->doc_print_pref_json','dps->doc_printing_avail')";
	if ($conn->query($insert_document_printing_setting_mysql_qry)=== TRUE) {
		$doc_printing_id=mysqli_insert_id($conn);
	}
	
}*/

//If action is update
	if($action =="update"){

		$dps_OBJ= json_decode($data);
		$update_image_printing_setting_mysql_qry="UPDATE image_printing_setting SET img_print_pref_json ='$dps_OBJ->img_print_pref_json' WHERE printer_id ='$dps_OBJ->printer_id'";
		if ($conn->query($update_image_printing_setting_mysql_qry)=== TRUE) {
			$complaint_rec_id=mysqli_insert_id($conn);
		}
	}

//If action is read 
if($action =="read"){
	$dps = json_decode($data);
	
		
		$select_image_printing_setting_mysql_qry = "select * from image_printing_setting where printer_id like '$dps->printer_id'";
		$select_image_printing_setting_mysql_qry_result = mysqli_query($conn ,$select_image_printing_setting_mysql_qry);
		if(mysqli_num_rows($select_image_printing_setting_mysql_qry_result) > 0){	
	
			$dps_Array = array();
			$tempArray = array();
			$tempClass =new stdClass;
		
			while($row = $select_image_printing_setting_mysql_qry_result->fetch_object()){
			
			    if($row->img_print_pref_json=="default"){
			        $error=1;
			    }else{
			    	$tempArray = $row;
			    	$tempClass=json_decode($row->img_print_pref_json);
				    $row->img_print_pref_json=$tempClass;
				    array_push($dps_Array, $tempArray);
			    }
			}
		
		}else{
			$error =1 ;
		}
	

}

if($error ==0){
	$response->message="Success";
	if($dps_Array!=null){
		$response->data=json_encode($dps_Array);
	}else{
		 $response->data=null;
	}
} else{
	$response->message="Fail";
    $response->data=null;
}

echo json_encode($response);
$conn->close();
?>