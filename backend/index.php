<?php
    //auto load all php files needed
    require_once 'autoLoad.php';
    //get the config values
    $config = require_once 'config.php';

    $container = new Container();
    $container->bind(IUserRepository::class, UserRepository::class);
    $container->bind(IAuthTokenRepository::class, AuthTokenRepository::class);
    $container->bind(IRefreshTokenRepository::class, RefreshTokenRepository::class);
    $container->bind(ILogRepository::class, LogRepository::class);

    

    //load config values into the class
    Config::load($config);
    //set UTC for standard time
    date_default_timezone_set('UTC');
    //load the environment values into the class, for db environment and api key
    Env::load(Config::get('env_path'));
    //finally handle the incoming request
    Router::handle($container);
?>