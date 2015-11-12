package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import org.junit.Test;

import de.tum.in.niedermr.ta.runner.configuration.exceptions.ConfigurationException;

public class MultiClassnamePropertyTest {
	@Test
	public void testSetValue() {
		AbstractMultiClassnameProperty<Collection<?>> stubProperty = getStubProperty();
		stubProperty.setValue(LinkedList.class, ArrayList.class);

		String sep = stubProperty.getSeparator();
		assertEquals(LinkedList.class.getName() + sep + ArrayList.class.getName() + sep,
				stubProperty.getValueAsString());

	}

	@Test
	public void testValidate1() {
		AbstractMultiClassnameProperty<Collection<?>> stubProperty = getStubProperty();

		try {
			stubProperty.setValue(LinkedList.class, ArrayList.class);
			stubProperty.validate();
		} catch (ConfigurationException e) {
			fail();
		}

		try {
			stubProperty.setValue();
			stubProperty.validate();
		} catch (ConfigurationException e) {
			fail();
		}
	}

	@Test(expected = ConfigurationException.class)
	public void testValidate2() throws ConfigurationException {
		AbstractMultiClassnameProperty<Collection<?>> stubProperty = getStubProperty();
		stubProperty.setValue(HashSet.class, BigInteger.class);

		stubProperty.validate();
	}

	@Test
	public void testCreateInstances() throws ReflectiveOperationException {
		AbstractMultiClassnameProperty<Collection<?>> stubProperty = getStubProperty();
		stubProperty.setValue(LinkedList.class, ArrayList.class);

		Collection<?>[] instances = stubProperty.createInstances();

		assertNotNull(instances);
		assertEquals(2, instances.length);
		assertTrue(instances[0] instanceof LinkedList);
	}

	private AbstractMultiClassnameProperty<Collection<?>> getStubProperty() {
		return new AbstractMultiClassnameProperty<Collection<?>>() {

			@Override
			public String getName() {
				return "stubProperty";
			}

			@Override
			public String getDescription() {
				return "description";
			}

			@SuppressWarnings("unchecked")
			@Override
			protected Class<? extends Collection<?>> getRequiredType() {
				return (Class<? extends Collection<?>>) ((Class<?>) Collection.class);
			}
		};
	}
}
