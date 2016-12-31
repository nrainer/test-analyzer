package de.tum.in.niedermr.ta.extensions.analysis.mutation.returnvalues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InvalidClassException;
import java.util.Arrays;

import org.junit.Test;
import org.objectweb.asm.Type;

import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.AbstractReturnValueGeneratorTest;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.ClassWithMethodsForMutation;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.SimpleReturnValueGeneratorWith0;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;

/** Test {@link SimpleReturnValueGeneratorWith0}. */
public class CommonInstancesReturnValueGeneratorTest
		extends AbstractReturnValueGeneratorTest<ClassWithMethodsForMutation> {

	private static final String DESCRIPTOR_STRING_ARRAY = "[Ljava.lang.String;";
	private static final String CLASS_NAME_STRING_ARRAY = "java.lang.String[]";

	/** Constructor. */
	public CommonInstancesReturnValueGeneratorTest() throws InvalidClassException, ReflectiveOperationException {
		super(new CommonInstancesReturnValueGenerator(), ClassWithMethodsForMutation.class);
	}

	/** {@inheritDoc} */
	@Override
	protected void verifyMutation(Class<?> mutatedClass, Object instanceOfMutatedClass,
			ClassWithMethodsForMutation instanceOfOriginalClass) throws ReflectiveOperationException {
		CommonReturnValueFactory factory = new CommonReturnValueFactory();
		Object expected;

		expected = factory.get(MethodIdentifier.EMPTY.get(), StringBuilder.class.getName());
		assertEquals(expected.toString(),
				mutatedClass.getMethod("getStringBuilder").invoke(instanceOfMutatedClass).toString());

		expected = factory.get(MethodIdentifier.EMPTY.get(), File.class.getName());
		assertEquals(expected, mutatedClass.getMethod("getFile").invoke(instanceOfMutatedClass));

		expected = factory.get(MethodIdentifier.EMPTY.get(), int.class.getName() + "[]");
		assertTrue(Arrays.equals((int[]) expected,
				(int[]) mutatedClass.getMethod("getIntArray").invoke(instanceOfMutatedClass)));

		expected = factory.get(MethodIdentifier.EMPTY.get(), Object.class.getName() + "[]");
		assertTrue(Arrays.equals((Object[]) expected,
				(Object[]) mutatedClass.getMethod("getObjectArray").invoke(instanceOfMutatedClass)));
	}

	/** Test. */
	@Test
	public void testSupports() throws Exception {
		CommonInstancesReturnValueGenerator retValGen = new CommonInstancesReturnValueGenerator();

		assertTrue(CommonReturnValueFactory.INSTANCE.supports(MethodIdentifier.EMPTY, CLASS_NAME_STRING_ARRAY));
		assertTrue(retValGen.checkReturnValueSupported(MethodIdentifier.EMPTY, Type.getType(DESCRIPTOR_STRING_ARRAY)));
	}
}
