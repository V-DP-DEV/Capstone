<?php
    class SortOptions{
        public readonly string $field;
        public readonly string $direction;

        public function __construct(
            string $field,
            string $direction = 'DESC',
        ) {
            $direction = strtoupper($direction);

            if (!in_array($direction, ['ASC', 'DESC'], true)) {
                throw new ValidationException("Sort not send correctly");
            }

            $this->field = $field;
            $this->direction = $direction;
        }

        public function mapField(array $validFields):string{
            $sortColumn = $validFields[$this->field]??null;
            if($sortColumn === null){
                throw new ValidationException("Field not in allowed list " . $this->field);
            }
            return $sortColumn;
        }
    }