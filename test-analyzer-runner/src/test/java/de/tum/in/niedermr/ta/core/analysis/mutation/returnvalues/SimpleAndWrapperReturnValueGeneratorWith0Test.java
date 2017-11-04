package de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues;

import static org.junit.Assert.assertEquals;

/** Test {@link SimpleAndWrapperReturnValueGeneratorWith0}. */
public class SimpleAndWrapperReturnValueGeneratorWith0Test
		extends AbstractReturnValueGeneratorTest<ClassWithMethodsForMutation> {

	/** Constructor. */
	public SimpleAndWrapperReturnValueGeneratorWith0Test() {
		super(ClassWithMethodsForMutation.class, new SimpleAndWrapperReturnValueGeneratorWith0());
	}

	/** {@inheritDoc} */
	@Override
	protected void verifyModification(Class<?> mutatedClass, Object instanceOfMutatedClass,
			ClassWithMethodsForMutation instanceOfOriginalClass) throws ReflectiveOperationException {
		SimpleReturnValueGeneratorWith0Test.verifyMutationForSimpleValueMethods(mutatedClass, instanceOfMutatedClass);

		assertEquals(false, mutatedClass.getMethod("getTrueBooleanWrapper").invoke(instanceOfMutatedClass));
		assertEquals((byte) 0, mutatedClass.getMethod("getByteWrapper").invoke(instanceOfMutatedClass));
		assertEquals((short) 0, mutatedClass.getMethod("getShortWrapper").invoke(instanceOfMutatedClass));
		assertEquals(0, mutatedClass.getMethod("getIntegerWrapper").invoke(instanceOfMutatedClass));
		assertEquals(' ', mutatedClass.getMethod("getCharacterWrapper").invoke(instanceOfMutatedClass));
		assertEquals(0L, mutatedClass.getMethod("getLongWrapper").invoke(instanceOfMutatedClass));
		assertEquals(0.0F, mutatedClass.getMethod("getFloatWrapper").invoke(instanceOfMutatedClass));
		assertEquals(0.0, mutatedClass.getMethod("getDoubleWrapper").invoke(instanceOfMutatedClass));
	}
}
