<?php
	include_once 'connection.php';

	class House {
		private $db;
		private $connection;
		

		function __construct(){
			$this->db=new DB_Connection;
			$this->connection= $this->db->getConnection();
		}

		public function does_house_exist($address,$zip_code,$city,$heat_type,$b_year,$r_year){
			global $address;
			global $zip_code;
			global $city;
			global $b_year;
			global $r_year;
			$query = "insert into home (address, zip_code, city, heat_type, b_year, r_year) values 
			('$address','$zip_code','$city','$heat_type','$b_year','$r_year')";
			$inserted = mysqli_query($this -> connection, $query);
			if($inserted == 1 ){
				$json['success'] = 'Home registered';
			}else{
				$json['error'] = 'Home not registered';
			}
			echo json_encode($json);
			mysqli_close($this->connection);
		}
	}

	$house = new House();
	if(isset
		($_POST['address'],$_POST['zip_code'],$_POST['city'],$_POST['heat_type'],$_POST['b_year'],$_POST['r_year']))
	{
		$address = $_POST['address'];
		$zip_code = $_POST['zip_code'];
		$city = $_POST['city'];
		$heat_type = $_POST['heat_type'];
		$b_year = $_POST['b_year'];
		$r_year = $_POST['r_year'];

		if(!empty($address) && !empty($zip_code) && !empty($city) && !empty($heat_type) && !empty($b_year) && !empty($r_year)) {
			$house->does_house_exist($address,$zip_code,$city,$heat_type,$b_year,$r_year);
		} else {
			echo json_encode("You must type both inputs");
		}
	} 
?>