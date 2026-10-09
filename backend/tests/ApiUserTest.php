<?php
// tests/AuthTest.php
require_once __DIR__ . '/ApiTestCase.php';

class ApiUserTest extends ApiTestCase
{
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

        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'user2@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );

        print_r($response);
        $this->assertSame(401, $response['status']);
    }

    public function testDeleteUser():void
    {
        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'user2@example.com', 'password' => 'NewPassword123!', "deviceId"=>"12","deviceName"=>"Hello"]
        );
        $token = $response['body']['data']['token'];

        $response = $this->http->request(
            'DELETE',
            '/user/delete',
            ['password' => 'Password123'],
            $token
        );

        print_r($response);
        $this->assertSame(401, $response['status']);

        $response = $this->http->request(
            'DELETE',
            '/user/delete',
            ['password' => 'NewPassword123!'],
            $token
        );

        print_r($response);
        $this->assertSame(200, $response['status']);


        $response = $this->http->request(
            'POST',
            '/auth/login',
            ['email' => 'user2@example.com', 'password' => 'Password123', "deviceId"=>"12","deviceName"=>"Hello"]
        );
        $token = $response['body']['data']['token'];

        print_r($response);
        $this->assertSame(401, $response['status']);
    }
        
    
    
}