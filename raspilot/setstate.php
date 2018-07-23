<?php

$enabled = ($_POST['enabled'] == 'true' ? true : false);

$output;
if ($enabled) {
    exec("sudo systemctl start alarmsystem 2>&1");
    echo "Alarmsystem has been started";
} else {
    exec("sudo systemctl stop alarmsystem 2>&1");
    echo "Alarmsystem stopped";
}