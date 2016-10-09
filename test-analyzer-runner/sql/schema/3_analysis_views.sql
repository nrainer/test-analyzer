-- DROP VIEW IF EXISTS V_Project_Overview;
-- DROP VIEW IF EXISTS V_Project_Overview_Sub;
-- DROP VIEW IF EXISTS V_Method_Classification;
-- DROP VIEW IF EXISTS V_Test_Result_Info;
-- DROP VIEW IF EXISTS V_Tested_Methods_Info_Agg;
-- DROP VIEW IF EXISTS V_Tested_Methods_Info;
-- DROP VIEW IF EXISTS V_Name_Mapping;

/* Mapping between methodId, testcaseId and method (name) and testcase (name). */
CREATE VIEW V_Name_Mapping AS
	SELECT 
		ri.execution, 
		ri.methodId, 
		ri.testcaseId, 
		mi.method, 
		ti.testcase, 
		mi.methodHash, 
		ti.testcaseHash
	FROM Relation_Info ri
	INNER JOIN Method_Info mi
	ON ri.execution = mi.execution
	AND ri.methodId = mi.methodId
	INNER JOIN Testcase_Info ti
	ON ri.execution = ti.execution
	AND ri.testcaseId = ti.testcaseId;
	
/* Methods that were tested (they have a test result or all tests aborted). */
CREATE VIEW V_Tested_Methods_Info AS
	SELECT 
		ri.execution, 
		ri.methodId, 
		COALESCE(1 - MIN(tri.killed), 0) AS living, 
		COALESCE(MAX(tri.killed), 0) AS killed, 
		COUNT(mtai.execution) > 0 AS aborted,
		COUNT(DISTINCT ri.testcaseId) AS testcaseCount,
		MIN(ri.minStackDistance) AS minStackDistance,
		SUM(ri.invocationCount) AS sumCountInvocations,
		-- minimum of: number of methods that a test case (that test-executes this method) covers (out of all methods, also of not investigated ones)
		(SELECT MIN(ti.countCoveredMethods) FROM Testcase_Info ti WHERE ti.execution = ri.execution AND ti.testcaseId IN (
				-- test cases that cover the given method
				SELECT ri2.testcaseId FROM Relation_Info ri2 WHERE ri.execution = ri2.execution AND ri.methodId = ri2.methodId
				)
			) AS minNumberOfCoveredMethodsOfAnyTestcase,
		-- explicit also check for hashCode as fallback for the case that no classification was made
		(mi.method LIKE '%hashCode()' OR mi.classificationId IN (SELECT mci.classificationId FROM Method_Classification_Info mci WHERE mci.isIrrelevant = 1)) AS isIrrelevant,
		mi.method, 
		mi.methodHash
    FROM Relation_Info ri
    INNER JOIN Method_Info mi
    ON ri.execution = mi.execution
    AND ri.methodId = mi.methodId
    LEFT OUTER JOIN Test_Result_Info tri
    ON ri.execution = tri.execution
    AND ri.methodId = tri.methodId
    AND ri.testcaseId = tri.testcaseId
    LEFT OUTER JOIN Method_Test_Abort_Info mtai
    ON ri.execution = mtai.execution
    AND ri.methodId = mtai.methodId
    GROUP BY ri.execution, ri.methodId, mi.method, mi.methodHash
    HAVING COUNT(tri.execution) > 0
    OR COUNT(mtai.execution) > 0;
    
/* Methods that were tested and their aggregated test result. */
CREATE VIEW V_Tested_Methods_Info_Agg AS
	SELECT 
		vtmi.execution, 
		vtmi.methodId, 
		vtmi.method, 
		CASE WHEN vtmi.killed + vtmi.aborted > 0 THEN 1 ELSE 0 END AS killedResult,
		vtmi.isIrrelevant,
		vtmi.testcaseCount,
		vtmi.minStackDistance,
		vtmi.sumCountInvocations,
		vtmi.minNumberOfCoveredMethodsOfAnyTestcase
    FROM V_Tested_Methods_Info vtmi;
    
