DROP PROCEDURE IF EXISTS MergeRawImports;

DELIMITER //
CREATE PROCEDURE MergeRawImports (IN param_execution_1 VARCHAR(5), IN param_execution_2 VARCHAR(5), IN param_execution_target VARCHAR(5), IN param_include_coverage_info TINYINT(1))
BEGIN

SET @executionIdOld1 = param_execution_1;
SET @executionIdOld2 = param_execution_2;
SET @executionIdTarget = param_execution_target;
SET @includeCoverageInfo = param_execution_target;

START TRANSACTION;

INSERT INTO Collected_Information_Import
(execution, method, testcase)
SELECT @executionIdTarget, cii1.method, cii1.testcase
FROM Collected_Information_Import cii1
WHERE cii1.execution = @executionIdOld1
UNION
SELECT @executionIdTarget, cii2.method, cii2.testcase
FROM Collected_Information_Import cii2
WHERE cii2.execution = @executionIdOld2;

-- test cases are not supposed to be overlapping
INSERT INTO Test_Result_Import
(execution, testcase, method, retValGen, killed, assertErr, exception)
SELECT @executionIdTarget, tri1.testcase, tri1.method, tri1.retValGen, tri1.killed, tri1.assertErr, tri1.exception
FROM Test_Result_Import tri1
WHERE tri1.execution = @executionIdOld1
UNION 
SELECT @executionIdTarget, tri2.testcase, tri2.method, tri2.retValGen, tri2.killed, tri2.assertErr, tri2.exception
FROM Test_Result_Import tri2
WHERE tri2.execution = @executionIdOld2;

INSERT INTO Test_Abort_Import
(execution, method, retValGen, cause)
SELECT @executionIdTarget, tai1.method, tai1.retValGen, tai1.cause
FROM Test_Abort_Import tai1
WHERE tai1.execution = @executionIdOld1
UNION 
SELECT @executionIdTarget, tai2.method, tai2.retValGen, tai2.cause
FROM Test_Abort_Import tai2
WHERE tai2.execution = @executionIdOld2;

INSERT INTO Method_Info_Import
(execution, method, intValue, stringValue, valueName)
SELECT @executionIdTarget, mii1.method, mii1.intValue, mii1.stringValue, mii1.valueName
FROM Method_Info_Import mii1
WHERE mii1.execution = @executionIdOld1
AND (@includeCoverageInfo OR mii1.valueName NOT LIKE 'cov_%')
UNION
SELECT @executionIdTarget, mii2.method, mii2.intValue, mii2.stringValue, mii2.valueName
FROM Method_Info_Import mii2
WHERE mii2.execution = @executionIdOld2
AND (@includeCoverageInfo OR mii2.valueName NOT LIKE 'cov_%');

INSERT INTO Testcase_Info_Import
(execution, testcase, intValue, stringValue, valueName)
SELECT @executionIdTarget, tii1.testcase, tii1.intValue, tii1.stringValue, tii1.valueName
FROM Testcase_Info_Import tii1
WHERE tii1.execution = @executionIdOld1
UNION
SELECT @executionIdTarget, tii2.testcase, tii2.intValue, tii2.stringValue, tii2.valueName
FROM Testcase_Info_Import tii2
WHERE tii2.execution = @executionIdOld2;

INSERT INTO Stack_Info_Import
(execution, testcase, method, minStackDistance, maxStackDistance)
SELECT @executionIdTarget, x.testcase, x.method, MIN(x.minStackDistance), MAX(x.maxStackDistance)
FROM
(
SELECT sii1.testcase, sii1.method, sii1.minStackDistance, sii1.maxStackDistance
FROM Stack_Info_Import sii1
WHERE sii1.execution = @executionIdOld1
UNION 
SELECT sii2.testcase, sii2.method, sii2.minStackDistance, sii2.maxStackDistance
FROM Stack_Info_Import sii2
WHERE sii2.execution = @executionIdOld2
) x
GROUP BY x.testcase, x.method;

INSERT INTO Execution_Information
(execution, valid, date, project, description)
SELECT 
  @executionIdTarget,
  (SELECT MIN(ei.valid) FROM Execution_Information ei WHERE ei.execution IN (@executionIdOld1, @executionIdOld2)),
  CURRENT_DATE(),
  (SELECT ei1.project FROM Execution_Information ei1 WHERE ei1.execution = @executionIdOld1),
  CONCAT('Merge of ', CONCAT(@executionIdOld1, CONCAT(' and ', @executionIdOld2)));

COMMIT;

END //
DELIMITER ;