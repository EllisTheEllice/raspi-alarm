<?php

$returnValue = exec("systemctl status alarmsystem");

if (strpos($returnValue, "exited") === false && strpos($returnValue, "killed") === false) {
    echo "running";
} else {
    echo "not running";
}