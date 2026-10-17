<?php
// tests/ApiTestCase.php
use PHPUnit\Framework\TestCase;
require_once __DIR__ . '/HttpTestHelper.php';

abstract class ApiTestCase extends TestCase
{
    protected HttpTestHelper $http;

    protected function setUp(): void
    {
        $this->http = new HttpTestHelper('http://mywebsite.local');
    }

    protected function loginAsAdmin(): string
    {
        return $this->login('admin@example.com', 'Password123');
    }

    protected function loginAsUser(): string
    {
        return $this->login('user@example.com', 'Password123');
    }

    private function login(string $email, string $password): string
    {
        $response = $this->http->request('POST', '/auth/login', [
            'email' => $email,
            'password' => $password,
            'deviceId' => '12',
            'deviceName' => 'Test',
        ]);
        $this->assertSame(200, $response['status'], "Login failed for $email");
        return $response['body']['data']['token'];
    }
}