/** Test result, extended by test and method ids and names. Note that it does not contain test aborts. */
CREATE VIEW V_Test_Result_Info AS 
	SELECT 
		t.execution,
		t.methodId, 
		t.testcaseId, 
		t.retValGenId, 
		mapping.method, 
		mapping.testcase, 
		t.killed, 
		mapping.methodHash, 
		mapping.testcaseHash
	FROM Test_Result_Info t
	INNER JOIN V_Name_Mapping mapping
	ON t.execution = mapping.execution
	AND t.methodId = mapping.methodId
	AND t.testcaseId = mapping.testcaseId;
	
CREATE VIEW V_Method_Classification AS
	SELECT
		vtmia.execution,
		vtmia.methodId,
		vtmia.method,
		mci.category AS methodCategory,
		mci.severity AS methodSeverity,
		mci.isIrrelevant
	FROM V_Tested_Methods_Info_Agg vtmia
	INNER JOIN Method_Info mi
	ON vtmia.execution = mi.execution
	AND vtmia.methodId = mi.methodId
	LEFT OUTER JOIN Method_Classification_Info mci
	ON mi.classificationId = mci.classificationId;
	
CREATE VIEW V_Project_Overview_Sub AS
	SELECT
		e.execution,
		-- number of methods executed by at least one successful test case
		(SELECT COUNT(*) FROM Method_Info mi WHERE mi.execution = e.execution) AS countCollectedMethods,
		-- number of successful test cases
		(SELECT COUNT(*) FROM Testcase_Info ti WHERE ti.execution = e.execution) AS countCollectedTestcases,
		-- number of pairs of successful test cases and their executed methods
		(SELECT COUNT(*) FROM Relation_Info ri WHERE ri.execution = e.execution) AS countRelations,
		-- number of used return value generators
		(SELECT COUNT(*) FROM RetValGen_Info rvg WHERE rvg.execution = e.execution) AS countReturnValueGenerators,
		-- number of test cases that were executed because of at least one covered method that was mutated
		(SELECT COUNT(DISTINCT tri.testcaseId) FROM Test_Result_Info tri WHERE tri.execution = e.execution) AS countExecutedTestcases,
		-- number of methods that were mutated and tested (without test abort)
		(SELECT COUNT(DISTINCT tri.methodId) FROM Test_Result_Info tri WHERE tri.execution = e.execution) AS countTestExecutedMethods,
		-- number of test executed pairs of test cases and methods
		(SELECT COUNT(DISTINCT tri.testcaseId, tri.methodId) FROM Test_Result_Info tri WHERE tri.execution = e.execution) AS countExecutedRelations,
		-- number of test executed test runs for pairs of test cases and methods (some pairs are tested with multiple return value generators
		(SELECT COUNT(*) FROM Test_Result_Info tri WHERE tri.execution = e.execution) AS countExecutedRelationRuns,
		-- number of methods that caused a test abort (eg: timeout)
		(SELECT COUNT(*) FROM Method_Test_Abort_Info mtai WHERE mtai.execution = e.execution) AS countAbortedRelationRuns
	FROM Execution_Information e;
	
CREATE VIEW V_Project_Overview AS
	SELECT
		e.execution,
		e.project,
		e.testType,
		e.processed,
		e.valid,
		pos.countCollectedMethods,
		pos.countCollectedTestcases,
		pos.countRelations,
		pos.countReturnValueGenerators,
		pos.countExecutedTestcases,
		pos.countTestExecutedMethods,
		pos.countExecutedRelations,
		pos.countExecutedRelationRuns,
		pos.countAbortedRelationRuns,
		-- ratio out of covered methods
		(pos.countTestExecutedMethods / pos.countCollectedMethods) AS ratioExecutedOfCoveredMethods,
		-- ratio out of successful test cases
		(pos.countExecutedTestcases / pos.countCollectedTestcases) AS ratioExecutedTestcases,
		(pos.countExecutedRelations / pos.countRelations) AS ratioExecutedRelations,
		(pos.countExecutedRelationRuns / pos.countTestExecutedMethods) AS avgTestPerTestExecutedMethod,
		(SELECT AVG(ti.instructions) FROM Testcase_Info ti WHERE ti.execution = e.execution) AS avgTestcaseInstructions,
		(SELECT AVG(ti.assertions) FROM Testcase_Info ti WHERE ti.execution = e.execution) AS avgTestcaseAssertions,
		(SELECT AVG(mi.bytecodeInstructionCount) FROM Method_Info mi WHERE mi.execution = e.execution) AS avgMethodBytecodeInstructions,
		(SELECT AVG(mi.instructionCount) FROM Method_Info mi WHERE mi.execution = e.execution) AS avgMethodInstructionCount,
		(SELECT AVG(mi.branchCoverage) FROM Method_Info mi WHERE mi.execution = e.execution) AS avgMethodBranchCoverage,
		(SELECT AVG(ri.minStackDistance) FROM Relation_Info ri WHERE ri.execution = e.execution AND (ri.methodId, ri.testcaseId) IN (SELECT r.methodId, r.testcaseId FROM Test_Result_Info r WHERE r.execution = e.execution)) AS avgMinStackDistOfExecuted,
		(SELECT STDDEV(ri.minStackDistance) FROM Relation_Info ri WHERE ri.execution = e.execution AND (ri.methodId, ri.testcaseId) IN (SELECT r.methodId, r.testcaseId FROM Test_Result_Info r WHERE r.execution = e.execution)) AS stddevMinStackDistOfExecuted,
		(SELECT MAX(ri.minStackDistance) FROM Relation_Info ri WHERE ri.execution = e.execution AND (ri.methodId, ri.testcaseId) IN (SELECT r.methodId, r.testcaseId FROM Test_Result_Info r WHERE r.execution = e.execution)) AS absMaxOfMinStackDistOfExecuted,
		(SELECT AVG(ri.invocationCount) FROM Relation_Info ri WHERE ri.execution = e.execution AND (ri.methodId, ri.testcaseId) IN (SELECT r.methodId, r.testcaseId FROM Test_Result_Info r WHERE r.execution = e.execution)) AS avgInvocationOfMethodPerTestcase,
		(SELECT COUNT(*) > 0 FROM Testcase_Info ti WHERE ti.execution = e.execution AND ti.instructions IS NOT NULL AND ti.instructions IS NOT NULL) AS hasTestcaseInformation,
		(SELECT COUNT(*) > 0 FROM Method_Info mi WHERE mi.execution = e.execution AND mi.bytecodeInstructionCount IS NOT NULL AND mi.modifier IS NOT NULL) AS hasMethodInformation,
		(SELECT COUNT(*) > 0 FROM Method_Info mi WHERE mi.execution = e.execution AND mi.instructionCovered IS NOT NULL AND mi.branchCovered IS NOT NULL) AS hasCoverageInformation,
		(SELECT COUNT(*) > 0 FROM Relation_Info ri WHERE ri.execution = e.execution AND ri.minStackDistance IS NOT NULL) AS hasStackDistanceInformation,
		(SELECT COUNT(*) > 0 FROM Relation_Info ri WHERE ri.execution = e.execution AND ri.invocationCount IS NOT NULL) AS hasInvocationCountInformation,
		(SELECT COUNT(*) > 0 FROM Method_Info mi WHERE mi.execution = e.execution AND mi.classificationId IS NOT NULL) AS areMethodsClassified,
		e.description,
		e.notes,
		e.methodCoverage AS projectMethodCoverage,
		e.lineCoverage AS projectLineCoverage,
		e.instructionCoverage AS projectInstructionCoverage,
		e.branchCoverage AS projectBranchCoverage
	FROM Execution_Information e
	INNER JOIN V_Project_Overview_Sub pos
	ON e.execution = pos.execution
	ORDER BY e.testType, e.project;