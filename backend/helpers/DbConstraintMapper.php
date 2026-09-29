<?php
class DbConstraintMapper
{
    public static function map(Throwable $e)
    {
        //check if code constraint violation
        if (!self::isConstraintViolationException($e)) {
            //return unmodified error to be handled by error handler
            return $e;
        }
        //get the field and message
        $data = self::getConstraintViolation($e);
        //if not matched return unmodified error the error handler
        if (!$data) {
            return $e;
        }
        //if constraint matched return modified new field validationException
        return new FieldValidationException("Validation errors occurred", $data);
    }

    private static function isConstraintViolationException(Throwable $e): bool
    {

        $code = $e->errorInfo[1] ?? null;
        return $e instanceof PDOException && in_array($code, [1062, 1451, 1452]);
    }

    private static function getConstraintViolation(Throwable $e): array
    {
        //check through entiere map
        foreach (self::MAP as $constraint => $info) {
            //if constraint is matched
            //return the field and message error
            if (strpos($e->errorInfo[2], $constraint) !== false) {
                $data = [];
                $data[$info['field']] = $info['message'];
                return $data;
            }
        }
        return [];
    }

    public const MAP = [
        // Unique constraints
        'uk_users_email' => [
            'field' => 'email',
            'message' => 'The email address is already in use.',
        ],

        'uk_interview_categories_name' => [
            'field' => 'name',
            'message' => 'The category name is already in use.',
        ],

        'uk_question_types_name' => [
            'field' => 'name',
            'message' => 'The question type name is already in use.',
        ],

        'uk_concepts_name' => [
            'field' => 'name',
            'message' => 'The concept name is already in use.',
        ],

        'uk_interview_question_concepts_question_concept' => [
            'field' => 'questionId',
            'message' => 'This question is already associated with this concept.',
        ],

        'uk_competencies_name' => [
            'field' => 'name',
            'message' => 'The competency name is already in use.',
        ],

        'uk_competency_breakdowns_competency_name' => [
            'field' => 'name',
            'message' => 'The competency breakdown name is already in use for this competency.',
        ],

        'uk_qtcb_question_type_breakdown' => [
            'field' => 'questionTypeId',
            'message' => 'This question type is already associated with this competency breakdown.',
        ],

        'uk_interview_question_attempts_attempt_question' => [
            'field' => 'questionId',
            'message' => 'This question has already been answered for this attempt.',
        ],

        'uk_question_feedbacks_attempt_concept' => [
            'field' => 'conceptId',
            'message' => 'Feedback for this concept has already been recorded for this question attempt.',
        ],

        'uk_competency_breakdown_scores_attempt_breakdown' => [
            'field' => 'competencyBreakdownId',
            'message' => 'A score for this competency breakdown has already been recorded for this attempt.',
        ],

        'uk_question_competency_breakdown_scores_attempt_breakdown' => [
            'field' => 'competencyBreakdownId',
            'message' => 'A score for this competency breakdown has already been recorded for this question attempt.',
        ],

        'uk_auth_tokens_token' => [
            'field' => 'token',
            'message' => 'The token already exists.',
        ],

        // Foreign key constraints
        'fk_interviews_category' => [
            'field' => 'categoryId',
            'message' => 'The selected category does not exist.',
        ],

        'fk_interviews_admin' => [
            'field' => 'adminId',
            'message' => 'The selected admin user does not exist.',
        ],

        'fk_interview_questions_interview' => [
            'field' => 'interviewId',
            'message' => 'The selected interview does not exist.',
        ],

        'fk_interview_questions_admin' => [
            'field' => 'adminId',
            'message' => 'The selected admin user does not exist.',
        ],

        'fk_interview_questions_type' => [
            'field' => 'typeId',
            'message' => 'The selected question type does not exist.',
        ],

        'fk_interview_question_concepts_question' => [
            'field' => 'questionId',
            'message' => 'The selected question does not exist.',
        ],

        'fk_interview_question_concepts_concept' => [
            'field' => 'conceptId',
            'message' => 'The selected concept does not exist.',
        ],

        'fk_competency_breakdowns_competency' => [
            'field' => 'competencyId',
            'message' => 'The selected competency does not exist.',
        ],

        'fk_qtcb_question_type' => [
            'field' => 'questionTypeId',
            'message' => 'The selected question type does not exist.',
        ],

        'fk_qtcb_competency_breakdown' => [
            'field' => 'competencyBreakdownId',
            'message' => 'The selected competency breakdown does not exist.',
        ],

        'fk_interview_user_attempts_user' => [
            'field' => 'userId',
            'message' => 'The selected user does not exist.',
        ],

        'fk_interview_user_attempts_interview' => [
            'field' => 'interviewId',
            'message' => 'The selected interview does not exist.',
        ],

        'fk_interview_question_attempts_user_attempt' => [
            'field' => 'userAttemptId',
            'message' => 'The selected user attempt does not exist.',
        ],

        'fk_interview_question_attempts_question' => [
            'field' => 'questionId',
            'message' => 'The selected question does not exist.',
        ],

        'fk_question_feedbacks_question_attempt' => [
            'field' => 'questionAttemptId',
            'message' => 'The selected question attempt does not exist.',
        ],

        'fk_question_feedbacks_concept' => [
            'field' => 'conceptId',
            'message' => 'The selected concept does not exist.',
        ],

        'fk_competency_breakdown_scores_attempt' => [
            'field' => 'attemptId',
            'message' => 'The selected attempt does not exist.',
        ],

        'fk_competency_breakdown_scores_competency_breakdown' => [
            'field' => 'competencyBreakdownId',
            'message' => 'The selected competency breakdown does not exist.',
        ],

        'fk_question_competency_breakdown_scores_question_attempt' => [
            'field' => 'questionAttemptId',
            'message' => 'The selected question attempt does not exist.',
        ],

        'fk_question_competency_breakdown_scores_competency_breakdown' => [
            'field' => 'competencyBreakdownId',
            'message' => 'The selected competency breakdown does not exist.',
        ],

        'fk_auth_tokens_user' => [
            'field' => 'userId',
            'message' => 'The selected user does not exist.',
        ],

        'fk_auth_log_user' => [
            'field' => 'userId',
            'message' => 'The selected user does not exist.',
        ],

        'fk_auth_log_token' => [
            'field' => 'tokenId',
            'message' => 'The selected token does not exist.',
        ],

        'fk_audit_logs_admin' => [
            'field' => 'adminId',
            'message' => 'The selected admin user does not exist.',
        ],
    ];
}
