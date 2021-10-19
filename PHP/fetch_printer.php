<?php 
include 'Response.php';
include 'conn.php';


$conn = mysqli_connect("localhost","id16288491_jameswan","\Pc[SL9}[g~~!V1P","id16288491_fyp2");

$GLOBALS['conn'] = $conn;
if (mysqli_connect_errno()) {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  exit();  
}

// Declare Class variable
$response= new Response();


// POST method here


//Variables declared and Initialized
$error=0;


//Testing 


$select_printer_mysql_qry = "select * from printer;";
$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
if(mysqli_num_rows($select_printer_mysql_qry_result) > 0){	
	
	
	$Printer_Array = array();
	$tempArray = array();
	
	while($row = $select_printer_mysql_qry_result->fetch_object()){
	    
	    $printer_id= $row->printer_id;
	    if(VerificationOfFillUpEverything($printer_id)==1){
	         	$select_user_mysql_qry = "select * from user where user_id like '$row->user_id';";
	        	$select_user_mysql_qry_result = mysqli_query($conn ,$select_user_mysql_qry);
	        	$row->user=$select_user_mysql_qry_result->fetch_object();
		        $tempArray = $row;
	             array_push($Printer_Array, $tempArray);
	    }
	
	}
	

} else{
    $error=1;
}

function VerificationOfFillUpEverything(int $printer_id) {
   $valid =1;
   $error=0;
   if((VerificationOfShippingOption($printer_id))===1){
       
   }else{
       $error=1;
   }
   
   if((VerificationOfPaymentSetting($printer_id))===1){
      
   }else{
       $error=1;
   }
   
   if((VerificationOfPrintingSetting($printer_id)===1)){
       
   }else{
       $error=1;
   }
   
   if($error===0){
       $valid=1;
   }else{
     
       $valid=0;
   }
  return $valid;
}

function VerificationOfShippingOption(int $printer_id){
    $error=0;
    $valid=1;
     $select_shipping_option_mysql_qry = "select * from shipping_option where printer_id like $printer_id;";
       $select_shipping_option_mysql_qry_result = mysqli_query($GLOBALS['conn']  ,$select_shipping_option_mysql_qry);
       if(mysqli_num_rows($select_shipping_option_mysql_qry_result) > 0){	
           while($row = $select_shipping_option_mysql_qry_result->fetch_object()){
	            if(($row->delivery_avail==="default")&&($row->self_pick_up_avail==="default")){
	                $error=1;
	            }
	            
	        }
           
       }
    if($error===0){
        $valid=1;
    }else{
        $valid=0;
    }
    return $valid;
}

function VerificationOfPaymentSetting(int $printer_id){
    $valid=1;
    $error=0;
    $select_payment_setting_mysql_qry = "select * from payment_setting where printer_id like $printer_id;";
       $select_payment_setting_mysql_qry_result = mysqli_query($GLOBALS['conn']  ,$select_payment_setting_mysql_qry);
       if(mysqli_num_rows($select_payment_setting_mysql_qry_result) > 0){	
           while($row = $select_payment_setting_mysql_qry_result->fetch_object()){
	            if(($row->online_payment==="default")&&($row->COD==="default")){
	                $error=1;
	            }
	            
	        }
           
       }else{
           $error=1;
       }
     
     if($error===0){
         $valid=1;
     }else{
         $valid=0;
     }
    
    return $valid;
}

function VerificationOfPrintingSetting(int $printer_id){
    $valid=0;
    $error=0;
    
    $doc_print_pref_json="";
    $img_print_pref_json="";
    
     $select_document_printing_setting_mysql_qry= "select * from document_printing_setting where printer_id like $printer_id;";
     $select_document_printing_setting_mysql_qry_result = mysqli_query($GLOBALS['conn'],$select_document_printing_setting_mysql_qry);
       if(mysqli_num_rows($select_document_printing_setting_mysql_qry_result) > 0){	
           while($row = $select_document_printing_setting_mysql_qry_result->fetch_object()){
	           $doc_print_pref_json = $row->doc_print_pref_json;
	        }
           
       }else{
           $error=1;
       }
    
      $select_image_printing_setting_mysql_qry= "select * from image_printing_setting where printer_id like $printer_id;";
     $select_image_printing_setting_mysql_qry_result = mysqli_query($GLOBALS['conn'],$select_image_printing_setting_mysql_qry);
       if(mysqli_num_rows($select_image_printing_setting_mysql_qry_result) > 0){	
           while($row = $select_image_printing_setting_mysql_qry_result->fetch_object()){
	           $img_print_pref_json = $row->img_print_pref_json;
	        }
       }else{
           $error=1;
       }
       
       if(($doc_print_pref_json!="default")||($img_print_pref_json!="default")){
       }else{
           $error=1;
       }
       
       if($error===0){
           $valid=1;
       }else{
           $valid=0;
       }
       return $valid;
}

if($Printer_Array==null){
    $error=1;
}

if($error==0){
    $response->message="Success";
    $response->data=json_encode($Printer_Array);
}else{
    $response->message="Fail";
    $response->data=null;

}
echo json_encode($response);
$conn->close();
?>