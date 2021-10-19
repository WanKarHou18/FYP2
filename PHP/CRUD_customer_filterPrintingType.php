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
$Available_Printer_Array = array();

$Printer_Array=array();

//Testing 

//$action = "read";
//$data ='{"Document":"No","Image":"Yes"}';
//$data ='{"delivery_avail":"No","self_pick_up_avail":"Yes"}';


//If action is read 
if($action==="read"){
	$dataOBJ=json_decode($data);
	$doc_avail = $dataOBJ->Document;
	$img_avail = $dataOBJ->Image;
	
	$Available_Printer_Array = FetchAvailablePrinter();
//	echo json_encode($Available_Printer_Array);

	/* Delivery = Yes , Self Pick = Null*/
	if(($doc_avail == "Yes")&&($img_avail == "Yes")){
		
		if($Available_Printer_Array!=null){
    		for($i=0;$i<count($Available_Printer_Array);$i++){
    		    
    		    $confirm ="0";
    		    
    		    $printer = new stdClass();
    		    $available_printer = $Available_Printer_Array[$i];
    		    $avail_printer_id = $available_printer->printer_id;
    		    
    		    //echo $avail_printer_id;
    		    if($doc_avail == "Yes"){
					
					$select_document_setting_mysql_qry = "select * from document_printing_setting where printer_id like '$avail_printer_id';";
					$select_document_setting_mysql_qry_result = mysqli_query($conn ,$select_document_setting_mysql_qry);
					if(mysqli_num_rows($select_document_setting_mysql_qry_result) > 0){
						$row1= $select_document_setting_mysql_qry_result->fetch_object();
						$setting = json_decode($row1->doc_print_pref_json);
						if($setting !=null){
						
							if(($setting->available=="Yes")){
							    $confirm+=1;
							}else{
							
							}
						}
					}
				}
				
				if($img_avail==="Yes"){
					$select_image_setting_mysql_qry = "select * from image_printing_setting where printer_id like '$avail_printer_id';";
					$select_image_setting_mysql_qry_result = mysqli_query($conn ,$select_image_setting_mysql_qry);
					if(mysqli_num_rows($select_image_setting_mysql_qry_result) > 0){
						$row1= $select_image_setting_mysql_qry_result->fetch_object();
						$setting = json_decode($row1->img_print_pref_json);
						if($setting !=null){
                           
							if(($setting->available=="Yes")){
							
							    $confirm+=1;
							}else{
								 
							}
						}
					}
				}
				
				//If document or image is available for 
				if($confirm==1){
				   
				//	echo json_encode($available_printer);
					array_push($Printer_Array, $Available_Printer_Array[$i]);	
				}else{
				}
    		   
    		  
    		}
		}
    	
    }else if(($doc_avail== "Yes")&&($img_avail == "No")){ 
    		
    		if($Available_Printer_Array!=null){
        		for($i=0;$i<count($Available_Printer_Array);$i++){
        		    
        		    $confirm ="0";
        		    
        		    $printer = new stdClass();
        		    $available_printer = $Available_Printer_Array[$i];
        		    $avail_printer_id = $available_printer->printer_id;
        		    
        		    //echo $avail_printer_id;
        		    if($doc_avail == "Yes"){
    					
    					$select_document_setting_mysql_qry = "select * from document_printing_setting where printer_id like '$avail_printer_id';";
    					$select_document_setting_mysql_qry_result = mysqli_query($conn ,$select_document_setting_mysql_qry);
    					if(mysqli_num_rows($select_document_setting_mysql_qry_result) > 0){
    						$row1= $select_document_setting_mysql_qry_result->fetch_object();
    						$setting = json_decode($row1->doc_print_pref_json);
    						if($setting !=null){
    						
    							if(($setting->available=="Yes")){
    							    $confirm+=1;
    							}else{
    							
    							}
    						}
    					}
    				}
    				
    				
    				//If document or image is available for 
    				if($confirm==1){
    				   
    				//	echo json_encode($available_printer);
    					array_push($Printer_Array, $Available_Printer_Array[$i]);	
    				}else{
    				}
        		   
        	    	}
        		}
    		
	}else{
	    	if($Available_Printer_Array!=null){
        		for($i=0;$i<count($Available_Printer_Array);$i++){
        		    
        		    $confirm ="0";
        		    
        		    $printer = new stdClass();
        		    $available_printer = $Available_Printer_Array[$i];
        		    $avail_printer_id = $available_printer->printer_id;
        		    
        		    //echo $avail_printer_id;
        		    if($img_avail == "Yes"){
    					
    					$select_image_setting_mysql_qry = "select * from image_printing_setting where printer_id like '$avail_printer_id';";
    					$select_image_setting_mysql_qry_result = mysqli_query($conn ,$select_image_setting_mysql_qry);
    					if(mysqli_num_rows($select_image_setting_mysql_qry_result) > 0){
    						$row1= $select_image_setting_mysql_qry_result->fetch_object();
    						$setting = json_decode($row1->img_print_pref_json);
    						if($setting !=null){
    						
    							if(($setting->available=="Yes")){
    							    $confirm+=1;
    							}else{
    							
    							}
    						}
    					}
    				}
    				
    				
    				//If document or image is available for 
    				if($confirm==1){
    				   
    				//	echo json_encode($available_printer);
    					array_push($Printer_Array, $Available_Printer_Array[$i]);	
    				}else{
    				}
        		   
        	    	}
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

if($Printer_Array==null){
    $error=1;
}

/*Create Response */
if($error == "1"){
	$response->message = "Fail";
	$response->data = null;
}else{
	$response->message = "Success";
	$response->data = json_encode($Printer_Array);
}


echo json_encode($response);
$conn->close();
?>