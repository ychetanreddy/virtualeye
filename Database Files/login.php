<?php
$con=mysqli_connect("localhost","root","root","temp");
$sql="INSERT INTO table1 (, LastName, Age) VALUES ('admin', 'admin','adminstrator')";
if (mysqli_query($con,$sql))
{
   echo "Values have been inserted successfully";
}
?>