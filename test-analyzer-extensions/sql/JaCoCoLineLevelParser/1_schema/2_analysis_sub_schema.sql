-- DROP TABLE IF EXISTS Line_Coverage_Info;
-- DROP TABLE IF EXISTS Source_File_Info;

CREATE TABLE Source_File_Info
(
	sourceFileId INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	commitId INT(11) NOT NULL REFERENCES Commit_Info(commitId),
	packageName VARCHAR(512) NOT NULL COLLATE UTF8_BIN,
	sourceFileName VARCHAR(128) NOT NULL COLLATE UTF8_BIN,
	sourceFilePathHash VARCHAR(64) GENERATED ALWAYS AS (CONCAT(MD5(packageName), MD5(sourceFileName))) VIRTUAL
);

-- NOT_COVERABLE not allowed on purpose
CREATE TABLE Line_Coverage_Info
(
	id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	commitId INT(11) NOT NULL REFERENCES Commit_Info(commitId),
	sessionNumber INT(2) NOT NULL,
	sourceFileId INT(11) NOT NULL REFERENCES Source_File_Info(sourceFileId),
	lineNumber INT(6) NOT NULL,
    coverageState ENUM ('NOT_COVERED', 'PARTIALLY_COVERED', 'FULLY_COVERED'),
    isFullyCovered TINYINT(1) GENERATED ALWAYS AS (coverageState = 'FULLY_COVERED') VIRTUAL,
    isPartiallyCovered TINYINT(1) GENERATED ALWAYS AS (coverageState = 'PARTIALLY_COVERED') VIRTUAL,
    isNotCovered TINYINT(1) GENERATED ALWAYS AS (coverageState = 'NOT_COVERED') VIRTUAL
);

CREATE INDEX idx_sf_info_1 ON Source_File_Info(commitId);
CREATE INDEX idx_sf_info_2 ON Source_File_Info(sourceFilePathHash);

ALTER TABLE Line_Coverage_Info ADD CONSTRAINT uc_lc_info_1 UNIQUE (commitId, sessionNumber, sourceFileId, lineNumber);
