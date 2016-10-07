DROP PROCEDURE IF EXISTS MaterializeViews;
DROP FUNCTION IF EXISTS ProjectDisplayText;

CREATE FUNCTION ProjectDisplayText(execution VARCHAR(5), project VARCHAR(64), testType VARCHAR(64))
	RETURNS CHAR(160) DETERMINISTIC
	RETURN CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(project, ' ('), testType),'; '), execution), ')');

DELIMITER //
CREATE PROCEDURE MaterializeViews ()
BEGIN

DROP TABLE IF EXISTS MV_Project_Results;
DROP TABLE IF EXISTS MV_Project_Overview;
DROP TABLE IF EXISTS MV_Tested_Methods_Info_Agg;
DROP TABLE IF EXISTS MV_Tested_Methods_Info;

CREATE TABLE MV_Tested_Methods_Info AS SELECT * FROM V_Tested_Methods_Info;
CREATE TABLE MV_Tested_Methods_Info_Agg AS SELECT * FROM V_Tested_Methods_Info_Agg;
CREATE TABLE MV_Project_Overview AS SELECT * FROM V_Project_Overview;

CREATE INDEX mv_tmi_1 ON MV_Tested_Methods_Info(execution);
CREATE INDEX mv_tmi_2 ON MV_Tested_Methods_Info(methodId);
CREATE INDEX mv_tmia_1 ON MV_Tested_Methods_Info_Agg(execution);
CREATE INDEX mv_tmia_2 ON MV_Tested_Methods_Info_Agg(methodId);

CREATE TABLE MV_Project_Results AS
	SELECT
		e.execution,
		e.project,
		e.testType,
		e.processed,
		e.valid,
		(SELECT COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 1) AS countKilledMethods,
		(SELECT COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 0) AS countPseudoTestedMethods,
		(SELECT SUM(CASE WHEN vtmia.killedResult = 1 THEN 1 ELSE 0 END) / COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution) AS mutationScore,
		(SELECT COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 1 AND vtmia.method NOT LIKE '%hashCode()') AS countKilledMethodsFiltered,
		(SELECT COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 0 AND vtmia.method NOT LIKE '%hashCode()') AS countPseudoTestedMethodsFiltered,
		(SELECT SUM(CASE WHEN vtmia.killedResult = 1 THEN 1 ELSE 0 END) / COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.method NOT LIKE '%hashCode()') AS mutationScoreFiltered,
		(SELECT SUM(CASE WHEN vtmia.killedResult = 1 THEN 1 ELSE 0 END) / COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.method NOT LIKE '%hashCode()' AND vtmia.minStackDistance >= 5) AS mutationScoreFilteredD5,
		(SELECT SUM(CASE WHEN vtmia.killedResult = 1 THEN 1 ELSE 0 END) / COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.method NOT LIKE '%hashCode()' AND vtmia.minStackDistance >= 6) AS mutationScoreFilteredD6,
		(SELECT SUM(CASE WHEN vtmia.killedResult = 1 THEN 1 ELSE 0 END) / COUNT(*) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.method NOT LIKE '%hashCode()' AND vtmia.minStackDistance >= 7) AS mutationScoreFilteredD7,
		(SELECT AVG(vtmia.minStackDistance) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 1) AS avgMinStackDistKilled,
		(SELECT AVG(vtmia.minStackDistance) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 0) AS avgMinStackDistLiving,
		(SELECT AVG(vtmia.minNumberOfCoveredMethodsOfAnyTestcase) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 1) AS avgMinNumberOfCoveredMethodsOfAnyTestcaseKilled,
		(SELECT AVG(vtmia.minNumberOfCoveredMethodsOfAnyTestcase) FROM MV_Tested_Methods_Info_Agg vtmia WHERE vtmia.execution = e.execution AND vtmia.killedResult = 0) AS avgMinNumberOfCoveredMethodsOfAnyTestcaseLiving
	FROM Execution_Information e
	ORDER BY e.testType, e.project;

END //
DELIMITER ;