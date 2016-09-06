package de.tum.in.niedermr.ta.extensions.analysis.workflows.statistics.steps;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.identifier.TestcaseIdentifier;

public class TestcaseInheritanceHelper {
	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(TestcaseInheritanceHelper.class);

	protected static void postProcessAllTestcases(Map<Class<?>, Set<String>> allTestcases,
			Map<MethodIdentifier, Integer> collectedTestcaseStatistics) {
		for (Entry<Class<?>, Set<String>> entry : allTestcases.entrySet()) {
			Class<?> originalClass = entry.getKey();
			Set<String> testcaseSet = entry.getValue();

			for (String testcase : testcaseSet) {
				postProcessTestcase(originalClass, testcase, collectedTestcaseStatistics);
			}
		}
	}

	private static void postProcessTestcase(Class<?> originalClass, String testcase,
			Map<MethodIdentifier, Integer> collectedTestcaseStatistics) {
		TestcaseIdentifier testIdentifier = TestcaseIdentifier.create(originalClass, testcase);

		if (!collectedTestcaseStatistics.containsKey(testIdentifier.toMethodIdentifier())) {
			try {
				TestcaseInheritanceHelper.tryToFindTestcaseInSuperClass(collectedTestcaseStatistics, testIdentifier);
			} catch (Exception ex) {
				LOGGER.error("Error when trying to find testcase in super class: ", ex);
			}
		}
	}

	private static void tryToFindTestcaseInSuperClass(Map<MethodIdentifier, Integer> testcaseStatistics,
			TestcaseIdentifier testIdentifier) throws Exception {
		Class<?> superClass = testIdentifier.resolveTestClass();

		while (true) {
			superClass = superClass.getSuperclass();

			if (superClass == null || superClass == Object.class) {
				break;
			}

			MethodIdentifier newIdentifier = TestcaseIdentifier.create(superClass, testIdentifier.getTestcaseName())
					.toMethodIdentifier();

			if (testcaseStatistics.containsKey(newIdentifier)) {
				testcaseStatistics.put(testIdentifier.toMethodIdentifier(), testcaseStatistics.get(newIdentifier));
				break;
			}
		}
	}
}
