<?php

	require_once '../includes/DbOperations.php';
	$response = array();

	if($_SERVER['REQUEST_METHOD']=='POST'){

		if(isset($_POST['email']) and 
			isset($_POST['num_coins']) and 
			isset($_POST['walk']) and 
			isset($_POST['cycle']) and 
			isset($_POST['drive']) and 
			isset($_POST['avg_cf']))
		{
			$db = new DbOperations();

			$result = $db->insertNumbers(
				$_POST['email'],
				$_POST['num_coins'],
				$_POST['walk'],
				$_POST['cycle'],
				$_POST['drive'],
				$_POST['avg_cf']);

			if($result == 1)
			{
				$response['error'] = false;
				$response['message'] = "Numbers registered successfully";
			}elseif ($result == 2) {
				$response['error'] = true;
				$response['message'] = "Some error occurred please try again";
			}
		}else{
			$response['error'] = true;
			$response['message'] = "Required fields are missing";
		}
	}else{
		$response['error'] = true;
		$response['message'] = "Invalid request";
	}

	echo json_encode($response);

