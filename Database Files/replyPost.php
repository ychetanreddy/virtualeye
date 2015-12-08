<?php
  $con=mysqli_connect("localhost","root","root","temp");
  $username = $_POST["username"];
  $pid = $_POST["postId"];
  $response = $_POST["response"];
  
  $statement = mysqli_prepare($con, "UPDATE post_home SET post = ? WHERE pid = ?");
  mysqli_stmt_bind_param($statement, "si", $response,intval($pid));
  mysqli_stmt_execute($statement);
  
  
  mysqli_stmt_close($statement);
  
  
  mysqli_close($con);
  ?>