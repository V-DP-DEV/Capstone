<?php
// tests/AuthTest.php
require_once __DIR__ . '/ApiTestCase.php';

class ApiInterviewAttemptTest extends ApiTestCase
{
    public function testSubmitInterview():void{
        $token = $this->loginAsUser();
        $request = [
    'id' => 1,
    'answers' => [
        [
            'id' => 1,
            'answer' => 'I have experience working with C# and frontend applications.',
            'secondsSpent' => 45
        ],
        [
            'id' => 2,
            'answer' => 'Dependency injection allows dependencies to be provided to a class rather than creating them directly.',
            'secondsSpent' => 60
        ],
        [
            'id' => 3,
            'answer' => 'Using await allows the application to continue without blocking while waiting for the HTTP request.',
            'secondsSpent' => 52
        ],
        [
            'id' => 4,
            'answer' => 'public async Task<string> GetUserName(int id) { ... }',
            'secondsSpent' => 120
        ],
        [
            'id' => 5,
            'answer' => 'The problem is that Result blocks the thread. I would use await instead.',
            'secondsSpent' => 75
        ],
        [
            'id' => 6,
            'answer' => 'I would structure the Blazor application using separate API services with dependency injection.',
            'secondsSpent' => 90
        ],
        [
            'id' => 7,
            'answer' => 'I would investigate the component lifecycle and determine where the API request is being triggered multiple times.',
            'secondsSpent' => 110
        ],
        [
            'id' => 8,
            'answer' => 'I improved frontend performance by reducing unnecessary API calls and caching repeated data.',
            'secondsSpent' => 85
        ]
    ]
];

        $response = $this->http->request(
            'POST',
            '/interviewAttempt/submitAttempt',
            $request,
            $token
        );

        print_r($response);
    }
    
        

    public function testAnalyse(){
        $token = $this->loginAsUser();

        $response = $this->http->request(
            'POST',
            '/interviewAttempt/analyse',
            ['attemptId'=>1],
            $token
        );

        print_r($response);
    }
}