<?php

	require_once '../includes/DbOperations.php';
	$response = array();

	if($_SERVER['REQUEST_METHOD']=='POST'){

		if(isset($_POST['email']) and isset($_POST['password'])){
			$db = new DbOperations();

			if($db->userLogin2($_POST['email'], $_POST['password'])){
				$user = $db->getUserByEmail($_POST['email']);
				$response['error'] = false;
				$response['id'] = $user['id'];
				$response['email'] = $user['email'];
				$response['name'] = $user['name'];
				$response['gender'] = $user['gender'];
				$response['weight'] = $user['weight'];
				$response['birthdate'] = $user['birthdate'];
				$response['consent'] = $user['consent'];
				$response['address'] = $user['address'];
				$response['zip_code'] = $user['zip_code'];
				$response['city'] = $user['city'];
			}else{
				$response['error'] = true;
				$response['message'] = "Invalid username or password";
			}
		}else{
			$response['error'] = true;
			$response['message'] = "Required fields are missing";
		}
	}

	echo json_encode($response);