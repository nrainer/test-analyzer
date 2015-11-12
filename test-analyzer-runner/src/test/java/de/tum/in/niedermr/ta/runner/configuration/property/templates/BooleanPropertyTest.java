package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BooleanPropertyTest {
	@Test
	public void testProperty() {
		AbstractBooleanProperty prop = new AbstractBooleanProperty() {

			@Override
			public String getName() {
				return "testProperty";
			}

			@Override
			public String getDescription() {
				return "";
			}

			@Override
			protected Boolean getDefault() {
				return false;
			}
		};

		assertFalse(prop.getValue());

		prop.setTrue();
		assertTrue(prop.getValue());
		assertTrue(prop.isTrue());
		assertFalse(prop.isFalse());

		prop.setFalse();
		assertFalse(prop.getValue());
		assertFalse(prop.isTrue());
		assertTrue(prop.isFalse());
	}
}
