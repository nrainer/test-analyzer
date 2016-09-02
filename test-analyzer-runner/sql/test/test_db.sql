DROP PROCEDURE IF EXISTS AssertEquals;

DELIMITER //
CREATE PROCEDURE AssertEquals (IN expectedValue INT, IN actualValue INT)
BEGIN
	IF expectedValue != actualValue THEN 
     SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Assertion error';
	END IF;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS TestDatabase;

DELIMITER //
CREATE PROCEDURE TestDatabase ()
BEGIN

CALL RevertTransfer('TEST');
CALL RemoveRawImport('TEST');

INSERT INTO Execution_Information (execution, date, project, configurationContent) VALUES ('TEST', CURRENT_DATE(), '?', '[...]');
UPDATE Execution_Information SET notes = '54 methods. 15 processed successfully. 39 skipped. 0 with timeout. 0 failed.' WHERE execution = 'TEST';

INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResultAsString()', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringCorrect()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResultAsString()', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringReturnNotNull()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult2()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.intArray()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringCorrect()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.clear()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.sub()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringReturnNotNull()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.increment()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationTrivialTests.emptyAtBeginning()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.parse()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add5()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.constructor()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add0()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.resultAsList()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult2()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.clear()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.sub()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.increment()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationTrivialTests.emptyAtBeginning()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.parse()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add5()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.constructor()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add0()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.resultAsList()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult2()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.clear()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.clear()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation$1.compareTo(de.tum.in.ma.simpleproject.core.Calculation)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.comparable()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult2()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.increment()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.clear()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add5()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.sub()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add0()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.resultAsList()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.isEven()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation$1.compareTo(java.lang.Object)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.comparable()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.getResultAsList()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.resultAsList()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.parse(java.lang.String)', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.parse()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.getComparable()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.comparable()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.getResultAsArray()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.intArray()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.trimString(java.lang.String)', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.parse()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.increment()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.increment()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.increment()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.increment()', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()');
INSERT INTO Collected_Information_Import (execution, method, testcase) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.sub(int)', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.sub()');

INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringCorrect()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResultAsString()', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0', true, true, 'org.junit.ComparisonFailure');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringReturnNotNull()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResultAsString()', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0', false, false, '');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringCorrect()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResultAsString()', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith1', true, true, 'org.junit.ComparisonFailure');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringReturnNotNull()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResultAsString()', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith1', false, false, '');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0', true, false, 'java.lang.StackOverflowError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0', true, false, 'java.lang.StackOverflowError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith1', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith1', false, false, '');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith1', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult2()', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.clear()', 'de.tum.in.ma.simpleproject.core.Calculation.clear()', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult2()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', false, false, '');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.increment()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.clear()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add5()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.sub()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add0()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', false, false, '');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.resultAsList()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', false, false, '');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.isEven()', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.isEven()', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith1', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive()', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive()', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith1', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.parse()', 'de.tum.in.ma.simpleproject.core.Calculation.trimString(java.lang.String)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0', true, false, 'java.lang.NumberFormatException');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.parse()', 'de.tum.in.ma.simpleproject.core.Calculation.trimString(java.lang.String)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith1', true, false, 'java.lang.NumberFormatException');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.increment()', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.increment()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.increment()', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.increment()', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', false, false, '');
INSERT INTO Test_Result_Import (execution, testcase, method, retValGen, killed, assertErr, exception) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.sub()', 'de.tum.in.ma.simpleproject.core.Calculation.sub(int)', 'de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator', true, true, 'java.lang.AssertionError');

INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.sub()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 3);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.sub()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 3);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.sub()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.sub()', 'de.tum.in.ma.simpleproject.core.Calculation.sub(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add0()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add0()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add0()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.constructor()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.constructor()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.clear()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.clear()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.clear()', 'de.tum.in.ma.simpleproject.core.Calculation.clear()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.clear()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.increment()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 3);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.increment()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 3);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.increment()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 2, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.increment()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.increment()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', 2, 3);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 3);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 3);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.increment()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.intArray()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 2, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.intArray()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.intArray()', 'de.tum.in.ma.simpleproject.core.Calculation.getResultAsArray()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult2()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult2()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult2()', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult2()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.isEven()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 2, 3);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 3);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 2, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.increment()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', 2, 3);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 3);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 3);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', 2, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add5()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add5()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add5()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.resultAsList()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 2, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.resultAsList()', 'de.tum.in.ma.simpleproject.core.Calculation.getResultAsList()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.resultAsList()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.resultAsList()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.comparable()', 'de.tum.in.ma.simpleproject.core.Calculation$1.compareTo(java.lang.Object)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.comparable()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.comparable()', 'de.tum.in.ma.simpleproject.core.Calculation.getComparable()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.comparable()', 'de.tum.in.ma.simpleproject.core.Calculation$1.compareTo(de.tum.in.ma.simpleproject.core.Calculation)', 2, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationTrivialTests.emptyAtBeginning()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationTrivialTests.emptyAtBeginning()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringCorrect()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResultAsString()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringCorrect()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringCorrect()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringReturnNotNull()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResultAsString()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringReturnNotNull()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 2, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringReturnNotNull()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.parse()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.parse()', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', 1, 2);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.parse()', 'de.tum.in.ma.simpleproject.core.Calculation.parse(java.lang.String)', 1, 1);
INSERT INTO Stack_Info_Import (execution, testcase, method, minStackDistance, maxStackDistance) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.parse()', 'de.tum.in.ma.simpleproject.core.Calculation.trimString(java.lang.String)', 2, 2);

INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.TimeoutInTest.testDieIfMutated()', '6', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.staticMethod()', '2', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', '3', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.system.Mathematics.faculty(java.lang.String)', '7', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', '37', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.clear()', '4', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8.lambda$1(java.lang.Integer)', '7', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.MiscellaneousTests.setUp()', '5', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.returnFiveForTestNotToCauseATimeout()', '2', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.add(int,int,int)', '6', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation$1.compareTo(java.lang.Object)', '5', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SetUpAndTearDownTests.beforeClass()', '3', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SpecialTest2Object.callAfter()', '1', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.even()', '16', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SetUpAndTearDownTests.afterClass()', '3', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.HasFailingTest.successfulTest()', '5', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.emptyAtBeginning()', '7', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.sub(int)', '5', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.MiscellaneousTests.testWithTryCatchBlock()', '11', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.add5()', '17', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', '6', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8.usePredicateLambda()', '9', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation$1.compareTo(de.tum.in.ma.simpleproject.core.Calculation)', '2', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8Test.testPredicateLambda()', '10', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.MiscellaneousTests.testTryCatchBlock()', '13', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive()', '4', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8.multiApplyFunction(java.util.function.Function,int,int)', '18', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8.lambda$0(java.lang.Integer)', '7', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SetUpAndTearDownTests.after()', '3', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SpecialTest2Object.callBeforeClass()', '1', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.system.MathematicsTests.testFaculty1()', '35', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.JUnitRulesTest.access$0()', '2', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.system.Mathematics.faculty(de.tum.in.ma.simpleproject.core.Calculation)', '28', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.IgnoredTestClassTests.testInIgnoredClass()', '11', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SpecialTest2Object.callAfterClass()', '1', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.JUnitRulesTest$MyStatement.evaluate()', '8', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.increment()', '4', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.compareTo(java.lang.Object)', '5', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResultAsString()', '4', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.setUp()', '6', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8.applyMultiplyWithEight(int)', '6', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', '4', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.MiscellaneousTests.testNoReturnStatement()', '4', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', '7', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.stringCorrect()', '9', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.HasFailingTest.failingTest()', '5', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.JUnitRulesTest$MyRule.apply(org.junit.runners.model.Statement,org.junit.runner.Description)', '7', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SetUpAndTearDownTests.testBeforeAndAfterDontInstrument()', '6', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.parse(java.lang.String)', '7', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.HierarchyTestsSuper.testInInheritableNonAbstractClass()', '10', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.tryCatchBlock(java.lang.String)', '6', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.HierarchyTestsAbstract.testInAbstractClass()', '10', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.add0()', '17', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.MiscellaneousTests.testStaticMethod()', '5', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.MiscellaneousTests.testSyntheticBride()', '15', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.compareTo(java.lang.Integer)', '3', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.JUnitRulesTest.testRun()', '6', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8.apply(java.util.function.Predicate,int)', '9', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SetUpAndTearDownTests.before()', '3', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.isEven()', '9', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.TimeoutInTest.testTimeoutIfMutated()', '6', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SpecialTest2Object.callBefore()', '1', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SpecialTest2Object.callTest()', '2', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.HierarchyTestsInherited.testInInheritingClass()', '10', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.getResultAsList()', '12', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.increment()', '16', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.getComparable()', '5', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.getResultAsArray()', '8', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.noReturnStatement()', '4', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.trimString(java.lang.String)', '3', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8Test.testMultiplyEight()', '11', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.returnFiveForTestNotToExit()', '2', NULL, 'instructions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.system.MathematicsTests.testFaculty2()', '6', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()', '14', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.comparable()', '10', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()', '15', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult2()', '15', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.sub()', '15', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()', '21', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationTrivialTests.emptyAtBeginning()', '8', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add0()', '17', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringCorrect()', '9', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.parse()', '13', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.clear()', '26', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringReturnNotNull()', '5', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', '34', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add5()', '17', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.increment()', '16', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.intArray()', '18', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.constructor()', '20', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.resultAsList()', '22', NULL, 'instructions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.even()', '2', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.comparable()', '1', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult3()', '1', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult2()', '1', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.sub()', '1', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.mult1()', '2', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationTrivialTests.emptyAtBeginning()', '1', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add0()', '2', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringCorrect()', '1', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.parse()', '2', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.clear()', '3', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationStringTests.stringReturnNotNull()', '1', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.positive()', '5', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add5()', '2', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.increment()', '2', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.intArray()', '3', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.constructor()', '2', NULL, 'assertions');
INSERT INTO Testcase_Info_Import (execution, testcase, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.resultAsList()', '3', NULL, 'assertions');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.TimeoutInTest.testDieIfMutated()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.staticMethod()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResult()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.system.Mathematics.faculty(java.lang.String)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.mult(int)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.clear()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8.lambda$1(java.lang.Integer)', NULL, 'private', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.MiscellaneousTests.setUp()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.returnFiveForTestNotToCauseATimeout()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.add(int,int,int)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation$1.compareTo(java.lang.Object)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SetUpAndTearDownTests.beforeClass()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SpecialTest2Object.callAfter()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.even()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SetUpAndTearDownTests.afterClass()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.HasFailingTest.successfulTest()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.emptyAtBeginning()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.sub(int)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.MiscellaneousTests.testWithTryCatchBlock()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.add5()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)', NULL, 'private', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8.usePredicateLambda()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation$1.compareTo(de.tum.in.ma.simpleproject.core.Calculation)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8Test.testPredicateLambda()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.MiscellaneousTests.testTryCatchBlock()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.isPositive()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8.multiApplyFunction(java.util.function.Function,int,int)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8.lambda$0(java.lang.Integer)', NULL, 'private', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SetUpAndTearDownTests.after()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SpecialTest2Object.callBeforeClass()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.system.MathematicsTests.testFaculty1()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.JUnitRulesTest.access$0()', NULL, 'default', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.system.Mathematics.faculty(de.tum.in.ma.simpleproject.core.Calculation)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.IgnoredTestClassTests.testInIgnoredClass()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SpecialTest2Object.callAfterClass()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.JUnitRulesTest$MyStatement.evaluate()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.increment()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.compareTo(java.lang.Object)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.getResultAsString()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.setUp()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8.applyMultiplyWithEight(int)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.setResult(int)', NULL, 'protected', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.MiscellaneousTests.testNoReturnStatement()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.add(int)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.stringCorrect()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.HasFailingTest.failingTest()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.JUnitRulesTest$MyRule.apply(org.junit.runners.model.Statement,org.junit.runner.Description)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SetUpAndTearDownTests.testBeforeAndAfterDontInstrument()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.parse(java.lang.String)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.HierarchyTestsSuper.testInInheritableNonAbstractClass()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.tryCatchBlock(java.lang.String)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.HierarchyTestsAbstract.testInAbstractClass()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.add0()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.MiscellaneousTests.testStaticMethod()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.MiscellaneousTests.testSyntheticBride()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.compareTo(java.lang.Integer)', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.JUnitRulesTest.testRun()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8.apply(java.util.function.Predicate,int)', NULL, 'protected', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SetUpAndTearDownTests.before()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLite.isEven()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.TimeoutInTest.testTimeoutIfMutated()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SpecialTest2Object.callBefore()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.SpecialTest2Object.callTest()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.HierarchyTestsInherited.testInInheritingClass()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.getResultAsList()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.lite.CalculationLiteTests.increment()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.getComparable()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.getResultAsArray()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.noReturnStatement()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.core.Calculation.trimString(java.lang.String)', NULL, 'private', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Java8Test.testMultiplyEight()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.special.Special.returnFiveForTestNotToExit()', NULL, 'public', 'modifier');
INSERT INTO Method_Info_Import (execution, method, intValue, stringValue, valueName) VALUES ('TEST', 'de.tum.in.ma.simpleproject.system.MathematicsTests.testFaculty2()', NULL, 'public', 'modifier');

