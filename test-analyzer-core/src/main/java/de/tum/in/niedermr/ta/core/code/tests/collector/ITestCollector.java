package de.tum.in.niedermr.ta.core.code.tests.collector;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import de.tum.in.niedermr.ta.core.code.operation.ICodeAnalyzeOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;

public interface ITestCollector extends ICodeAnalyzeOperation {
	/**
	 * Gets a map with all test classes (with at least one testcase).
	 */
	public Collection<Class<?>> getTestClasses();

	/**
	 * Gets a map with all test classes (with at least one testcase) as key. Each value to a key holds the names (not
	 * the descriptor!) of the testcases in the test class.
	 */
	public Map<Class<?>, Set<String>> getTestClassesWithTestcases();

	/** Get the used test class detector. */
	ITestClassDetector getTestClassDetector();
}