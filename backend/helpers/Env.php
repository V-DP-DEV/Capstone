<?php
    //used to load env 
    class Env
    {
        public static function load(string $file): void
        {
            //if files doesnt exist throw error
            if (!file_exists($file)) {
                throw new RuntimeException(".env file not found");
            }

            //get lines
            $lines = file($file, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);

            //foreach line
            foreach ($lines as $line) {
                //trim the line
                $line = trim($line);

                // Ignore comments
                if ($line === '' || str_starts_with($line, '#')) {
                    continue;
                }

                //extract key value pair
                [$key, $value] = array_pad(
                    explode('=', $line, 2),
                    2,
                    ''
                );

                //trim key and value
                $key = trim($key);
                $value = trim($value);

                // Remove optional surrounding quotes
                $value = trim($value, "\"'");

                //put key value pair in env
                putenv("$key=$value");
            }
        }
    }
?>