CREATE OR REPLACE VIEW V_Line_Coverage_Diffs AS
SELECT
	c.commitId, 
    c.projectKey, 
    c.commitHashShort, 
    lci.sourceFileId, 
    lci.lineNumber, 
    COUNT(*) AS countSessions, 
    SUM(lci.isFullyCovered) AS countFullyCovered, 
    SUM(lci.isPartiallyCovered) AS countPartiallyCovered,
    SUM(lci.isNotCovered) AS countNotCovered,
    CASE 
    	WHEN ((SUM(lci.isFullyCovered) > 0) + (SUM(lci.isPartiallyCovered) > 0) + (SUM(lci.isNotCovered) > 0)) = 1 THEN 0 
    	ELSE 1
    END AS isDeviating
FROM Line_Coverage_Info lci
INNER JOIN Commit_Info c
ON lci.commitId = c.commitId
GROUP BY c.commitId, lci.sourceFileId, lci.lineNumber;