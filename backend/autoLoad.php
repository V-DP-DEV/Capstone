<?php
    //load individual files
    require_once __DIR__ . '/exceptions/Exceptions.php';
    require_once __DIR__ . '/Router.php';
    require_once __DIR__ . '/Container.php';

    //autoload when needed in folder
    spl_autoload_register(function ($class) {
        //folders to target
        $folders = [
            'controllers',
            'services',
            'repositories',
            'middleware',
            'helpers'
        ];

        //go through each file and include them
        foreach ($folders as $folder) {

            $file = __DIR__ . '/' . $folder . '/' . $class . '.php';

            if (file_exists($file)) {
                require_once $file;
                return;
            }
        }
    });
?>