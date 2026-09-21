<?php
    require_once __DIR__ . '/exceptions/Exceptions.php';
    require_once __DIR__ . '/Router.php';

    spl_autoload_register(function ($class) {
    
        $folders = [
            'controllers',
            'services',
            'repositories',
            'middleware',
            'helpers'
        ];

        foreach ($folders as $folder) {

            $file = __DIR__ . '/' . $folder . '/' . $class . '.php';

            if (file_exists($file)) {
                require_once $file;
                return;
            }
        }
    });
?>