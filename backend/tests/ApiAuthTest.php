<?php
// tests/AuthTest.php
require_once __DIR__ . '/ApiTestCase.php';

class ApiAuthTest extends ApiTestCase
{
    public function testLogout(): void
    {
        $token = $this->loginAsUser();
        $response = $this->http->request('POST', '/auth/logout', null, $token);
        $this->assertSame(200, $response['status']);
        $response = $this->http->request('POST','/auth/logoutAllDevices',null,null);
        $this->assertSame(200, $response['status']);
    }

    public function testLogoutAllDevices():void
    {
        $token = $this->loginAsAdmin();
        $response = $this->http->request('POST','/auth/logoutAllDevices',null,$token);
        $this->assertSame(200, $response['status']);
        $response = $this->http->request('POST','/auth/logoutAllDevices',null,null);
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
            null
        );
        $this->assertSame(200, $response['status']);

        //test with no bearer token
        $response = $this->http->request(
            'POST',
            '/auth/refresh',
            null
        );

        $this->assertSame(400, $response['status']);
    }

    public function testSignup():void
    {
        //test with bearer token
        $response = $this->http->request(
            'POST',
            '/auth/signup',
            ['email' => 'admin3@example.com', 'password' => 'Password123', "firstname"=>"12","surname"=>"Hello"]
        );
        
        //print_r($response);
        $this->assertSame(400, $response['status']);

        $response = $this->http->request(
            'POST',
            '/auth/signup',
            ['email' => 'admin3@example.com', 'password' => 'Password123!', "firstname"=>"12","surname"=>"Hello"]
        );
        $this->assertSame(200, $response['status']);

        $response = $this->http->request(
            'POST',
            '/auth/signup',
            ['email' => 'admin3@example.com', 'password' => 'Password123!', "firstname"=>"12","surname"=>"Hello"]
        );
        $this->assertSame(400, $response['status']);
    }
}