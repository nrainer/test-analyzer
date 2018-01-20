-- DROP TABLE IF EXISTS Line_Coverage_Import;
-- DROP TABLE IF EXISTS Method_Location_Import;
-- DROP TABLE IF EXISTS Commit_Info;

CREATE TABLE Commit_Info
(
	commitId INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	projectKey VARCHAR(5) NOT NULL,
	commitHash VARCHAR(40) NOT NULL,
	commitHashShort VARCHAR(5) GENERATED ALWAYS AS (SUBSTRING(commitHash, 1, 5)) VIRTUAL,
	isSourceFileNamesImported TINYINT(1) NOT NULL DEFAULT 0
);

CREATE TABLE Method_Location_Import
(
	id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	projectKey VARCHAR(5) NOT NULL,
	commitHash VARCHAR(40) NOT NULL,
	className VARCHAR(512) NOT NULL COLLATE UTF8_BIN,
	methodShortName VARCHAR(128) NOT NULL COLLATE UTF8_BIN,
	methodDesc VARCHAR(256) NOT NULL COLLATE UTF8_BIN,
    startLine INT(6) NOT NULL,
	fullMethodHash VARCHAR(96) GENERATED ALWAYS AS (CONCAT(MD5(className), MD5(methodShortName), MD5(methodDesc))) VIRTUAL
);

CREATE TABLE Line_Coverage_Import
(
	id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	projectKey VARCHAR(5) NOT NULL,
	commitHash VARCHAR(40) NOT NULL,
	sessionNumber INT(2) NOT NULL,
	packageName VARCHAR(512) NOT NULL COLLATE UTF8_BIN,
	sourceFileName VARCHAR(128) NOT NULL COLLATE UTF8_BIN,
	lineNumber INT(6) NOT NULL,
    coverageState ENUM ('NOT_COVERABLE', 'NOT_COVERED', 'PARTIALLY_COVERED', 'FULLY_COVERED'),
	sourceFilePathHash VARCHAR(64) GENERATED ALWAYS AS (CONCAT(MD5(packageName), MD5(sourceFileName))) VIRTUAL
);

CREATE INDEX idx_ml_imp_1 ON Method_Location_Import(projectKey, commitHash);
CREATE INDEX idx_ml_imp_2 ON Method_Location_Import(startLine);
CREATE INDEX idx_ml_imp_3 ON Method_Location_Import(fullMethodHash);
CREATE INDEX idx_lc_imp_1 ON Line_Coverage_Import(projectKey, commitHash, sessionNumber);
CREATE INDEX idx_lc_imp_2 ON Line_Coverage_Import(sourceFilePathHash);

ALTER TABLE Commit_Info ADD CONSTRAINT uc_comm_info_1 UNIQUE (projectKey, commitHash);
