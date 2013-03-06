<?php
$imageName = $_FILES['uploadedfile']['name'];

/**********************************************/
/* MODIFY THESE VALUES TO REFLECT YOUR SERVER */
/* CONFIGURATION                              */
/**********************************************/
//Tracks user signature submission by IP
$session_id = $_SERVER['REMOTE_ADDR'];

//mySQL login information
$mySQLuser = 'admin';
$mySQLpass = 'password';
$mySQLserver = 'localhost';
$mySQLdatabase = 'database';
$mySQLtable = 'table';
$link = mysql_connect($mySQLserver,$mySQLuser, $mySQLpass) OR DIE(mysql_error);
mysql_select_db($mySQLdatabase, $link);

//This is the directory where the signature will be uploaded
$target_path = "uploads/";


$target_path = $target_path . basename( $_FILES['uploadedfile']['name']); 
$link = mysql_connect('localhost','dbuser', 'dbpassword') OR DIE(mysql_error);
    mysql_select_db("$mySQLdatabase", $link);
    mysql_query("INSERT INTO `$mySQLtabel` (`session`, `image_location`) VALUES ('$session_id', '$imageName')") OR DIE(mysql_error());
    mysql_close($link);
    
if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) { 
    echo "The file ". basename( $_FILES['uploadedfile']['name'])." has been uploaded";            
} else{ 
    echo "There was an error uploading the file, please try again!"; 
}

?>
