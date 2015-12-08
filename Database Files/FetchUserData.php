<?php
  $con=mysqli_connect("localhost","root","root","temp");
  $username = $_POST["username"];
  $password = $_POST["password"];

  $statement = mysqli_prepare($con, "SELECT * FROM table1 WHERE username = ? AND password = ?");
  mysqli_stmt_bind_param($statement, "ss", $username, $password);
  mysqli_stmt_execute($statement);
  
  mysqli_stmt_store_result($statement);
  mysqli_stmt_bind_result($statement, $name, $location, $phnum, $username, $password);
  
  $user = array();


  while (mysqli_stmt_fetch($statement)) {
    $user[name] = $name;
    $user[location] = $location;
    $user[phnum] = $phnum;
    $user[username] = $username;
    $user[password] = $password;
  }


  echo json_encode($user);
  mysqli_stmt_close($statement);
  
  mysqli_close($con);
  ?>
