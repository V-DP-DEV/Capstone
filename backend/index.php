<?php
    require_once 'autoLoad.php';
    $config = require_once 'config.php';
    Config::load($config);
    date_default_timezone_set('UTC');
    Env::load(Config::get('env_path'));
    Router::handle();
?>