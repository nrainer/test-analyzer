DROP PROCEDURE IF EXISTS RemoveRawImport;

DELIMITER //
CREATE PROCEDURE RemoveRawImport (IN execution VARCHAR(5))
BEGIN

SET @executionId = execution;

START TRANSACTION;

DELETE FROM Collected_Information_Import
WHERE execution = @executionId;

DELETE FROM Test_Result_Import
WHERE execution = @executionId;

DELETE FROM Test_Abort_Import
WHERE execution = @executionId;

DELETE FROM Method_Info_Import
WHERE execution = @executionId;

DELETE FROM Testcase_Info_Import
WHERE execution = @executionId;

DELETE FROM Stack_Info_Import
WHERE execution = @executionId;

DELETE FROM Execution_Information
WHERE execution = @executionId;

COMMIT;

END //
DELIMITER ;