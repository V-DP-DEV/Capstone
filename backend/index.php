<?php
    //auto load all php files needed
    require_once 'autoLoad.php';
    //get the config values
    $config = require_once 'config.php';
    //load config values into the class
    Config::load($config);
    //set UTC for standard time config
    date_default_timezone_set('UTC');
    //load the environment values into the class, for db environment and api key
    Env::load(Config::get('env_path'));
    //finally handle the incoming requesty
    Router::handle();
?>