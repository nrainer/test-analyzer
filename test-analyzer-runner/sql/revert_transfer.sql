DROP PROCEDURE IF EXISTS RevertTransfer;

DELIMITER //
CREATE PROCEDURE RevertTransfer (IN param_execution VARCHAR(5))
BEGIN

SET @executionId = param_execution;

START TRANSACTION;

DELETE FROM Method_Info
WHERE execution = @executionId;

DELETE FROM Testcase_Info
WHERE execution = @executionId;

DELETE FROM Relation_Info
WHERE execution = @executionId;

DELETE FROM Test_Result_Info
WHERE execution = @executionId;

DELETE FROM Method_Test_Abort_Info
WHERE execution = @executionId;

DELETE FROM RetValGen_Info
WHERE execution = @executionId;

/* Mark all entries in Collected_Information_Import as not processed. */
UPDATE Collected_Information_Import c
SET c.processed = 0
WHERE c.execution = @executionId;

/* Mark all entries in Test_Result_Import as not processed. */
UPDATE Test_Result_Import t
SET t.processed = 0
WHERE t.execution = @executionId;

/* Mark all entries in Test_Abort_Import as not processed. */
UPDATE Test_Abort_Import t
SET t.processed = 0
WHERE t.execution = @executionId;

UPDATE Execution_Information
SET processed = 0
WHERE execution = @executionId;

COMMIT;

END //
DELIMITER ;