CALL Transfer('TEST');

/* BEGIN TESTS */

SELECT COUNT(*) INTO @actualCount
FROM Method_Info;
CALL AssertEquals(18, @actualCount);

SELECT bytecodeInstructionCount INTO @actualCount
FROM Method_Info
WHERE method = 'de.tum.in.ma.simpleproject.core.Calculation.isPositive()';
CALL AssertEquals(4, @actualCount);

SELECT COUNT(*) INTO @actualCount
FROM RetValGen_Info;
CALL AssertEquals(3, @actualCount);

SELECT COUNT(*) INTO @actualCount
FROM Relation_Info;
CALL AssertEquals(65, @actualCount);

SELECT COUNT(*) INTO @actualCount
FROM Test_Result_Info;
CALL AssertEquals(36, @actualCount);

SELECT COUNT(*) INTO @actualCount
FROM Testcase_Info;
CALL AssertEquals(18, @actualCount);

SELECT instructions INTO @actualCount
FROM Testcase_Info
WHERE testcase = 'de.tum.in.ma.simpleproject.core.CalculationDefaultTests.add0()';
CALL AssertEquals(17, @actualCount);

SELECT COUNT(*) INTO @actualCount
FROM V_Name_Mapping;
CALL AssertEquals(65, @actualCount);

SELECT COUNT(*) INTO @actualCount
FROM V_Test_Result_Info;
CALL AssertEquals(36, @actualCount);

SELECT COUNT(*) INTO @actualCount
FROM V_Tested_Methods_Info;
CALL AssertEquals(10, @actualCount);

SELECT COUNT(*) INTO @actualCount
FROM V_Tested_Methods_Info
WHERE method = 'de.tum.in.ma.simpleproject.lite.CalculationLite.isEven()'
AND living = 0 AND killed = 1 AND aborted = 0 AND minStackDistance = 1;
CALL AssertEquals(1, @actualCount);

SELECT COUNT(*) INTO @actualCount
FROM V_Tested_Methods_Info
WHERE method = 'de.tum.in.ma.simpleproject.core.Calculation.isPositive(int)'
AND living = 1 AND killed = 1 AND aborted = 0 AND minStackDistance = 2;
CALL AssertEquals(1, @actualCount);

/* END TESTS */

/* Cleanup */
CALL RevertTransfer('TEST');
CALL RemoveRawImport('TEST');

END //
DELIMITER ;

CALL TestDatabase();