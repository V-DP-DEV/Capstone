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
        $this->assertSame(200, $response['status']);
        $this->assertTrue($response['body']['success']);
        $this->tokenAdmin = $response['body']['data']['token'];
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

        $response = $this->http->request(
            'POST',
            '/auth/refresh',
            null,
            $token
        );

        $this->assertSame(200, $response['status']);

        //test with no bearer token
        $response = $this->http->request(
            'POST',
            '/auth/refresh',
            null
        );

        $this->assertSame(401, $response['status']);
    }

}