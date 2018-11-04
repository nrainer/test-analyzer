package de.tum.in.niedermr.ta.runner.analysis.instrumentation.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationTargetException;

import org.junit.Before;
import org.junit.Test;

import de.tum.in.niedermr.ta.core.analysis.instrumentation.InvocationLogger;
import de.tum.in.niedermr.ta.core.analysis.instrumentation.InvocationLogger.LoggingMode;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.BiasedTestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.visitor.BytecodeModificationTestUtility;

public class SourceInstrumentationOperationTest {

	private Class<?> m_instrumentedClass;
	private Object m_instanceOfInstrumentedClass;

	@Before
	public void createInstrumentedClass() throws Exception {
		ICodeModificationOperation modificationOperation = new SourceInstrumentationOperation(
				new BiasedTestClassDetector(ClassType.NO_TEST_CLASS), null);
		m_instrumentedClass = BytecodeModificationTestUtility.modifyAndLoadClass(ClassToBeInstrumented.class,
				modificationOperation);
		m_instanceOfInstrumentedClass = m_instrumentedClass.newInstance();

		InvocationLogger.reset();
	}

	/** Test the instrumentation. */
	@Test
	public void testInstrumentationWithPath1() throws Exception {
		InvocationLogger.setMode(LoggingMode.TESTING);

		m_instrumentedClass.getMethod("compute", int.class).invoke(m_instanceOfInstrumentedClass, 0);

		assertEquals(1, InvocationLogger.getTestingLog().size());
		assertEquals(ClassToBeInstrumented.class.getName() + ".compute(int)",
				InvocationLogger.getTestingLog().iterator().next());
	}

	/** Test the instrumentation. */
	@Test
	public void testInstrumentationWithPath2() throws Exception {
		InvocationLogger.setMode(LoggingMode.TESTING);

		m_instrumentedClass.getMethod("compute", int.class).invoke(m_instanceOfInstrumentedClass, 3);
		assertEquals(3, InvocationLogger.getTestingLog().size());
		assertTrue(InvocationLogger.getTestingLog().contains(ClassToBeInstrumented.class.getName() + ".compute(int)"));
		assertTrue(InvocationLogger.getTestingLog()
				.contains(ClassToBeInstrumented.class.getName() + ".computeInternal(int)"));
		assertTrue(InvocationLogger.getTestingLog().contains(ClassToBeInstrumented.class.getName() + ".increase(int)"));
	}

	/** Test the instrumentation. */
	@Test
	public void testInstrumentationWithPath3() throws Exception {
		InvocationLogger.setMode(LoggingMode.TESTING);

		try {
			m_instrumentedClass.getMethod("compute", int.class).invoke(m_instanceOfInstrumentedClass, -1);
			fail("IllegalArgumentException expected");
		} catch (InvocationTargetException e) {
			// expected
			assertTrue(e.getCause() instanceof IllegalArgumentException);
		}
		assertEquals(2, InvocationLogger.getTestingLog().size());
		assertTrue(InvocationLogger.getTestingLog().contains(ClassToBeInstrumented.class.getName() + ".compute(int)"));
		assertTrue(InvocationLogger.getTestingLog()
				.contains(ClassToBeInstrumented.class.getName() + ".handleNegativeValue()"));
	}

}
