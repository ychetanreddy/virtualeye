<?php 
    $con=mysqli_connect("localhost","root","root","temp");
    $post=$_POST["post"];
    $loc=$_POST["loc"];
    $username=$_POST["username"];
    
    $statement = mysqli_prepare($con, "INSERT INTO post_home (post,loc) VALUES (?,?)");
    
    mysqli_stmt_bind_param($statement, "ss", $post,$loc);
    mysqli_stmt_execute($statement);
    mysqli_stmt_close($statement);

    //attach user to his own post
    // i.e to Table 3 populate_post
    $extract = mysqli_prepare($con, "SELECT pid FROM post_home WHERE post = ? AND loc = ? ");

	mysqli_stmt_bind_param($extract, "ss", $post, $loc );

	mysqli_stmt_execute($extract);
	mysqli_stmt_store_result($extract);
	mysqli_stmt_bind_result($extract, $pid);
	

 while(mysqli_stmt_fetch($extract)){
 		$id=$pid;
 		$statement = mysqli_prepare($con, "INSERT INTO populate_post (username,pid) VALUES (?,?)");
    	mysqli_stmt_bind_param($statement, "ss", $username, $id);
    	mysqli_stmt_execute($statement);
    	mysqli_stmt_close($statement);

 }
   mysqli_stmt_close($extract);

   //attach this post to existing users in requested location

   $extract = mysqli_prepare($con, "SELECT username FROM table1 WHERE location = ?");

	mysqli_stmt_bind_param($extract, "s", $loc);

	mysqli_stmt_execute($extract);
	mysqli_stmt_store_result($extract);
	mysqli_stmt_bind_result($extract, $uname);
	

 while(mysqli_stmt_fetch($extract)){
 		$id=$uname;
 		if($uname!=$username)
 		{
 		$statement = mysqli_prepare($con, "INSERT INTO populate_post (username,pid) VALUES (?,?)");
    	mysqli_stmt_bind_param($statement, "ss", $id, $pid);
    	mysqli_stmt_execute($statement);
    	mysqli_stmt_close($statement);
    }
 }
   mysqli_stmt_close($extract);
mysqli_close($con);
?>