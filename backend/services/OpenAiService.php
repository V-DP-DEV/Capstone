<?php

class OpenAIService
{
    private string $apiKey;
    public function __construct(
    ) {
        //gets api key from env file
        $this->apiKey = getenv('OPENAI_API_KEY');
    }

    //does the call
    public function createResponse(array $body): array
    {   
        //performs curl and sets header information
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

        //execute the command
        $response = curl_exec($ch);

        //if no response throw an error 
        if ($response === false) {
            throw new Exception(curl_error($ch));
        }

        //otherwise set the code
        $statusCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);

        curl_close($ch);

        //if other errors occured throw error
        if ($statusCode >= 400) {
    throw new Exception(
        "OpenAI request failed: HTTP {$statusCode}\n" .
        "Response: {$response}"
    );
}
        //return the decoded response
        return json_decode($response, true);
    }
}
