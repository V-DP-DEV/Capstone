<?php

class OpenAIService
{
    private string $apiKey;
    public function __construct(
    ) {
        $this->apiKey = getenv('OPENAI_API_KEY');
    }

    public function createResponse(array $body): array
    {
        $ch = curl_init('https://api.openai.com/v1/responses');

        curl_setopt_array($ch, [
            CURLOPT_POST => true,
            CURLOPT_RETURNTRANSFER => true,
            CURLOPT_HTTPHEADER => [
                'Content-Type: application/json',
                'Authorization: Bearer ' . $this->apiKey
            ],
            CURLOPT_POSTFIELDS => json_encode($body)
        ]);

        $response = curl_exec($ch);

        if ($response === false) {
            throw new Exception(curl_error($ch));
        }

        $statusCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);

        curl_close($ch);

        if ($statusCode >= 400) {
    throw new Exception(
        "OpenAI request failed: HTTP {$statusCode}\n" .
        "Response: {$response}"
    );
}

        return json_decode($response, true);
    }
}
