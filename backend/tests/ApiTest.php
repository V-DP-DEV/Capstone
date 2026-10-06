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
    
    public function testRefreshToken():void
    {
        //test with bearer token
        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'admin@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );
        $token = $response['body']['data']['token'];
        $refreshToken = $response['body']['data']['refreshToken'];
        $response = $this->http->request(
            'POST',
            '/auth/refresh',
            ['refreshToken' => $refreshToken],
            $token
        );
        //print_r($response);
        $this->assertSame(200, $response['status']);

        //test with no bearer token
        $response = $this->http->request(
            'POST',
            '/auth/refresh',
            null
        );

        $this->assertSame(401, $response['status']);
    }
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