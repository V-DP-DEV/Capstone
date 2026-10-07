<?php
    class TimeUtil{
        public static function toMillis(string $datetime): int
    {
        return strtotime($datetime) * 1000;
    }

    public static function nowMillis(): int
    {
        return (int) (microtime(true) * 1000);
    }
}