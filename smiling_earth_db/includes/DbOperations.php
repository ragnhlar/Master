<?php

	class DbOperations{

		private $con;

		function __construct(){
			
			require_once dirname(__FILE__).'/DbConnect.php';

			$db = new DbConnect();

			$this->con = $db->connect();

		}

		/*CRUD -> C -> CREATE*/

		public function createUser2($email, $pass, $name, $gender, $weight, $birthdate, $consent, $address, $zip_code, $city){
			if ($this->doesUserExist($email)) {
				return 0;
			} else {
				$encryptedPassword = md5($pass);
				$stmt = $this->con->prepare("
					INSERT INTO `users` (`id`, `email`, `password`, `name`, `gender`, `weight`, `birthdate`, `consent`, `address`, `zip_code`, `city`) 
					VALUES (NULL, ?,?,?,?,?,?,?,?,?,?;");
				$stmt->bind_param("ssssisssis",$email, $encryptedPassword, $name, $gender, $weight, $birthdate, $consent, $address, $zip_code, $city);
				if($stmt->execute()){
					return 1;
				} else {
					return 2;
				}
			}
		}

		public function createUser($username, $pass, $email){
			if ($this->isUserExist($username,$email)) {
				return 0;
			} else {
				$password = md5($pass);
				$stmt = $this->con->prepare(
					"INSERT INTO `users` (`id`, `username`, `password`, `email`) VALUES (NULL, ?, ?, ?);");
				$stmt->bind_param("sss",$username,$password,$email);

				if($stmt->execute()){
					return 1;
				} else {
					return 2;
				}
			}
		}

		public function insertNumbers($email, $num_coins, $walk, $cycle, $drive, $avg_cf){
			$stmt = $this->con->prepare(
				"INSERT INTO `numbers` (`id`, `email`, `num_coins`, `walk`, `cycle`, `drive`, `avg_cf`) VALUES (NULL, ?, ?, ?, ?, ?, ?);");
			$stmt->bind_param("siiiid",$email,$num_coins,$walk,$cycle,$drive,$avg_cf);
			if ($stmt->execute()) {
				return 1;
			} else {
				return 2;
			}
		}

		public function userLogin2($email, $pass){
			$password = md5($pass);
			$stmt = $this->con->prepare("SELECT id FROM users WHERE email = ? AND password = ?");
			$stmt->bind_param("ss",$email,$password);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows > 0;
		}

		public function userLogin($username, $pass){
			$password = md5($pass);
			$stmt = $this->con->prepare("SELECT id FROM users WHERE username = ? AND password = ?");
			$stmt->bind_param("ss",$username,$password);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows > 0;
		}


		public function getUserByEmail($email){
			$stmt = $this->con->prepare("SELECT * FROM users WHERE email = ?");
			$stmt->bind_param("s",$email);
			$stmt->execute();
			return $stmt->get_result()->fetch_assoc();
		}

		public function getUserByUsername($username){
			$stmt = $this->con->prepare("SELECT * FROM users WHERE username = ?");
			$stmt->bind_param("s",$username);
			$stmt->execute();
			return $stmt->get_result()->fetch_assoc();
		}

		public function getNumbersByEmail($email){
			$stmt = $this->con->prepare("SELECT * FROM numbers WHERE email = ?");
			$stmt->bind_param("s",$email);
			$stmt->execute();
			return $stmt->get_result()->fetch_assoc();
		}

		public function getAllScores(){
			$stmt = $this->con->prepare("SELECT * FROM numbers;");
			//$stmt->bind_param("s",$email);
			$stmt->execute();
			$stmt->bind_result($id, $email, $num_coins, $walk, $cycle, $drive, $avg_cf);
			$score = array();
			while ($stmt->fetch()) {
				$temp = array();
				$temp['id'] = $id;
				$temp['email'] = $email;
				$temp['num_coins'] = $num_coins;
				$temp['walk'] = $walk;
				$temp['cycle'] = $cycle;
				$temp['drive'] = $drive;
				$temp['avg_cf'] = $avg_cf;

				array_push($score, $temp);
			}
			//echo json_encode($score)
			return $score;
		}

		public function getAllScores2(){
			$stmt = $this->con->prepare("SELECT * FROM scores;");
			//$stmt->bind_param("s",$email);
			$stmt->execute();
			$stmt->bind_result($id, $email, $num_coins, $walk, $cycle, $drive, $avg_cf);
			$score = array();
			while ($stmt->fetch()) {
				$temp = array();
				$temp['id'] = $id;
				$temp['email'] = $email;
				$temp['num_coins'] = $num_coins;
				$temp['walk'] = $walk;
				$temp['cycle'] = $cycle;
				$temp['drive'] = $drive;
				$temp['avg_cf'] = $avg_cf;

				array_push($score, $temp);
			}
			//echo json_encode($score)
			return $score;
		}

		private function doesUserExist($email){
			$stmt = $this->con->prepare("SELECT id FROM users WHERE email = ?");
			$stmt->bind_param("s",$email);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows > 0;
		}

		private function isUserExist($username,$email){
			//stmt = statement
			$stmt = $this->con->prepare("SELECT id FROM users WHERE username = ? OR email = ?");
			$stmt->bind_param("ss",$username,$email);
			$stmt->execute();
			$stmt->store_result();
			return $stmt->num_rows > 0;
		}
	}