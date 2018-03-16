<?php

	require_once '../includes/DbOperations.php';
	//$response = array();

	$db = new DbOperations();

	$score = $db->getAllScores2();

	echo json_encode($score);

	//echo json_encode($response);