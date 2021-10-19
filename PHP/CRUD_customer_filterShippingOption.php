<?php 
include 'Response.php';
include 'conn.php';

$conn = mysqli_connect("localhost","id16288491_jameswan","\Pc[SL9}[g~~!V1P","id16288491_fyp2");
$GLOBALS['conn'] = $conn;
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
$confirm ="0";
$Available_Printer_Array = array();


//Testing 

//$action = "read";
//$data ='{"delivery_avail":"Yes","self_pick_up_avail":"Yes"}';
//$data ='{"delivery_avail":"No","self_pick_up_avail":"Yes"}';

//If action is read 
if($action==="read"){
	$dataOBJ=json_decode($data);
	$Available_Printer_Array = FetchAvailablePrinter();
	
	/* Delivery = Yes , Self Pick = Null*/
	if(($dataOBJ->delivery_avail == "Yes")&&($dataOBJ->self_pick_up_avail == "Yes")){
		
		if($Available_Printer_Array!=null){
    		for($i=0;$i<count($Available_Printer_Array);$i++){
    		    
    		    $printer = new stdClass();
    		    $available_printer = $Available_Printer_Array[$i];
    		    $avail_printer_id = $available_printer->printer_id;
    		   
    		    $select_shippingOption_mysql_qry = "select * from shipping_option where  delivery_avail like '$dataOBJ->delivery_avail' or self_pick_up_avail like '$dataOBJ->self_pick_up_avail' and printer_id like '$avail_printer_id';";
        		$select_shippingOption_mysql_qry_result = mysqli_query($conn ,$select_shippingOption_mysql_qry);
        
        		if(mysqli_num_rows($select_shippingOption_mysql_qry_result) > 0){	
        
        			$printer_Array = array();
        			$tempArray = array();
        			$data= new stdClass();
        
        		
        			while($row = $select_shippingOption_mysql_qry_result->fetch_object()){
        			
        				$printer_id = $row->printer_id;
        				
        					
        				//Fetech Printers data based on the cust id.
        				$select_printer_mysql_qry = "select * from printer where printer_id like '$printer_id';";
        				$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
        				$data=$select_printer_mysql_qry_result->fetch_object();
        		
        				$printer_user_id = $data->user_id;
        		
        				//Fetch user data of printer based on printer id
        				$select_user_printer_mysql_qry = "select * from user where user_id like '$printer_user_id';";
        				$select_user_printer_mysql_qry_result = mysqli_query($conn ,$select_user_printer_mysql_qry);
        				$data->user=$select_user_printer_mysql_qry_result->fetch_object();
        				
        				$tempArray =$data;
        				array_push($printer_Array, $tempArray);		
        				
        			}
        
        		
        		}else{
        		
        			$error = "1";
        		}
    		}
		}else{
		    $error =1;
		}
    		
    		
    	
    	}else if(($dataOBJ->delivery_avail == "Yes")&&($dataOBJ->self_pick_up_avail == "No")){ 
    		
    		if($Available_Printer_Array!=null){
    		    for($i=0;$i<count($Available_Printer_Array);$i++){
    		        $printer = new stdClass();
    		        $available_printer = $Available_Printer_Array[$i];
    		        $avail_printer_id = $available_printer->printer_id;
    		        
            		$select_shippingOption_mysql_qry = "select * from shipping_option where delivery_avail like 'Yes' and printer_id like '$avail_printer_id';";
            		$select_shippingOption_mysql_qry_result = mysqli_query($conn ,$select_shippingOption_mysql_qry);
            		if(mysqli_num_rows($select_shippingOption_mysql_qry_result) > 0){	
            	
            			$printer_Array = array();
            			$tempArray = array();
            			$data= new stdClass();
            
            		
            			while($row = $select_shippingOption_mysql_qry_result->fetch_object()){
            			
            				$printer_id = $row->printer_id;
            				
            				//Fetech Printers data based on the printer id.
            				$select_printer_mysql_qry = "select * from printer where printer_id like $printer_id;";
            				$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
            				$data=$select_printer_mysql_qry_result->fetch_object();
            				
            				$printer_user_id = $data->user_id;
            		
            				//Fetch user data of printer based on printer id
            				$select_user_printer_mysql_qry = "select * from user where user_id like '$printer_user_id';";
            				$select_user_printer_mysql_qry_result = mysqli_query($conn ,$select_user_printer_mysql_qry);
            				$data->user=$select_user_printer_mysql_qry_result->fetch_object();
            				
            				$tempArray =$data;
            				array_push($printer_Array, $tempArray);	
            				
            			}
        		
        		}else{
        			$error = "1";
        		}
    		    }
		}else{
		    $error =1;
		}

	}else{
	    if($Available_Printer_Array!=null){
    		    for($i=0;$i<count($Available_Printer_Array);$i++){
    		        $printer = new stdClass();
    		        $available_printer = $Available_Printer_Array[$i];
    		        $avail_printer_id = $available_printer->printer_id;
    		        
            		$select_shippingOption_mysql_qry = "select * from shipping_option where self_pick_up_avail like 'Yes' and printer_id like '$avail_printer_id';";
            		$select_shippingOption_mysql_qry_result = mysqli_query($conn ,$select_shippingOption_mysql_qry);
            		if(mysqli_num_rows($select_shippingOption_mysql_qry_result) > 0){	
            	
            			$printer_Array = array();
            			$tempArray = array();
            			$data= new stdClass();
            
            		
            			while($row = $select_shippingOption_mysql_qry_result->fetch_object()){
            			
            				$printer_id = $row->printer_id;
            				
            			
            				
            				//Fetech Printers data based on the printer id.
            				$select_printer_mysql_qry = "select * from printer where printer_id like '$row->printer_id';";
            				$select_printer_mysql_qry_result = mysqli_query($conn ,$select_printer_mysql_qry);
            				$data=$select_printer_mysql_qry_result->fetch_object();
            				
            				$printer_user_id = $data->user_id;
            		
            				//Fetch user data of printer based on printer id
            				$select_user_printer_mysql_qry = "select * from user where user_id like '$printer_user_id';";
            				$select_user_printer_mysql_qry_result = mysqli_query($conn ,$select_user_printer_mysql_qry);
            				$data->user=$select_user_printer_mysql_qry_result->fetch_object();
            				
            				$tempArray =$data;
            				array_push($printer_Array, $tempArray);	
            				
            			}
            		
            		}else{
            			$error = "1";
            		}
            		
    		    }	
         }else{
             $error=1;
         }
	}
}
/*Function:To fetch available printer*/
function FetchAvailablePrinter(){
        $select_printer_mysql_qry = "select * from printer;";
        $select_printer_mysql_qry_result = mysqli_query($GLOBALS['conn'] ,$select_printer_mysql_qry);
        if(mysqli_num_rows($select_printer_mysql_qry_result) > 0){	
    	
    	
    	$Printer_Array = array();
    	$tempArray = array();
    	
    	while($row = $select_printer_mysql_qry_result->fetch_object()){
    	    
    	    $printer_id= $row->printer_id;
    	    if(VerificationOfFillUpEverything($printer_id)==1){
    	         	$select_user_mysql_qry = "select * from user where user_id like '$row->user_id';";
    	        	$select_user_mysql_qry_result = mysqli_query($GLOBALS['conn'],$select_user_mysql_qry);
    	        	$row->user=$select_user_mysql_qry_result->fetch_object();
    		        $tempArray = $row;
    	             array_push($Printer_Array, $tempArray);
    	    }
    	
    	}

        } else{
            $error=1;
        }
    return $Printer_Array;
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
	

/*Create Response */
if($error == "1"){
	$response->message = "Fail";
	$response->data = null;
}else{
	$response->message = "Success";
	$response->data = json_encode($printer_Array);
}


echo json_encode($response);
$conn->close();
?>