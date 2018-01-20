SET @projectKey = '###';
SET @commitHash = '###';
SET @sessionNumber = '###';

START TRANSACTION;

INSERT INTO Source_File_Info
(commitId, packageName, sourceFileName)
SELECT 
	c.commitId,
	lci.packageName,
	lci.sourceFileName
FROM Line_Coverage_Import lci
INNER JOIN Commit_Info c
	ON c.projectKey = lci.projectKey
	AND c.commitHash = lci.commitHash
WHERE c.isSourceFileNamesImported = 0
AND c.projectKey = @projectKey
AND c.commitHash = @commitHash
GROUP BY
	c.commitId,
	lci.sourceFilePathHash,
	lci.packageName,
	lci.sourceFileName;

UPDATE Commit_Info c
SET c.isSourceFileNamesImported = 1
WHERE c.projectKey = @projectKey
AND c.commitHash = @commitHash;

INSERT INTO Line_Coverage_Info
(commitId, sessionNumber, sourceFileId, lineNumber, coverageState)
SELECT
	c.commitId,
	lci.sessionNumber,
	sfi.sourceFileId,
	lci.lineNumber,
	lci.coverageState
FROM Line_Coverage_Import lci
INNER JOIN Commit_Info c
	ON c.projectKey = lci.projectKey
	AND c.commitHash = lci.commitHash
INNER JOIN Source_File_Info sfi
	ON sfi.commitId = c.commitId
	AND sfi.sourceFilePathHash = lci.sourceFilePathHash
	AND sfi.packageName = lci.packageName
	AND sfi.sourceFileName = lci.sourceFileName
WHERE c.projectKey = @projectKey
AND c.commitHash = @commitHash
AND lci.sessionNumber = @sessionNumber
AND lci.coverageState <> 'NOT_COVERABLE';

COMMIT;