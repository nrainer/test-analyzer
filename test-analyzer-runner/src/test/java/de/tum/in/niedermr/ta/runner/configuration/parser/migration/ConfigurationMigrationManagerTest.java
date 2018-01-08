package de.tum.in.niedermr.ta.runner.configuration.parser.migration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Test;

/** Test {@link ConfigurationMigrationManager}. */
public class ConfigurationMigrationManagerTest {

	/** Test. */
	@Test
	public void testCreateAggregatedMigrationWithRelevantSteps() {
		Collection<IConfigurationMigration> steps = ConfigurationMigrationManager
				.createAggregatedMigrationWithRelevantSteps(2).m_migrations;
		assertFalse(containsStepOfClass(steps, ConfigurationMigrationFromV1ToV2.class));
		assertTrue(containsStepOfClass(steps, ConfigurationMigrationFromV2ToV3.class));
		assertTrue(containsStepOfClass(steps, ConfigurationMigrationFromV3ToV4.class));
	}

	private boolean containsStepOfClass(Collection<IConfigurationMigration> steps,
			Class<? extends IConfigurationMigration> clsOfStep) {
		return steps.stream().anyMatch(step -> step.getClass() == clsOfStep);
	}
}
