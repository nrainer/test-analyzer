DROP PROCEDURE IF EXISTS MaterializeViews;

DELIMITER //
CREATE PROCEDURE MaterializeViews ()
BEGIN

DROP TABLE IF EXISTS MV_Tested_Methods_Info;
DROP TABLE IF EXISTS MV_Tested_Methods_Info_Agg;

CREATE TABLE MV_Tested_Methods_Info AS SELECT * FROM V_Tested_Methods_Info;
CREATE TABLE MV_Tested_Methods_Info_Agg AS SELECT * FROM V_Tested_Methods_Info_Agg;

CREATE INDEX mv_tmi_1 ON MV_Tested_Methods_Info(execution);
CREATE INDEX mv_tmi_2 ON MV_Tested_Methods_Info(methodId);
CREATE INDEX mv_tmia_1 ON MV_Tested_Methods_Info_Agg(execution);
CREATE INDEX mv_tmia_2 ON MV_Tested_Methods_Info_Agg(methodId);

END //
DELIMITER ;