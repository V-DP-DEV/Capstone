<?php
    class HttpTestHelper
{
    private string $baseUrl;

    public function __construct(string $baseUrl)
    {
        $this->baseUrl = rtrim($baseUrl, '/');
    }

    public function request(
        string $method,
        string $endpoint,
        ?array $body = null,
        ?string $token = null
    ): array {
        $ch = curl_init(
            $this->baseUrl . '/' . ltrim($endpoint, '/')
        );

        $headers = [
            'Accept: application/json'
        ];

        if ($body !== null) {
            $headers[] = 'Content-Type: application/json';
        }

        if ($token !== null) {
            $headers[] = 'Authorization: Bearer ' . $token;
        }

        curl_setopt_array($ch, [
            CURLOPT_CUSTOMREQUEST => $method,
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_HTTPHEADER => $headers,
        ]);

        if ($body !== null) {
            curl_setopt(
                $ch,
                CURLOPT_POSTFIELDS,
                json_encode($body)
            );
        }

        $responseBody = curl_exec($ch);

        if ($responseBody === false) {
            throw new RuntimeException(curl_error($ch));
        }

        $statusCode = curl_getinfo(
            $ch,
            CURLINFO_HTTP_CODE
        );

        curl_close($ch);

        return [
            'status' => $statusCode,
            'body' => json_decode($responseBody, true)
        ];
    }
}
?>