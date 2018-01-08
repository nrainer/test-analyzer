package de.tum.in.niedermr.ta.runner.configuration.parser.migration;

import de.tum.in.niedermr.ta.core.analysis.filter.advanced.SetterGetterFilter;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith1;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.VoidReturnValueGenerator;
import de.tum.in.niedermr.ta.core.code.tests.runner.junit.JUnitTestRunner;
import de.tum.in.niedermr.ta.runner.analysis.workflow.TestWorkflow;
import de.tum.in.niedermr.ta.runner.configuration.property.MethodFiltersProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.ReturnValueGeneratorsProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.TestRunnerProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.WorkflowsProperty;
import de.tum.in.niedermr.ta.runner.configuration.property.templates.IConfigurationProperty;

/** Migration for the configuration from version 1 to 2. */
class ConfigurationMigrationFromV1ToV2 implements IConfigurationMigration {

	/** {@inheritDoc} */
	@Override
	public int getFromVersion() {
		return 1;
	}

	/** {@inheritDoc} */
	@Override
	public String migrateKey(String key) {
		switch (key) {
		case "returnValueGeneratorNames":
			return "returnValueGenerators";
		case "testWorkflow":
			return "testWorkflows";
		case "jarsWithMethodsToMutate":
			return "codepathToMutate";
		case "jarsWithTestsToRun":
			return "codepathToTest";
		default:
			return key;
		}
	}

	/** {@inheritDoc} */
	@Override
	public String migrateRawValue(IConfigurationProperty<?> property, String value0) {
		String value;

		if (property instanceof WorkflowsProperty) {
			value = value0.replace("de.tum.in.ma.logic.execution.TestWorkflow", TestWorkflow.class.getName());
		} else if (property instanceof TestRunnerProperty) {
			value = value0.replace("de.tum.in.ma.logic.runner.junit.JUnitTestRunner", JUnitTestRunner.class.getName());
		} else if (property instanceof MethodFiltersProperty) {
			value = value0.replace("de.tum.in.ma.logic.filter.additional.SetterGetterFilter",
					SetterGetterFilter.class.getName());
		} else if (property instanceof ReturnValueGeneratorsProperty) {
			value = value0
					.replace("de.tum.in.ma.logic.mutation.returnValues.VoidReturnValueGenerator",
							VoidReturnValueGenerator.class.getName())
					.replace("de.tum.in.ma.logic.mutation.returnValues.SimpleReturnValueGeneratorWith0",
							SimpleReturnValueGeneratorWith0.class.getName())
					.replace("de.tum.in.ma.logic.mutation.returnValues.SimpleReturnValueGeneratorWith1",
							SimpleReturnValueGeneratorWith1.class.getName())
					.replace("de.tum.in.ma.logic.mutation.returnValues.CommonInstancesReturnValueGenerator",
							"de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues.CommonInstancesReturnValueGenerator");
		} else {
			value = value0;
		}

		return value;
	}
}
