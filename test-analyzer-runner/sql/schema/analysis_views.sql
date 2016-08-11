-- DROP VIEW IF EXISTS V_Test_Result_Info;
-- DROP VIEW IF EXISTS V_Method_State_Info_Extended;
-- DROP VIEW IF EXISTS V_Method_State_Info;
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
	SELECT ri.execution, ri.methodId, ri.testcaseId, mi.method, ti.testcase, mi.methodHash, ti.testcaseHash
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
	methodId
) AS
	SELECT DISTINCT t.execution, t.methodId
	FROM Test_Result_Info t
	UNION
	SELECT DISTINCT m.execution, m.methodId
	FROM Method_Test_Abort_Info m;
	
/* Test result of methods that were successfully tested. */
CREATE VIEW V_Method_State_Info
(
	execution,
	methodId,
	method,
	killed,
    methodHash
) AS 
	SELECT t.execution, mi.methodId, mi.method, SUM(t.killed) > 0, mi.methodHash
	FROM Test_Result_Info t
	INNER JOIN Method_Info mi
	ON t.execution = mi.execution
	AND t.methodId = mi.methodId
	GROUP BY t.execution, mi.methodId, mi.methodHash, mi.method;

/** Test result of methods for which tests were started (and run successfully or were aborted). */
CREATE VIEW V_Method_State_Info_Extended
(
	execution,
	methodId,
	method,
	testCompleted,
	killed,
	aborted,
    methodHash
) AS 
	SELECT tmi.execution, tmi.methodId, mapping.method, COUNT(tr.killed) > 0, COALESCE(SUM(tr.killed) > 0, 0), COUNT(ta.methodId) > 0, mapping.methodHash
	FROM V_Tested_Methods_Info tmi
	INNER JOIN V_Name_Mapping mapping
	ON tmi.execution = mapping.execution
	AND tmi.methodId = mapping.methodId
	LEFT OUTER JOIN Test_Result_Info tr
	ON tr.execution = tmi.execution
	AND tr.methodId = mapping.methodId
	AND tr.testcaseId = mapping.testcaseId
	AND mapping.methodId = tmi.methodId
	LEFT OUTER JOIN Method_Test_Abort_Info ta
	ON ta.execution = tmi.execution
	AND ta.methodId = tmi.methodId
	GROUP BY tmi.execution, tmi.methodId, mapping.methodHash, mapping.method;
	
/** Test result, extended by test and method ids and names. */
CREATE VIEW V_Test_Result_Info
(
	execution,
	methodId,
	testcaseId,
	method,
	testcase,
	retValGenId,
	killed,
    methodHash,
    testcaseHash
) AS 
	SELECT t.execution, mapping.methodId, mapping.testcaseId, mapping.method, mapping.testcase, t.retValGenId, t.killed, mapping.methodHash, mapping.testcaseHash
	FROM Test_Result_Info t
	INNER JOIN V_Name_Mapping mapping
	ON t.execution = mapping.execution
	AND t.methodId = mapping.methodId
	AND t.testcaseId = mapping.testcaseId;