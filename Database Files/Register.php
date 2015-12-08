<?php
  $con=mysqli_connect("localhost","root","root","temp");
  $name = $_POST["name"];
  $location = $_POST["location"];
  $phnum = $_POST["phnum"];
  $username = $_POST["username"];
  $password = $_POST["password"];
  /*$name = anu;
  $location = SJ;
  $phnum = 123;
  $username = chetancheddi;
  $password = anu;*/

  //echo $username;
 $statement = mysqli_prepare($con, "SELECT * FROM table1 WHERE username = ?");
  
  mysqli_stmt_bind_param($statement, "s", $username);
  mysqli_stmt_execute($statement);
  
  mysqli_stmt_store_result($statement);
  mysqli_stmt_bind_result($statement, $name, $age, $username, $password);

 
  $user = array();
  //echo json_encode($user);
  while (mysqli_stmt_fetch($statement)) {
    
    $user[username] = $username;
  }


if($user[username]==$username)
{

  $message="user exists";
  echo json_encode($message);
  //echo "<script type='text/javascript'>alert(\"$message\");</script>";
}
else
{
  echo  $user[username];
  echo "insert";
   $query = mysqli_prepare($con , "INSERT INTO table1 (name, location, phnum, username, password) VALUES (?,?,?,?,?) ");
  mysqli_stmt_bind_param($query, "ssiss", $name, $location, $phnum, $username, $password);
  mysqli_stmt_execute($query);
  mysqli_stmt_close($query);
    echo "close";
if (!mysqli_query($con,$query)) 
{ 
  die('Error: ' . mysqli_error($con)); 
} 
}

 
  mysqli_close($con);

  
  ?>
