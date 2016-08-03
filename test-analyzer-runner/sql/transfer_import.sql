SET @executionId = '';
SET @removeImportData = 0;

START TRANSACTION;

/* Create an entry for each method. */
INSERT INTO Method_Info
(execution, method)
SELECT @executionId, c.method
FROM Collected_Information_Import c
WHERE c.execution = @executionId
GROUP BY c.method
HAVING SUM(c.processed) = 0;

/* Create an entry for each testcase. */
INSERT INTO Testcase_Info
(execution, testcase)
SELECT @executionId, c.testcase
FROM Collected_Information_Import c
WHERE c.execution = @executionId
GROUP BY c.testcase
HAVING SUM(c.processed) = 0;

/* Create an entry for each entry in Collected_Information. */
INSERT INTO Relation_Info
(execution, methodId, testcaseId)
SELECT @executionId, mi.methodId, ti.testcaseId
FROM Collected_Information_Import c
INNER JOIN Method_Info mi
ON c.method = mi.method
INNER JOIN Testcase_Info ti
ON c.testcase = ti.testcase
WHERE c.execution = @executionId
AND c.processed = 0;

/* Create an entry for each entry in Test_Result. */
INSERT INTO Test_Result_Info
(execution, relationId, retValGen, killed)
SELECT @executionId, mapping.relationId, t.retValGen, t.killed
FROM Test_Result_Import t
INNER JOIN V_Name_Mapping mapping
ON mapping.execution = t.execution
AND mapping.method = t.method
WHERE t.execution = @executionId
AND t.processed = 0;

/* Create an entry for each entry in Test_Abort. */
INSERT INTO Method_Test_Abort_Info
(execution, methodId, retValGen)
SELECT @executionId, mi.methodId, t.retValGen
FROM Test_Abort_Import t
INNER JOIN Method_Info mi
ON t.method = mi.method
WHERE t.execution = @executionId
AND t.processed = 0;

/* Enrich data with stack information. */
UPDATE Relation_Info ri
INNER JOIN V_Name_Mapping mapping
ON ri.relationId = mapping.relationId
AND ri.execution = mapping.execution
INNER JOIN Stack_Info_Import sii
ON sii.method = mapping.method
AND sii.testcase = mapping.testcase
AND sii.execution = mapping.execution
SET ri.minStackDistance = sii.minStackDistance,
ri.maxStackDistance = sii.maxStackDistance
WHERE sii.execution = @executionId;

/* Enrich data with method information: instructions. */
UPDATE Method_Info mi
INNER JOIN V_Name_Mapping mapping
ON mi.methodId = mapping.methodId
AND mi.execution = mapping.execution
INNER JOIN Method_Info_Import mii
ON mii.method = mapping.method
AND mii.execution = mapping.execution
SET mi.instructions = mii.intValue
WHERE mii.execution = @executionId
AND mii.valueName = 'instructions';

/* Enrich data with method information: modifier. */
UPDATE Method_Info mi
INNER JOIN V_Name_Mapping mapping
ON mi.methodId = mapping.methodId
AND mi.execution = mapping.execution
INNER JOIN Method_Info_Import mii
ON mii.method = mapping.method
AND mii.execution = mapping.execution
SET mi.modifier = mii.stringValue
WHERE mii.execution = @executionId
AND mii.valueName = 'modifier';

/* Enrich data with test information: instructions. */
UPDATE Testcase_Info ti
INNER JOIN V_Name_Mapping mapping
ON ti.testcaseId = mapping.testcaseId
AND ti.execution = mapping.execution
INNER JOIN Testcase_Info_Import tii
ON tii.testcase = mapping.testcase
AND tii.execution = mapping.execution
SET ti.instructions = tii.intValue
WHERE tii.execution = @executionId
AND tii.valueName = 'instructions';

/* Enrich data with test information: assertions. */
UPDATE Testcase_Info ti
INNER JOIN V_Name_Mapping mapping
ON ti.testcaseId = mapping.testcaseId
AND ti.execution = mapping.execution
INNER JOIN Testcase_Info_Import tii
ON tii.testcase = mapping.testcase
AND tii.execution = mapping.execution
SET ti.assertions = tii.intValue
WHERE tii.execution = @executionId
AND tii.valueName = 'assertions';

/* Mark all entries in Collected_Information_Import as processed. */
UPDATE Collected_Information_Import c
SET c.processed = 1
WHERE c.execution = @executionId;

/* Mark all entries in Test_Result_Import as processed. */
UPDATE Test_Result_Import t
SET t.processed = 1
WHERE t.execution = @executionId;

/* Mark all entries in Test_Abort_Import as processed. */
UPDATE Test_Abort_Import t
SET t.processed = 1
WHERE t.execution = @executionId;

/* Delete data from Collected_Information_Import if @removeImportData is 1. */
DELETE FROM Collected_Information_Import
WHERE execution = @executionId
AND processed = 1
AND @removeImportData = 1;

/* Delete data from Test_Result_Import if @removeImportData is 1. */
DELETE FROM Test_Result_Import
WHERE execution = @executionId
AND processed = 1
AND @removeImportData = 1;

/* Delete data from Test_Abort_Import if @removeImportData is 1. */
DELETE FROM Test_Abort_Import
WHERE execution = @executionId
AND processed = 1
AND @removeImportData = 1;

UPDATE Execution_Information
SET importProcessed = importProcessed + 1
WHERE execution = @executionId;

COMMIT;