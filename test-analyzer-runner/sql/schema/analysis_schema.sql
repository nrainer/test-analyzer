-- DROP TABLE IF EXISTS Method_Classification_Info;
-- DROP TABLE IF EXISTS Method_Test_Abort_Info;
-- DROP TABLE IF EXISTS Test_Result_Info;
-- DROP TABLE IF EXISTS RetValGen_Info;
-- DROP TABLE IF EXISTS Relation_Info;
-- DROP TABLE IF EXISTS Testcase_Info;
-- DROP TABLE IF EXISTS Method_Info;

CREATE TABLE Method_Info
(
	methodId INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	execution VARCHAR(5) NOT NULL REFERENCES Execution_Information(execution),
	method VARCHAR(1024) NOT NULL COLLATE UTF8_BIN,
    classificationId INT REFERENCES Method_Classification_Info(classificationId),
    modifier VARCHAR(10),
    bytecodeInstructionCount INT(8),
    statementCount INT(6),
    statementCovered INT(6),
    instructionCount INT(6),
    instructionCovered INT(6),
    branchCount INT(6),
    branchCovered INT(6),
    statementCoverage DECIMAL(5, 4) GENERATED ALWAYS AS (CASE WHEN statementCovered IS NULL THEN NULL WHEN statementCount = 0 THEN 1.0 ELSE statementCount / statementCovered END) VIRTUAL,
    instructionCoverage DECIMAL(5, 4) GENERATED ALWAYS AS (CASE WHEN instructionCovered IS NULL THEN NULL WHEN instructionCount = 0 THEN 1.0 ELSE instructionCount / instructionCovered END) VIRTUAL,
    branchCoverage DECIMAL(5, 4) GENERATED ALWAYS AS (CASE WHEN branchCovered IS NULL THEN NULL WHEN branchCount = 0 THEN 1.0 ELSE branchCount / branchCovered END) VIRTUAL,
	methodHash VARCHAR(32) GENERATED ALWAYS AS (MD5(method)) VIRTUAL
);

CREATE TABLE Testcase_Info
(
	testcaseId INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	execution VARCHAR(5) NOT NULL REFERENCES Execution_Information(execution),
	testcase VARCHAR(1024) NOT NULL COLLATE UTF8_BIN,
    instructions INT(8),
    assertions INT(8),
    testcaseHash VARCHAR(32) GENERATED ALWAYS AS (MD5(testcase)) VIRTUAL
);

CREATE TABLE Relation_Info
(
	execution VARCHAR(5) NOT NULL REFERENCES Execution_Information(execution),
	methodId INT(11) NOT NULL REFERENCES Method_Info(methodId),
	testcaseId INT(11) NOT NULL REFERENCES Testcase_Info(testcaseId),
	minStackDistance INT(8),
	maxStackDistance INT(8),
	-- the execution does not need to be part of the primary key since methodId and testcaseId are already unique
	PRIMARY KEY (methodId, testcaseId)
);

CREATE TABLE RetValGen_Info
(
	retValGenId INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	execution VARCHAR(5) NOT NULL REFERENCES Execution_Information(execution),
	retValGen VARCHAR(256) NOT NULL,
	retValGenHash VARCHAR(32) GENERATED ALWAYS AS (MD5(retValGen)) VIRTUAL
);

CREATE TABLE Test_Result_Info
(
	resultId INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	execution VARCHAR(5) NOT NULL REFERENCES Execution_Information(execution),
	methodId INT(11) NOT NULL REFERENCES Method_Info(methodId),
	testcaseId INT(11) NOT NULL REFERENCES Testcase_Info(testcaseId),
	retValGenId INT(11) NOT NULL REFERENCES RetValGen_Info(retValGenId),
	killed TINYINT(1) NOT NULL
);

CREATE TABLE Method_Test_Abort_Info
(
	abortId INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	execution VARCHAR(5) NOT NULL REFERENCES Execution_Information(execution),
	methodId INT(11) NOT NULL REFERENCES Method_Info(methodId),
	retValGenId INT(11) NOT NULL REFERENCES RetValGen_Info(retValGenId)
);

CREATE TABLE Method_Classification_Info
(
  classificationId int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  category VARCHAR(64) NOT NULL,
  description VARCHAR(64),
  example VARCHAR(64),
  severity VARCHAR(12)
);

CREATE INDEX idx_aly_mi_1 ON Method_Info(execution, methodHash);
CREATE INDEX idx_aly_mi_2 ON Method_Info(classificationId);
CREATE INDEX idx_aly_ti_1 ON Testcase_Info(execution, testcaseHash);
CREATE INDEX idx_aly_ri_1 ON Relation_Info(execution, methodId, testcaseId);
CREATE INDEX idx_aly_ri_2 ON Relation_Info(testcaseId);
CREATE INDEX idx_aly_rvgi_1 ON RetValGen_Info(execution, retValGenHash);
CREATE INDEX idx_aly_tri_1 ON Test_Result_Info(execution, methodId, testcaseId);
CREATE INDEX idx_aly_tri_2 ON Test_Result_Info(testcaseId);

ALTER TABLE Relation_Info ADD CONSTRAINT uc_aly_ri_1 UNIQUE (execution, methodId, testcaseId);
ALTER TABLE Test_Result_Info ADD CONSTRAINT uc_aly_tri_1 UNIQUE (execution, methodId, testcaseId, retValGenId);
ALTER TABLE Method_Test_Abort_Info ADD CONSTRAINT uc_aly_mai_1 UNIQUE (execution, methodId, retValGenId);
ALTER TABLE Method_Classification_Info ADD CONSTRAINT uc_aly_mci_1 UNIQUE (name);