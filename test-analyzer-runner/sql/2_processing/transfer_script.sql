/** Transfers raw data imports to data structures optimized for analyses. */

SET @executionId = '###';

START TRANSACTION;

/* Create an entry for each method. */
INSERT INTO Method_Info
(execution, method)
SELECT c.execution, c.method
FROM Collected_Information_Import c
WHERE c.execution = @executionId
GROUP BY c.methodHash, c.method;

/* Create an entry for each testcase. */
INSERT INTO Testcase_Info
(execution, testcase)
SELECT c.execution, c.testcase
FROM Collected_Information_Import c
WHERE c.execution = @executionId
GROUP BY c.testcaseHash, c.testcase;

/* Create an entry for each entry in Collected_Information. */
INSERT INTO Relation_Info
(execution, methodId, testcaseId)
SELECT c.execution, mi.methodId, ti.testcaseId
FROM Collected_Information_Import c
INNER JOIN Method_Info mi
ON c.execution = mi.execution
AND c.methodHash = mi.methodHash
AND c.method = mi.method
INNER JOIN Testcase_Info ti
ON c.execution = ti.execution
AND c.testcaseHash = ti.testcaseHash
AND c.testcase = ti.testcase
WHERE c.execution = @executionId;

/* Create an entry for each return value generator. */
INSERT INTO RetValGen_Info
(execution, retValGen)
SELECT DISTINCT tri.execution, tri.retValGen
FROM Test_Result_Import tri
WHERE tri.execution = @executionId;

/* Create an entry for each entry in Test_Result. */
INSERT INTO Test_Result_Info
(execution, methodId, testcaseId, retValGenId, killed)
SELECT t.execution, mapping.methodId, mapping.testcaseId, rvg.retValGenId, t.killed
FROM Test_Result_Import t
INNER JOIN V_Name_Mapping mapping
ON mapping.execution = t.execution
AND mapping.methodHash = t.methodHash
AND mapping.testcaseHash = t.testcaseHash
AND mapping.method = t.method
AND mapping.testcase = t.testcase
INNER JOIN RetValGen_Info rvg
ON mapping.execution = rvg.execution
AND t.retValGenHash = rvg.retValGenHash
AND t.retValGen = rvg.retValGen
WHERE t.execution = @executionId;

/* Create an entry for each entry in Test_Abort. */
INSERT INTO Method_Test_Abort_Info
(execution, methodId, retValGenId)
SELECT mi.execution, mi.methodId, rvg.retValGenId
FROM Test_Abort_Import t
INNER JOIN Method_Info mi
ON t.execution = mi.execution
AND t.methodHash = mi.methodHash
AND t.method = mi.method
INNER JOIN RetValGen_Info rvg
ON t.execution = rvg.execution
AND t.retValGen = rvg.retValGen
WHERE t.execution = @executionId;

/* Create an entry for each entry in Pit_Mutation_Result_Import. */
INSERT INTO Pit_Mutation_Info
(importedMutationId, execution, mutatedMethod, mutatorName, mutationStatus, killingTestcase, methodId, testcaseId)
SELECT 
	pmr.id,
	pmr.execution,
	pmr.mutatedMethod,
	pmr.mutatorName,
	pmr.mutationStatus,
	pmr.killingTestcase,
	mi.methodId,
	ti.testcaseId
FROM Pit_Mutation_Result_Import pmr
LEFT OUTER JOIN Method_Info mi
ON pmr.execution = mi.execution
AND pmr.methodHash = mi.methodHash
AND pmr.mutatedMethod = mi.method
LEFT OUTER JOIN Testcase_Info ti
ON pmr.execution = ti.execution
AND pmr.testcaseHash = ti.testcaseHash
AND pmr.killingTestcase = ti.testcase
WHERE pmr.execution = @executionId;

/* Enrich data with stack information. */
UPDATE Relation_Info ri
INNER JOIN V_Name_Mapping mapping
ON ri.execution = mapping.execution
AND ri.methodId = mapping.methodId
AND ri.testcaseId = mapping.testcaseId
INNER JOIN Stack_Info_Import sii
ON sii.execution = mapping.execution
AND sii.methodHash = mapping.methodHash
AND sii.testcaseHash = mapping.testcaseHash
AND sii.method = mapping.method
AND sii.testcase = mapping.testcase
SET ri.minStackDistance = sii.minStackDistance,
	ri.maxStackDistance = sii.maxStackDistance,
	ri.invocationCount = sii.invocationCount
WHERE sii.execution = @executionId;

/** Update the number of covered methods per testcase (must be done after filling Relation_Info). */
UPDATE Testcase_Info ti
SET ti.countCoveredMethods = (SELECT COUNT(ri.methodId) FROM Relation_Info ri WHERE ri.execution = ti.execution AND ri.testcaseId = ti.testcaseId) 
WHERE ti.execution = @executionId;

/* Enrich data with method information: bytecode instructions. */
CALL _UpdateMethodInfo(@executionId, 'intValue', 'bytecodeInstructionCount', 'instructions');

/* Enrich data with method information: access modifier. */
CALL _UpdateMethodInfo(@executionId, 'stringValue', 'modifier', 'modifier');

/* Enrich data with method information: covered lines. */
CALL _UpdateMethodInfo(@executionId, 'intValue', 'lineCovered', 'cov_line_covered');
CALL _UpdateMethodInfo(@executionId, 'intValue', 'lineCount', 'cov_line_all');

/* Enrich data with method information: covered instructions. */
CALL _UpdateMethodInfo(@executionId, 'intValue', 'instructionCovered', 'cov_instruction_covered');
CALL _UpdateMethodInfo(@executionId, 'intValue', 'instructionCount', 'cov_instruction_all');

/* Enrich data with method information: covered branches. */
CALL _UpdateMethodInfo(@executionId, 'intValue', 'branchCovered', 'cov_branch_covered');
CALL _UpdateMethodInfo(@executionId, 'intValue', 'branchCount', 'cov_branch_all');

/* Enrich data with test information: instructions. */
CALL _UpdateTestcaseInfo(@executionId, 'intValue', 'instructions', 'instructions');

/* Enrich data with test information: assertions. */
CALL _UpdateTestcaseInfo(@executionId, 'intValue', 'assertions', 'assertions');

/* Mark the execution as processed. */
UPDATE Execution_Information ei
SET ei.processed = 1
WHERE ei.execution = @executionId;

COMMIT;

/* List potentially non-unique entries. */
SELECT 'Method_Info.method' AS location, @executionId AS execution, m.methodHash
FROM Method_Info m
WHERE m.execution = @executionId
GROUP BY m.methodHash
HAVING COUNT(*) > 1
UNION ALL
SELECT 'TestCase_Info.testcase' AS location, @executionId AS execution, t.testcaseHash
FROM Testcase_Info t
WHERE t.execution = @executionId
GROUP BY t.testcaseHash
HAVING COUNT(*) > 1
UNION ALL
SELECT 'RetValGen_Info.retValGen' AS location, @executionId AS execution, r.retValGenHash
FROM RetValGen_Info r
WHERE r.execution = @executionId
GROUP BY r.retValGenHash
HAVING COUNT(*) > 1;