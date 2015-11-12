package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClasspathPropertyTest {
	@Test
	public void testEmptyString() {
		AbstractClasspathProperty stubProperty = getStubProperty();
		stubProperty.setValue(AbstractStringProperty.EMPTY_STRING);
		assertEquals(AbstractStringProperty.EMPTY_STRING, stubProperty.getValue());
	}

	@Test
	public void testSeparatorIsAppended() {
		AbstractClasspathProperty stubProperty = getStubProperty();
		stubProperty.setValue("value");
		assertEquals("value" + stubProperty.getSeparator(), stubProperty.getValue());
	}

	@Test
	public void testGetElements() {
		AbstractClasspathProperty stubProperty = getStubProperty();
		stubProperty.setValue(" a " + stubProperty.getSeparator() + "b" + stubProperty.getSeparator() + "c"
				+ stubProperty.getSeparator());
		assertEquals(3, stubProperty.countElements());
		assertEquals("a", stubProperty.getElements()[0]);
	}

	private AbstractClasspathProperty getStubProperty() {
		return new AbstractClasspathProperty() {

			@Override
			public String getName() {
				return "stubProperty";
			}

			@Override
			public String getDescription() {
				return "description";
			}
		};
	}
}
