-- DROP TABLE IF EXISTS Execution_Information;
-- DROP TABLE IF EXISTS Collected_Information_Import;
-- DROP TABLE IF EXISTS Test_Result_Import;
-- DROP TABLE IF EXISTS Test_Abort_Import;
-- DROP TABLE IF EXISTS Stack_Info_Import;
-- DROP TABLE IF EXISTS Method_Info_Import;
-- DROP TABLE IF EXISTS Testcase_Info_Import;

CREATE TABLE Execution_Information
(
	id int(5) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	execution varchar(5) NOT NULL,
	date date NOT NULL,
	project varchar(64) NOT NULL,
	description varchar(512),
	notes varchar(512),
	configurationContent text,
	importProcessed tinyint(1) NOT NULL DEFAULT 0
);

CREATE TABLE Collected_Information_Import
(
	id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	execution varchar(5) NOT NULL,
	method varchar(1024) NOT NULL COLLATE utf8_bin,
	testcase varchar(1024) NOT NULL COLLATE utf8_bin,
	processed tinyint(1) NOT NULL DEFAULT 0
 );

CREATE TABLE Test_Result_Import
(
	id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	execution varchar(5) NOT NULL,
	testcase varchar(1024) NOT NULL COLLATE utf8_bin,
	method varchar(1024) NOT NULL COLLATE utf8_bin,
	retValGen varchar(256) NOT NULL,
	killed tinyint(1) NOT NULL,
	assertErr tinyint(1),
	exception varchar(256),
	processed tinyint(1) NOT NULL DEFAULT 0
);

CREATE TABLE Test_Abort_Import
(
	id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	execution VARCHAR(5) NOT NULL,
	method VARCHAR(1024) COLLATE utf8_bin,
	retValGen VARCHAR(256),
	cause VARCHAR(32),
	processed tinyint(1) NOT NULL DEFAULT 0
);

CREATE TABLE Stack_Info_Import
(
	id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	execution VARCHAR(5) NOT NULL,
	testcase VARCHAR(1024) COLLATE utf8_bin,
	method VARCHAR(1024) COLLATE utf8_bin,
	minStackDistance INT(8),
	maxStackDistance INT(8)
);

CREATE TABLE Method_Info_Import
(
	id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	execution VARCHAR(5) NOT NULL,
	method VARCHAR(1024) NOT NULL COLLATE utf8_bin,
	intValue INT(8),
	stringValue VARCHAR(20) COLLATE utf8_bin,
	valueName VARCHAR(20) NOT NULL
);

CREATE TABLE Testcase_Info_Import
(
	id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
	execution VARCHAR(5) NOT NULL,
	testcase VARCHAR(1024) COLLATE utf8_bin,
	intValue INT(8),
	stringValue VARCHAR(20) COLLATE utf8_bin,
	valueName VARCHAR(20) NOT NULL
);

CREATE INDEX idx_ei_1 ON Execution_Information(execution);
CREATE INDEX idx_ci_1 ON Collected_Information_Import(execution);
CREATE INDEX idx_ci_2 ON Collected_Information_Import(method(50));
CREATE INDEX idx_ci_3 ON Collected_Information_Import(testcase(50));
CREATE INDEX idx_tr_1 ON Test_Result_Import(execution);
CREATE INDEX idx_tr_2 ON Test_Result_Import(method(50));
CREATE INDEX idx_tr_3 ON Test_Result_Import(testcase(50));
CREATE INDEX idx_ta_1 ON Test_Abort_Import(execution);
CREATE INDEX idx_ta_2 ON Test_Abort_Import(method(50));
CREATE INDEX idx_sii_1 ON Stack_Info_Import(execution);
CREATE INDEX idx_sii_2 ON Stack_Info_Import(method(50));
CREATE INDEX idx_sii_3 ON Stack_Info_Import(testcase(50));
CREATE INDEX idx_mii_1 ON Method_Info_Import(execution);
CREATE INDEX idx_mii_2 ON Method_Info_Import(method(50));
CREATE INDEX idx_tii_1 ON Testcase_Info_Import(execution);
CREATE INDEX idx_tii_2 ON Testcase_Info_Import(testcase(50));