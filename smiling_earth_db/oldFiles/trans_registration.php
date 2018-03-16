<?php
	include_once 'connection.php';

	class Trans {
		private $db;
		private $connection;
		

		function __construct(){
			$this->db=new DB_Connection;
			$this->connection= $this->db->getConnection();
		}

		public function does_trans_exist($habits,$car_owner_1,$reg_nr_1,$car_value_1,$yearly_driving_distance_1,$dur_ownership_1,$car_owner_2,$reg_nr_2,$car_value_2,$yearly_driving_distance_2,$dur_ownership_2){
			global $habits;
			global $car_owner_1;
			global $reg_nr_1;
			global $car_value_1;
			global $yearly_driving_distance_1;
			global $dur_ownership_1;
			global $car_owner_2;
			global $reg_nr_2;
			global $car_value_2;
			global $yearly_driving_distance_2;
			global $dur_ownership_2;
			
			$query = "insert into transportation (habits, car_owner_1, reg_nr_1, car_value_1, yearly_driving_distance_1, dur_ownership_1, car_owner_2, reg_nr_2, car_value_2, yearly_driving_distance_2, dur_ownership_2) values 
			('$habits','$car_owner_1','$reg_nr_1','$car_value_1','$yearly_driving_distance_1','$dur_ownership_1','$car_owner_2','$reg_nr_2','$car_value_2','$yearly_driving_distance_2','$dur_ownership_2')";
			$inserted = mysqli_query($this -> connection, $query);
			if($inserted == 1 ){
				$json['success'] = 'Trans registered';
			}else{
				$json['error'] = 'Trans not registered';
			}
			echo json_encode($json);
			mysqli_close($this->connection);
		}
	}

	$trans = new Trans();
	if(isset($_POST['habits'],$_POST['car_owner_1'],$_POST['reg_nr_1'],$_POST['car_value_1'],$_POST['yearly_driving_distance_1'],$_POST['dur_ownership_1'],$_POST['car_owner_2'],$_POST['reg_nr_2'],$_POST['car_value_2'],$_POST['yearly_driving_distance_2'],$_POST['dur_ownership_2'])){
		$habits = $_POST['habits'];
		$car_owner_1 = $_POST['car_owner_1'];
		$reg_nr_1 = $_POST['reg_nr_1'];
		$car_value_1 = $_POST['car_value_1'];
		$yearly_driving_distance_1 = $_POST['yearly_driving_distance_1'];
		$dur_ownership_1 = $_POST['dur_ownership_1'];
		$car_owner_2 = $_POST['car_owner_2'];
		$reg_nr_2 = $_POST['reg_nr_2'];
		$car_value_2 = $_POST['car_value_2'];
		$yearly_driving_distance_2 = $_POST['yearly_driving_distance_2'];
		$dur_ownership_2 = $_POST['dur_ownership_2'];
		
		$trans->does_trans_exist($habits,$car_owner_1,$reg_nr_1,$car_value_1,$yearly_driving_distance_1,$dur_ownership_1,$car_owner_2,$reg_nr_2,$car_value_2,$yearly_driving_distance_2,$dur_ownership_2);
	} 
?>