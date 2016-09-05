-- DROP VIEW IF EXISTS V_Method_Classification;
-- DROP VIEW IF EXISTS V_Test_Result_Info;
-- DROP VIEW IF EXISTS V_Tested_Methods_Info_Agg;
-- DROP VIEW IF EXISTS V_Tested_Methods_Info;
-- DROP VIEW IF EXISTS V_Name_Mapping;

/* Mapping between methodId, testcaseId and method (name) and testcase (name). */
CREATE VIEW V_Name_Mapping
(
	execution, 
	methodId, 
	testcaseId, 
	method, 
	testcase,
    methodHash,
    testcaseHash
) AS
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
CREATE VIEW V_Tested_Methods_Info
(
	execution,
	methodId,
	living,
	killed,
	aborted,
	minStackDistance,
	testcaseCount,
	method,
	methodHash
) AS
	SELECT 
		ri.execution, 
		ri.methodId, 
		COALESCE(1 - MIN(tri.killed), 0) AS living, 
		COALESCE(MAX(tri.killed), 0) AS killed, 
		COUNT(mtai.execution) > 0 AS aborted,
		COUNT(DISTINCT ri.testcaseId) AS testcaseCount,
		MIN(ri.minStackDistance), 
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
CREATE VIEW V_Tested_Methods_Info_Agg
(
	execution,
	methodId,
	method,
	killedResult,
	testcaseCount,
	minStackDistance
) AS
	SELECT 
		vtmi.execution, 
		vtmi.methodId, 
		vtmi.method, 
		CASE WHEN vtmi.killed + vtmi.aborted > 0 THEN 1 ELSE 0 END,
		vtmi.testcaseCount,
		vtmi.minStackDistance
    FROM V_Tested_Methods_Info vtmi;
    
/** Test result, extended by test and method ids and names. */
CREATE VIEW V_Test_Result_Info
(
	execution,
	methodId,
	testcaseId,
	retValGenId,
	method,
	testcase,
	killed,
    methodHash,
    testcaseHash
) AS 
	SELECT 
		t.execution,
		t.methodId, 
		t.testcaseId, 
		mapping.method, 
		mapping.testcase, 
		t.retValGenId, 
		t.killed, 
		mapping.methodHash, 
		mapping.testcaseHash
	FROM Test_Result_Info t
	INNER JOIN V_Name_Mapping mapping
	ON t.execution = mapping.execution
	AND t.methodId = mapping.methodId
	AND t.testcaseId = mapping.testcaseId;
	
CREATE VIEW V_Method_Classification
(
	execution,
	methodId,
	method,
	methodCategory,
	methodSeverity
) AS
	SELECT
		vtmia.execution,
		vtmia.methodId,
		vtmia.method,
		mci.category,
		mci.severity
	FROM V_Tested_Methods_Info_Agg vtmia
	INNER JOIN Method_Info mi
	ON vtmia.execution = mi.execution
	AND vtmia.methodId = mi.methodId
	LEFT OUTER JOIN Method_Classification_Info mci
	ON mi.classificationId = mci.classificationId;