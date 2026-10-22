<?php
// tests/AuthTest.php
require_once __DIR__ . '/ApiTestCase.php';

class ApiAnalyticsTest extends ApiTestCase
{
    public function testGetConceptsAnalytics():void
    {
        $token = $this->loginAsUser();
        $response = $this->http->request(
            'GET',
            '/analytics/concepts?sortBy=percentage&sortOrder=ASC&lastInterviews=5',
            null,
            $token
        );
        print_r($response);
        $this->assertSame(200, $response['status']);
    }

    public function testGetCompetenciesAnalytics():void
    {
        $token = $this->loginAsUser();
        $response = $this->http->request(
            'GET',
            '/analytics/competencies?sortBy=percentage&sortOrder=ASC&lastInterviews=5',
            null,
            $token
        );
        print_r($response);
        $this->assertSame(200, $response['status']);
    }

    public function testGetUserDashboard():void{
        $token = $this->loginAsUser();
        $response = $this->http->request(
            'GET',
            '/analytics/userDashboard',
            null,
            $token
        );
        print_r($response);
        $this->assertSame(200, $response['status']);
    }
}