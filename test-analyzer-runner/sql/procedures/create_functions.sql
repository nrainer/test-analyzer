DROP FUNCTION IF EXISTS ProjectDisplayText;

CREATE FUNCTION ProjectDisplayText(execution VARCHAR(5), project VARCHAR(64), testType VARCHAR(64))
	RETURNS CHAR(160) DETERMINISTIC
	RETURN CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(project, ' ('), testType),'; '), execution), ')');