DROP PROCEDURE IF EXISTS _UpdateMethodInfo;
DROP PROCEDURE IF EXISTS _UpdateTestcaseInfo;

DELIMITER //

CREATE PROCEDURE _UpdateMethodInfo (IN param_execution VARCHAR(5), IN param_sourceColumn VARCHAR(32), IN param_destColumn VARCHAR(32), IN param_valueName VARCHAR(32))
BEGIN
	SET @sql = 
		"UPDATE Method_Info mi
		INNER JOIN Method_Info_Import mix
		ON mi.execution = mix.execution
		AND mi.methodHash = mix.methodHash
		AND mi.method = mix.method
		SET mi.%destColumn% = mix.%sourceColumn%
		WHERE mix.execution = '%execution%'
		AND mix.valueName = '%valueName%';";

	SET @sql = REPLACE(@sql, '%destColumn%', param_destColumn);
	SET @sql = REPLACE(@sql, '%sourceColumn%', param_sourceColumn);
	SET @sql = REPLACE(@sql, '%execution%', param_execution);
	SET @sql = REPLACE(@sql, '%valueName%', param_valueName);
		
	PREPARE stmt FROM @sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
END //

/* Needed by procedure Transfer. */
CREATE PROCEDURE _UpdateTestcaseInfo (IN param_execution VARCHAR(5), IN param_sourceColumn VARCHAR(32), IN param_destColumn VARCHAR(32), IN param_valueName VARCHAR(32))
BEGIN
	
	SET @sql = 
		"UPDATE Testcase_Info ti
		INNER JOIN Testcase_Info_Import tix
		ON ti.execution = tix.execution
		AND ti.testcaseHash = tix.testcaseHash
		AND ti.testcase = tix.testcase
		SET ti.%destColumn% = tix.%sourceColumn%
		WHERE tix.execution = '%execution%'
		AND tix.valueName = '%valueName%';";

	SET @sql = REPLACE(@sql, '%destColumn%', param_destColumn);
	SET @sql = REPLACE(@sql, '%sourceColumn%', param_sourceColumn);
	SET @sql = REPLACE(@sql, '%execution%', param_execution);
	SET @sql = REPLACE(@sql, '%valueName%', param_valueName);
		
	PREPARE stmt FROM @sql;
	EXECUTE stmt;
	DEALLOCATE PREPARE stmt;
END //
DELIMITER ;