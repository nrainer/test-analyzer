package de.tum.in.niedermr.ta.core.code.tests.collector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.code.tests.collector.TestCollectorTestData.EmptyClass;
import de.tum.in.niedermr.ta.core.code.tests.collector.TestCollectorTestData.InheritingTestClass;
import de.tum.in.niedermr.ta.core.code.tests.collector.TestCollectorTestData.TestClass1;
import de.tum.in.niedermr.ta.core.code.tests.collector.TestCollectorTestData.TestClass2;
import de.tum.in.niedermr.ta.core.code.tests.runner.junit.JUnitTestRunner;

public class TestCollectorTest {
	private static final String[] EMPTY_PATTERN_STRINGS = new String[0];
	private static final JUnitTestRunner JUNIT_TEST_RUNNER = new JUnitTestRunner();

	/** Test. */
	@Test
	public void testCollectDefault() throws Exception {
		ITestCollector collector = new TestCollector(
				JUNIT_TEST_RUNNER.createTestClassDetector(false, EMPTY_PATTERN_STRINGS, EMPTY_PATTERN_STRINGS));

		String className;

		className = EmptyClass.class.getName();
		collector.analyze(new ClassReader(className), className);

		className = TestClass1.class.getName();
		collector.analyze(new ClassReader(className), className);

		className = TestClass2.class.getName();
		collector.analyze(new ClassReader(className), className);

		Collection<Class<?>> testClasses = collector.getTestClasses();
		assertEquals(2, testClasses.size());
		assertFalse(testClasses.contains(EmptyClass.class));
		assertTrue(testClasses.contains(TestClass1.class));
		assertTrue(testClasses.contains(TestClass2.class));

		Map<Class<?>, Set<String>> classesWithTestCases = collector.getTestClassesWithTestcases();
		assertEquals(2, classesWithTestCases.size());
		assertNotNull(classesWithTestCases.get(TestClass1.class));
		assertNotNull(classesWithTestCases.get(TestClass2.class));
		assertTrue(classesWithTestCases.get(TestClass1.class).contains("a"));
		assertTrue(classesWithTestCases.get(TestClass1.class).contains("b"));
		assertTrue(classesWithTestCases.get(TestClass2.class).contains("a"));
		assertTrue(classesWithTestCases.get(TestClass2.class).contains("c"));
	}

	/** Test. */
	@Test
	public void testCollectInheritedMethods() throws Exception {
		checkCollectInheritedMethodsInternal(true, true);
		checkCollectInheritedMethodsInternal(true, false);
		checkCollectInheritedMethodsInternal(false, true);
		checkCollectInheritedMethodsInternal(false, false);
	}

	private void checkCollectInheritedMethodsInternal(final boolean collectInNonAbstractClasses,
			final boolean collectInAbstractClasses) throws Exception {
		TestCollector collector = new TestCollector(
				JUNIT_TEST_RUNNER.createTestClassDetector(false, EMPTY_PATTERN_STRINGS, EMPTY_PATTERN_STRINGS)) {
			@Override
			public boolean isCollectTestcasesInNonAbstractSuperClasses() {
				return collectInNonAbstractClasses;
			}

			@Override
			public boolean isCollectTestcasesInAbstractSuperClasses() {
				return collectInAbstractClasses;
			}
		};

		String className = InheritingTestClass.class.getName();
		collector.analyze(new ClassReader(className), className);

		Collection<Class<?>> testClasses = collector.getTestClasses();
		assertEquals(1, testClasses.size());
		assertTrue(testClasses.contains(InheritingTestClass.class));

		Map<Class<?>, Set<String>> classesWithTestCases = collector.getTestClassesWithTestcases();
		assertTrue(collector.isCollectTestcasesInAbstractSuperClasses() == classesWithTestCases
				.get(InheritingTestClass.class).contains("a"));
		assertTrue(collector.isCollectTestcasesInNonAbstractSuperClasses() == classesWithTestCases
				.get(InheritingTestClass.class).contains("b"));
		assertTrue(classesWithTestCases.get(InheritingTestClass.class).contains("c"));
	}
}
