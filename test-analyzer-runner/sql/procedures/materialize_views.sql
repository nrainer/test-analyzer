DROP PROCEDURE IF EXISTS MaterializeViews;

DELIMITER //
CREATE PROCEDURE MaterializeViews ()
BEGIN

DROP TABLE IF EXISTS MV_Project_Results;
DROP TABLE IF EXISTS MV_Project_Overview;
DROP TABLE IF EXISTS MV_Tested_Methods_Info;
DROP TABLE IF EXISTS MV_Tested_Methods_Info_Agg;

CREATE TABLE MV_Tested_Methods_Info_Agg AS SELECT * FROM V_Tested_Methods_Info_Agg;
CREATE TABLE MV_Project_Overview AS SELECT * FROM V_Project_Overview;

CREATE INDEX mv_tmia_1 ON MV_Tested_Methods_Info_Agg(execution);
CREATE INDEX mv_tmia_2 ON MV_Tested_Methods_Info_Agg(methodId);

CREATE TABLE MV_Project_Results AS
	SELECT
		e.execution,
		e.project,
		e.testType,
		e.processed,
		e.valid,
		(SELECT COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 1) AS countKilledMethodsFiltered,
		(SELECT COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 0) AS countPseudoTestedMethodsFiltered,
		(SELECT SUM(CASE WHEN vtmia.killedResult = 1 THEN 1 ELSE 0 END) / COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution) AS mutationScoreFiltered,
		(SELECT SUM(CASE WHEN vtmia.killedResult = 1 THEN 1 ELSE 0 END) / COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.minStackDistance >= 5) AS mutationScoreFilteredD5,
		(SELECT SUM(CASE WHEN vtmia.killedResult = 1 THEN 1 ELSE 0 END) / COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.minStackDistance >= 6) AS mutationScoreFilteredD6,
		(SELECT SUM(CASE WHEN vtmia.killedResult = 1 THEN 1 ELSE 0 END) / COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.minStackDistance >= 7) AS mutationScoreFilteredD7,
		(SELECT AVG(vtmia.minStackDistance) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 1) AS avgMinStackDistKilledFilt,
		(SELECT AVG(vtmia.minStackDistance) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 0) AS avgMinStackDistLivingFilt,
		(SELECT AVG(vtmia.sumCountInvocations) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 1) AS avgInvocationCountKilledFilt,
		(SELECT AVG(vtmia.sumCountInvocations) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 0) AS avgInvocationCountLivingFilt,
		(SELECT AVG(vtmia.minNumberOfCoveredMethodsOfAnyTestcase) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 1) AS avgMinNumberOfCoveredMethodsOfAnyTestcaseKilledFilt,
		(SELECT AVG(vtmia.minNumberOfCoveredMethodsOfAnyTestcase) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 0) AS avgMinNumberOfCoveredMethodsOfAnyTestcaseLivingFilt,
		(SELECT AVG(vtmia.avgNumberOfCoveredMethodsOfAnyTestcase) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 1) AS avgAvgNumberOfCoveredMethodsOfAnyTestcaseKilledFilt,
		(SELECT AVG(vtmia.avgNumberOfCoveredMethodsOfAnyTestcase) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 0) AS avgAvgNumberOfCoveredMethodsOfAnyTestcaseLivingFilt
	FROM Execution_Information e
	ORDER BY e.testType, e.project;

END //
DELIMITER ;