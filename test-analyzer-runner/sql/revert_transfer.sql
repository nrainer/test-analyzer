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

UPDATE Execution_Information ei
SET ei.processed = 0
WHERE ei.execution = @executionId;

COMMIT;

END //
DELIMITER ;