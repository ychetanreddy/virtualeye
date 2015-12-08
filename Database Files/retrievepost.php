<?php
  $con=mysqli_connect("localhost","root","root","temp");
  $username = $_POST["username"];
  $statement = mysqli_prepare($con, "SELECT pid FROM populate_post WHERE username = ?");
  mysqli_stmt_bind_param($statement, "s", $username);
  mysqli_stmt_execute($statement);
  
  mysqli_stmt_store_result($statement);
  mysqli_stmt_bind_result($statement, $pid);
  
  $id = array();


  while (mysqli_stmt_fetch($statement)) {

    array_push($id, $pid);
    
  }
  mysqli_stmt_close($statement);
  $i=0;
  $displayposts = array();
  foreach($id as $ids)
  {
    $statement = mysqli_prepare($con, "SELECT pid,post FROM post_home WHERE pid = ?");
    mysqli_stmt_bind_param($statement, "i", $ids);
    mysqli_stmt_execute($statement);
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $postid,$post);
    while (mysqli_stmt_fetch($statement)) {
      $str = "post" . $i ;
      $encrypt = $postid . "#" . $post;
      $displayposts[$str] = $encrypt;
      
      //echo $displayposts["post" . $i] . " ";  
      $i++; 
    }
    mysqli_stmt_close($s);
  }
  //echo displayposts;
  echo json_encode($displayposts);
  
  mysqli_close($con);
  ?>
