<?php

class Container
{
    private array $bindings = [];
    private array $instances = [];

    public function bind(string $abstract, string $concrete): void
    {
        $this->bindings[$abstract] = $concrete;
    }

    public function get(string $abstract): object
    {
        // Return existing instance if already created
        if (isset($this->instances[$abstract])) {
            return $this->instances[$abstract];
        }

        // Resolve interface → concrete class
        $concrete = $this->bindings[$abstract] ?? $abstract;

        $reflection = new ReflectionClass($concrete);

        if (!$reflection->isInstantiable()) {
            throw new RuntimeException(
                "Cannot instantiate: $concrete"
            );
        }

        $constructor = $reflection->getConstructor();

        // No constructor dependencies
        if ($constructor === null) {
            $instance = new $concrete();

            $this->instances[$abstract] = $instance;

            return $instance;
        }

        $dependencies = [];

        foreach ($constructor->getParameters() as $parameter) {
            $type = $parameter->getType();

            if (!$type instanceof ReflectionNamedType) {
                throw new RuntimeException(
                    "Unable to resolve dependency: "
                    . $parameter->getName()
                );
            }

            $dependencies[] = $this->get($type->getName());
        }

        $instance = $reflection->newInstanceArgs($dependencies);

        $this->instances[$abstract] = $instance;

        return $instance;
    }
}