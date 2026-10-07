<?php

use PHPUnit\Framework\TestCase;
require_once __DIR__ . '/HttpTestHelper.php';

class ApiTest extends TestCase
{
    private HttpTestHelper $http;
    private string $tokenAdmin;

    protected function setUp(): void
    {
        $this->http = new HttpTestHelper('http://mywebsite.local');
    }
    /*
    public function testLogin():void
    {
        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'admin@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );
        //print_r($response);
        $this->assertSame(200, $response['status']);
        $this->assertTrue($response['body']['success']);
        $this->tokenAdmin = $response['body']['data']['token'];
    }

    public function testLogoutAllDevices():void
    {
        //login and than logout with the token
        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'admin@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );
        $response = $this->http->request(
            'POST',
            '/auth/logoutAllDevices',
            null,
            $response['body']['data']['token']
        );
        //print_r($response);
        $this->assertSame(200, $response['status']);
    }
    
    public function testLogout():void
    {
        //login and than logout with the token
        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'admin@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );
        $response = $this->http->request(
            'POST',
            '/auth/logout',
            null,
            $response['body']['data']['token']
        );
        $this->assertSame(200, $response['status']);

        //test that logout without token returns 200 ie still succeeds
        $response = $this->http->request(
            'POST',
            '/auth/logout',
            null,
            ''
        );
        $this->assertSame(200, $response['status']);
    }
    */
    /*
    public function testRefreshToken():void
    {
        //test with bearer token
        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'admin@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );

        print_r($response);
        $token = $response['body']['data']['token'];
        $refreshToken = $response['body']['data']['refreshToken'];
        $response = $this->http->request(
            'POST',
            '/auth/refresh',
            ['refreshToken' => $refreshToken],
            null
        );
        print_r($response);
        print_r("hey");
        $this->assertSame(200, $response['status']);

        //test with no bearer token
        $response = $this->http->request(
            'POST',
            '/auth/refresh',
            null
        );
        print_r($response);

        $this->assertSame(401, $response['status']);
    }
        */
    /*
    public function testSignup():void
    {
        //test with bearer token
        $response = $this->http->request(
            'POST',
            '/auth/signup',
            ['email' => 'admin2@example.com', 'password' => 'Password123', "firstname"=>"12","surname"=>"Hello"]
        );
        
        //print_r($response);
        $this->assertSame(400, $response['status']);
        $response = $this->http->request(
            'POST',
            '/auth/signup',
            ['email' => 'admin@example.com', 'password' => 'Password123', "firstname"=>"12","surname"=>"Hello"]
        );
        print_r($response);
        $this->assertSame(400, $response['status']);
    }
    public function testGetUserInterviews():void
    {
        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'user2@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );
        print_r($response);
        $response = $this->http->request(
            'GET',
            '/interview/getInterviews?name=C#',
            null,
            $response['body']['data']['token']
        );
        print_r($response);
        $this->assertSame(200, $response['status']);
    }

    public function testGetUserInterview():void{
        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'user2@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );
        print_r($response);
        $response = $this->http->request(
            'GET',
            '/interview/getInterview?id=1',
            null,
            $response['body']['data']['token']
        );
        print_r($response);
        $this->assertSame(200, $response['status']);
    }
    */
    /*
    public function testSubmitInterview():void{
        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'user2@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );
        print_r($response);



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
            $response['body']['data']['token']
        );

        print_r($response);
    }
    */
        

    public function testAnalyse(){
         $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'user2@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );
        print_r($response);

        $response = $this->http->request(
            'POST',
            '/interviewAttempt/analyse',
            ['attemptId'=>1],
            $response['body']['data']['token']
        );

        print_r($response);
    }
    /*
    public function testDeleteUser():void
    {
        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'user@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );
        $token = $response['body']['data']['token'];
        print_r($response);
        $response = $this->http->request(
            'DELETE',
            '/user/delete',
            ['password' => 'Password123'],
            $token
        );
        print_r($response);

        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'user2@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );
        $token = $response['body']['data']['token'];
        print_r($response);
        $response = $this->http->request(
            'DELETE',
            '/user/delete',
            ['password' => 'Password13'],
            $token
        );

        $this->assertSame(401, $response['status']);
    }
        */
    /*
    public function testUpdatePassword():void
    {
        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'user2@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );

        $response = $this->http->request(
            'PATCH',
            '/user/changePassword',
            ['oldPassword' => 'Password123', 'newPassword' => 'NewPassword123!'],
            $response['body']['data']['token']
        );
        print_r($response);
        $this->assertSame(200, $response['status']);
    }
        */

    
}