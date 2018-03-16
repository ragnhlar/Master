<?php

	require_once '../includes/DbOperations.php';
	$response = array();

	if($_SERVER['REQUEST_METHOD']=='POST')
	{
		if(
			isset($_POST['email']) and
			isset($_POST['password']) and
			isset($_POST['name']) and
			isset($_POST['gender']) and
			isset($_POST['weight']) and
			isset($_POST['birthdate']) and
			isset($_POST['consent']) and
			isset($_POST['address']) and
			isset($_POST['zip_code']) and
			isset($_POST['city']))
		{
			$db = new DbOperations();

			$result = $db->createUser2(
				$_POST['email'],
				$_POST['password'],
				$_POST['name'],
				$_POST['gender'],
				$_POST['weight'],
				$_POST['birthdate'],
				$_POST['consent'],
				$_POST['address'],
				$_POST['zip_code'],
				$_POST['city']);

			if($result == 1)
			{
				$response['error'] = false;
				$response['message'] = "User registered successfully";
			}elseif ($result == 2) {
				$response['error'] = true;
				$response['message'] = "Some error occurred please try again";
			}elseif ($result == 0) {
				$response['error'] = true;
				$response['message'] = "It seems that you are already registered, please choose a different email and username";
			}
		}else {
			$response['error'] = true;
			$response['message'] = "Required fields are missing";
		}
	}else{
		$response['error'] = true;
		$response['message'] = "Invalid request";
	}

	echo json_encode($response);