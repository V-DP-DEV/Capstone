<?php
    require_once 'autoLoad.php';
    Env::load(__DIR__ . '/.env');
    Router::handle();
?>