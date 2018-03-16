<?php

	require_once '../includes/DbOperations.php';
	$response = array();

	if($_SERVER['REQUEST_METHOD']=='POST'){

		if(isset($_POST['email'])){
			$db = new DbOperations();

			$score = $db->getNumbersByEmail($_POST['email']);
				$response['error'] = false;
				$response['id'] = $score['id'];
				$response['email'] = $score['email'];
				$response['num_coins'] = $score['num_coins'];
				$response['walk'] = $score['walk'];
				$response['cycle'] = $score['cycle'];
				$response['drive'] = $score['drive'];
				$response['avg_cf'] = $score['avg_cf'];
		}else{
			$response['error'] = true;
			$response['message'] = "Required fields are missing";
		}
	}

	echo json_encode($response);