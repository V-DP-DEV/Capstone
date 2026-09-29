<?php
    class Router{
        //defines route that doesnt need token checks
        private static array $publicRoutes = [
            'Auth/login',
            'Auth/register',
            'Auth/logout'
        ];

        public static function handle(){
            try{
                //trim the path
                $path = trim(parse_url($_SERVER['REQUEST_URI'],PHP_URL_PATH),'/');
                
                //get the base url
                $basePath = Config::get('base_url');
                //if link starts with basepath extract basepath
                if(str_starts_with($path,$basePath .'/')){
                    $path = substr($path, strlen($basePath) +1);
                }

                //uppercase for public route checks
                $path = ucfirst($path);
                //split the path up for multiple parts for checks
                $parts = explode("/",$path);

                //ensure there are atleast 2 parts if not throw exception
                if(count($parts) < 2){
                    throw new NotFoundException("Invalid endpoint structure");
                }

                //gets controller name by upper casing first letter and concat controller
                $controllerName = ucfirst($parts[0]).'Controller';

                //get the action
                $action = $parts[1] ?? 'index';

                //get the paramaters
                $params = array_slice($parts,3);

                //if controller doesnt exist throw error
                if(!class_exists($controllerName)){
                    throw new NotFoundException("Controller not found");
                }

                //if requires token
                if(!in_array($path,self::$publicRoutes)){
                    //check if token valid (throws error and doesnt continue)
                    AuthMiddleware::requireToken();

                    //if admin check if admin
                    if($controllerName ==="AdminController"){
                        AuthMiddleware::requireAdmin();
                    }

                    //if user controller check if user
                    if($controllerName ==="UserController"){
                        AuthMiddleware::requireUser();
                    }
                }
        
                $controller = new $controllerName;

                //check if method and controller exists
                if(!method_exists($controller,$action)){
                    //otherwise throw error and exit
                    throw new NotFoundException("Endpoint not found");
                }

                //call the function
                call_user_func_array([$controller,$action],$params);
            }
            //any errors that were thrown gets caught here
            catch(Throwable $e){
                ErrorHandler::handle($e);
            }
        }
    }
?>