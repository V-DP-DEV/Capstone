<?php
    class Router{
        private static array $publicRoutes = [
            'Auth/login',
            'Auth/register',
            'Auth/logout'
        ];

        public static function handle(){
            try{
                $path = trim(parse_url($_SERVER['REQUEST_URI'],PHP_URL_PATH),'/');
                
                //fix later when deploying
                $basePath = Config::get('base_url');


                if(str_starts_with($path,$basePath .'/')){
                    $path = substr($path, strlen($basePath) +1);
                }

                $parts = explode("/",$path);

                if(count($parts) < 2){
                    throw new NotFoundException("Invalid endpoint structure");
                }

                //gets controller name by upper casing first letter and concat controller
                $controllerName = ucfirst($parts[0]).'Controller';

                $action = $parts[1] ?? 'index';

                $params = array_slice($parts,3);

                if(!class_exists($controllerName)){
                    throw new NotFoundException("Controller not found");
                }

                if(!in_array($path,self::$publicRoutes)){
                    AuthMiddleware::requireToken();

                    if($controllerName ==="AdminController"){
                        AuthMiddleware::requireAdmin();
                    }

                    if($controllerName ==="UserController"){
                        AuthMiddleware::requireUser();
                    }
                }

                $controller = new $controllerName;

                if(!method_exists($controller,$action)){
                    throw new NotFoundException("Endpoint not found");
                }

                call_user_func_array([$controller,$action],$params);
            }
            catch(Throwable $e){
                ErrorHandler::handle($e);
            }
        }
    }
?>