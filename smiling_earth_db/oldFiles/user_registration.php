<?php
	include_once 'connection.php';

	class User {
		private $db;
		private $connection;
		

		function __construct(){
			$this->db=new DB_Connection;
			$this->connection= $this->db->getConnection();
		}

		public function does_user_exist($email,$encrypted_password,$name,$gender,$weight,$birthdate,$consent,$address,$zip_code,$city){
			$email;
			$password;
			global $name;
			global $gender;
			global $weight;
			global $birthdate;
			global $consent;
			global $address;
			global $zip_code;
			global $city;
			$query = "Select * from users where email='$email' and password = '$encrypted_password' ";
				$result = mysqli_query($this->connection, $query);
				if(mysqli_num_rows($result)>0){
					$json['success'] = ' Welcome '.$email;
					echo json_encode($json);
					mysqli_close($this -> connection);
				}else{
					$query = "insert into USERS (email, password, name, gender, weight, birthdate, consent, address, zip_code, city) values ( '$email','$encrypted_password','$name','$gender','$weight','$birthdate','$consent','$address','$zip_code','$city')";
					$inserted = mysqli_query($this -> connection, $query);
					if($inserted == 1 ){
						$json['success'] = 'Account created';
					}else{
						$json['error'] = 'Wrong password';
					}
					echo json_encode($json);
					mysqli_close($this->connection);
				}
		}
	}

	$user = new User();
	if(isset
		($_POST['email'],$_POST['password'],$_POST['name'],$_POST['gender'],$_POST['weight'],$_POST['birthdate'],
			$_POST['consent'],$_POST['address'],$_POST['zip_code'],$_POST['city']))
	{
		$email = $_POST['email'];
		$password = $_POST['password'];
		$name = $_POST['name'];
		$gender = $_POST['gender'];
		$weight = $_POST['weight'];
		$birthdate = $_POST['birthdate'];
		$consent = $_POST['consent'];
		$address = $_POST['address'];
		$zip_code = $_POST['zip_code'];
		$city = $_POST['city'];

		if(!empty($email) && !empty($password) && !empty($name) && !empty($gender) && !empty($weight) && !empty($birthdate) && !empty($consent) && !empty($address) && !empty($zip_code) && !empty($city)) {
			$encrypted_password = md5($password);
			$user->does_user_exist($email,$encrypted_password,$name,$gender,$weight,$birthdate,$consent,$address,$zip_code,$city);
		} else {
			echo json_encode("You must type both inputs");
		}
	} 
?>