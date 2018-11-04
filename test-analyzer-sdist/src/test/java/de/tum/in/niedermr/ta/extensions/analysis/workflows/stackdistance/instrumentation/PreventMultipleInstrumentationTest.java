package de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation;

import org.junit.Test;
import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.code.tests.detector.BiasedTestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.visitor.BytecodeModificationTestUtility;

public class PreventMultipleInstrumentationTest {

	@Test
	public void testSingleInstrumentation() throws Exception {
		instrumentClass(StackDistanceSampleClass.class, true);
	}

	@Test(expected = IllegalStateException.class)
	public void failAfterMultipleInstrumentation() throws Exception {
		applyMultipleInstrumentation(true);
	}

	@Test
	public void noFailAfterMultipleInstrumentation() throws Exception {
		applyMultipleInstrumentation(false);
	}

	protected void applyMultipleInstrumentation(boolean checkForMultipleInstrumentation) throws Exception {
		byte[] instrumentedClassCode = instrumentClass(StackDistanceSampleClass.class, checkForMultipleInstrumentation);
		instrumentClass(new ClassReader(instrumentedClassCode), checkForMultipleInstrumentation);
	}

	protected byte[] instrumentClass(Class<?> classToBeModified, boolean failIfAlreadyInstrumented) throws Exception {
		ClassReader cr = new ClassReader(classToBeModified.getName());
		return instrumentClass(cr, failIfAlreadyInstrumented);
	}

	protected byte[] instrumentClass(ClassReader cr, boolean failIfAlreadyInstrumented) throws Exception {
		AnalysisInstrumentationOperation modificationOperation = new AnalysisInstrumentationOperation(
				new BiasedTestClassDetector(ClassType.NO_TEST_CLASS), StackLogRecorderForTestingPurposes.class);
		modificationOperation.setFailIfAlreadyInstrumented(failIfAlreadyInstrumented);
		return BytecodeModificationTestUtility.modifyAndLoadClassAsBytes(cr, modificationOperation);
	}

}
