<?php
    class AnalyticsRepository implements IAnalyticsRepository{
        public function __construct(private Db $db){}
        public function getCompetenciesAnalytics(SortOptions $sortOptions,int $last,int $userId):array|false{
            //only allow these sort collumns
            $sortCollumns = [
                'percentage' => 'competency_percentage',
            ];

            //tries to map otherwise throws errors
            $sortCollumn = $sortOptions->mapField($sortCollumns);

            //CTE get filtered interviews.
            
            //CTE2 uses CTE1 gets the breakdown scores 
            //joins to get the competency breakdowns
            // /5 for max score and average for percentage
            
            //CTE3 uses CTE2 to get overall scores
            //joins to get competency name

            //final query
            // join CTE 2 and CTE 3
            //applies order by
            $query = "WITH filteredInterviews as (
                        SELECT *
                        FROM interview_user_attempts iua
                        WHERE iua.user_id = ?
                        ORDER by iua.completed_at DESC, iua.id DESC
                        LIMIT ?
                    ), breakdown_scores as 
                    (
                        SELECT AVG(qcbc.score)/5 as competency_breakdown_score,
                        qcbc.competency_breakdown_id,
                        cb.competency_id competency_id,
                        cb.name
                        FROM filteredInterviews f
                        JOIN interview_question_attempts iqa
                        ON iqa.interview_attempt_id =f.id
                        JOIN question_competency_breakdown_scores qcbc
                        ON qcbc.question_attempt_id = iqa.id
                        JOIN competency_breakdowns cb 
                        ON qcbc.competency_breakdown_id = cb.id
                        GROUP BY qcbc.competency_breakdown_id
                    ), competency_scores AS
                    (	
                        SELECT 
                        c.id as competency_id,
                        c.name as name,
                        AVG(competency_breakdown_score) competency_score
                        FROM breakdown_scores b
                        JOIN competencies c
                        ON b.competency_id  = c.id
                        GROUP BY b.competency_id
                    )
                    SELECT cs.competency_score as competency_percentage,
                    cs.competency_id as competency_id,
                    bs.competency_breakdown_score as competency_breakdown_percentage,
                    cs.name as competency_name,
                    bs.name as competency_breakdown_name
                    FROM competency_scores cs
                    JOIN breakdown_scores bs
                    ON bs.competency_id = cs.competency_id
                    ORDER BY ". $sortCollumn . " ". $sortOptions->direction .";";
            return $this->db->queryAll($query,[$userId,$last]);
        }
        public function getConceptsAnalytics(SortOptions $sortOptions,int $last,int $userId):array|false{
            //only allow these sort collumns
            $sortCollumns = [
                'percentage' => 'percentage',
            ];

            //tries to map otherwise throws errors
            $sortCollumn = $sortOptions->mapField($sortCollumns);

            //cte to get the interviews.
            //than join to get the concept scores
            //than count and divide by 2 for the max score
            $query = "WITH filteredInterviews as (
                        SELECT *
                        FROM interview_user_attempts iua
                        WHERE iua.user_id = ?
                        ORDER by iua.completed_at DESC, iua.id DESC
                        LIMIT ?
                    )
                    SELECT c.name,
                    AVG(qcs.score)/2 percentage,
                    COUNT(qcs.score) timesTested
                    FROM filteredInterviews f
                    JOIN interview_question_attempts iqa
                    ON iqa.interview_attempt_id =f.id
                    JOIN question_concept_scores qcs
                    ON qcs.question_attempt_id = iqa.id
                    JOIN concepts c
                    ON c.id = qcs.concept_id
                    GROUP BY qcs.concept_id
                    ORDER BY ". $sortCollumn . " ". $sortOptions->direction .";";
            
            return $this->db->queryAll($query,[$userId,$last]);
        }

        public function getUserInterviewMetrics(int $userId):array|false{
            //get the last interviews completed for current month
            //than count total, do sum if checks for total week and if they were difficult.
            //coalesce to ensure the fields will always return a record
            $query = "WITH interviews_this_month AS (
                        SELECT iua.*,i.difficulty
                        FROM interview_user_attempts iua
                        JOIN interviews i
                            ON iua.interview_id= i.id
                        WHERE iua.user_id = ?
                        AND MONTH(iua.completed_at) = MONTH(CURDATE())
                    ) 
                    SELECT COUNT(*) as totalMonth,
                    COALESCE(SUM(interviews_this_month.completed_at >= NOW() - INTERVAL 1 WEEK),0) as totalWeek,
                    COALESCE(SUM(interviews_this_month.difficulty <> 'easy'),0) as totalDifficult
                    FROM interviews_this_month;";
            return $this->db->queryOne($query,[$userId]);
        }
    }