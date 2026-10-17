<?php
// tests/AuthTest.php
require_once __DIR__ . '/ApiTestCase.php';

class ApiInterviewTest extends ApiTestCase
{
    public function testGetUserInterviews():void
    {
        $token = $this->loginAsUser();
        $response = $this->http->request(
            'GET',
            '/interview/getInterviews?name=C#',
            null,
            $token
        );
        
        $this->assertSame(200, $response['status']);

        $response = $this->http->request(
            'GET',
            '/interview/getInterviews?name=C#?difficulty=intermediate',
            null,
            $token
        );

        $response = $this->http->request(
            'GET',
            '/interview/getInterviews?name=C#?difficulty=intermediate?categoryId=1',
            null,
            $token
        );
        
        $this->assertSame(200, $response['status']);
        $response = $this->http->request(
            'GET',
            '/interview/getInterviews?name=C#',
            null
        );
        $this->assertSame(401, $response['status']);
    }

    public function testGetInterviewCategories():void
    {
        $response = $this->http->request(
            'GET',
            '/interview/getAllInterviewCategories',
            null,
            null
        );
        $this->assertSame(200, $response['status']);
    }

    public function testGetUserInterview():void{
        $token = $this->loginAsUser();
        $response = $this->http->request(
            'GET',
            '/interview/getInterview?id=1',
            null,
            $token
        );
        $this->assertSame(200, $response['status']);
    }
}