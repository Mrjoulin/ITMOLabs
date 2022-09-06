<?php
	$start_time = microtime(true);

	header("Content-Type: application/json; charset=utf-8");

	function abort(int $code, string $message) {
		http_response_code($code);
		echo "{\"message\":\"$message\"}";
		exit;
	}

	function json_data(array $required_parameters) {
		if ($_SERVER["CONTENT_TYPE"] !== "application/json") {
			abort(400, "Not a json");
		}
		$request_body = file_get_contents("php://input");
		$json = json_decode($request_body, true);
		if ($json === null) {
			abort(400, "Not a json");
		}

		foreach ($required_parameters as $key => $type) {
			if (!array_key_exists($key, $json)) {
				abort(400, "$key is required");
			}
			if (!$type($json[$key])) {
				abort(400, "$key must be " . str_replace("is_", "", $type));
			}
		}

		return $json;
	}

	$method = $_SERVER["REQUEST_METHOD"];
	if ($method !== "POST") {
		abort(405, "$method not allowed");
	}

	$data = json_data([
		"x" => "is_numeric",
		"y" => "is_numeric",
		"r" => "is_numeric"
	]);
	$x = $data["x"];
	$y = $data["y"];
	$r = $data["r"];

    if ($x < -4 || $x > 4) abort(400, "X not in range!");
    if ($y <= -5 || $y >= 5) abort(400, "Y not in range!");
	if ($r <= 0) abort(400, "R must be positive!");
    if ($r > 3) abort(400, "R not in range!");

    $hit = false;

    if ($x >= 0 && $y >= 0) {
        $hit = ($x + $y) <= $r;
    } elseif ($x < 0 && $y >= 0) {
        $hit = ($x >= -$r) && ($y <= $r/2);
    } elseif ($x <= 0 && $y < 0) {
        $hit = ($x*$x + $y*$y) <= $r*$r/4;
    }

	$end_time = microtime(true);
	$script_time = number_format(($end_time - $start_time) * 1000, 3, '.', '');
	$end_datetime = date("d/m/Y H:i:s", (int) $end_time);
?>
{
	"result": <?=$hit ? '"area"' : '"miss"' ?>,
	"now": "<?=$end_datetime?>",
	"script_time": <?=$script_time?>
}