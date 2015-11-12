package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

public class ClassnamePropertyTest {
	private static final String DEFAULT = "D1";
	private static final String BUILT_IN_ALTERNATIVE = "D2";

	@Test
	public void testDefaultValue() {
		AbstractClassnameProperty<List<?>> stubProperty = getStubProperty();

		try {
			stubProperty.validate();
		} catch (ConfigurationException ex) {
			fail();
		}

		assertEquals(DEFAULT, stubProperty.getValue());
	}

	@Test
	public void testAllowedConstants1() {
		try {
			AbstractClassnameProperty<List<?>> stubProperty = getStubProperty();
			stubProperty.setValue(BUILT_IN_ALTERNATIVE);
			stubProperty.validate();
		} catch (ConfigurationException ex) {
			fail();
		}
	}

	@Test(expected = ConfigurationException.class)
	public void testAllowedConstants2() throws ConfigurationException {
		AbstractClassnameProperty<List<?>> stubProperty = getStubProperty();
		stubProperty.setValue("D3");
		stubProperty.validate();
	}

	@Test
	public void testSetValue() {
		AbstractClassnameProperty<List<?>> stubProperty = getStubProperty();
		stubProperty.setValue(AbstractClassnameProperty.class);

		assertEquals(AbstractClassnameProperty.class.getName(), stubProperty.getValue());
	}

	@Test
	public void testMustBeInstanceOf1() {
		AbstractClassnameProperty<List<?>> stubProperty = getStubProperty();
		stubProperty.setValue(LinkedList.class);

		try {
			stubProperty.validate();
		} catch (ConfigurationException e) {
			fail();
		}
	}

	@Test(expected = ConfigurationException.class)
	public void testMustBeInstanceOf2() throws ConfigurationException {
		AbstractClassnameProperty<List<?>> stubProperty = getStubProperty();
		stubProperty.setValue(HashSet.class);

		stubProperty.validate();
	}

	@Test
	public void testCreateInstance() throws ReflectiveOperationException {
		AbstractClassnameProperty<List<?>> stubProperty = getStubProperty();

		stubProperty.setValue(HashSet.class);
		assertTrue(stubProperty.createInstance() instanceof HashSet);

		stubProperty.setValue((Class<?>) null);
		assertNull(stubProperty.createInstance());
	}

	private AbstractClassnameProperty<List<?>> getStubProperty() {
		return new AbstractClassnameProperty<List<?>>() {

			@Override
			public String getName() {
				return "stubProperty";
			}

			@Override
			public String getDescription() {
				return "description";
			}

			@Override
			protected String getDefault() {
				return DEFAULT;
			}

			@Override
			protected String[] furtherAllowedConstants() {
				return new String[] { DEFAULT, BUILT_IN_ALTERNATIVE };
			}

			@SuppressWarnings("unchecked")
			@Override
			protected Class<? extends List<?>> getRequiredType() {
				return (Class<? extends List<?>>) ((Class<?>) List.class);
			}
		};
	}
}
