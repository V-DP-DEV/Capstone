<?php 
    class Env
    {
        public static function load(string $file): void
        {
            if (!file_exists($file)) {
                throw new RuntimeException(".env file not found");
            }

            $lines = file($file, FILE_IGNORE_NEW_LINES | FILE_SKIP_EMPTY_LINES);

            
            foreach ($lines as $line) {
                $line = trim($line);

                // Ignore comments
                if ($line === '' || str_starts_with($line, '#')) {
                    continue;
                }

                [$key, $value] = array_pad(
                    explode('=', $line, 2),
                    2,
                    ''
                );

                $key = trim($key);
                $value = trim($value);

                // Remove optional surrounding quotes
                $value = trim($value, "\"'");

                putenv("$key=$value");
            }
        }
    }
?>