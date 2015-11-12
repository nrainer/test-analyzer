package de.tum.in.niedermr.ta.runner.configuration.property.templates;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.tum.in.niedermr.ta.core.common.constants.CommonConstants;

public class MultiStringPropertyTest {
	private static final String SEPARATOR = CommonConstants.SEPARATOR_DEFAULT;
	private static final String VALUE_1 = "a" + SEPARATOR + "b";
	private static final String VALUE_2 = VALUE_1 + SEPARATOR;
	private static final String VALUE_3 = SEPARATOR;

	@Test
	public void testGetElements() {
		MultiStringPropertyStub prop = new MultiStringPropertyStub();

		prop.setValue(VALUE_1);
		assertEquals("b", prop.getElements()[1]);
	}

	@Test
	public void testCountElements() {
		MultiStringPropertyStub prop = new MultiStringPropertyStub();

		prop.setValue(VALUE_1);
		assertEquals(2, prop.countElements());

		prop.setValue(VALUE_2);
		assertEquals(2, prop.countElements());

		prop.setValue(VALUE_3);
		assertEquals(0, prop.countElements());

		prop.setValue(new String[] { "x", "y" });
		assertEquals(2, prop.countElements());
	}

	@Test
	public void testGetWithAlternativeSeparator() {
		MultiStringPropertyStub prop = new MultiStringPropertyStub();

		final String alternativeSeparator = "|";

		prop.setValue(VALUE_1);
		assertEquals(VALUE_1.replace(SEPARATOR, alternativeSeparator),
				prop.getWithAlternativeSeparator(alternativeSeparator));
	}

	class MultiStringPropertyStub extends AbstractMultiStringProperty {

		@Override
		public String getName() {
			return "multiStringProperty";
		}

		@Override
		public String getSeparator() {
			return SEPARATOR;
		}

		@Override
		public String getDescription() {
			return "description";
		}
	}
}
