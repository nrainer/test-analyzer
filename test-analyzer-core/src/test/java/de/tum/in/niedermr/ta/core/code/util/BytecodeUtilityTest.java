package de.tum.in.niedermr.ta.core.code.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.sample.SampleClass;
import de.tum.in.niedermr.ta.sample.SampleClass2;
import de.tum.in.niedermr.ta.sample.SampleClass3;
import de.tum.in.niedermr.ta.sample.SampleClass4;
import de.tum.in.niedermr.ta.sample.SampleClass5;

/** Test {@link BytecodeUtility}. */
public class BytecodeUtilityTest {

	/** Test. */
	@Test
	public void testGetAcceptedClassNode1() throws IOException {
		Class<?> cls = String.class;

		assertEquals(JavaUtility.toClassPathWithoutEnding(cls.getName()),
				BytecodeUtility.getAcceptedClassNode(cls).name);
	}

	/** Test. */
	@Test
	public void testGetAcceptedClassNode2() throws IOException {
		String className = String.class.getName();

		assertEquals(JavaUtility.toClassPathWithoutEnding(className),
				BytecodeUtility.getAcceptedClassNode(className).name);
	}

	/** Test. */
	@Test
	public void testIsAbstractClass() throws ClassNotFoundException, IOException {
		ClassNode cn;

		cn = BytecodeUtility.getAcceptedClassNode(String.class);
		assertFalse(BytecodeUtility.isAbstractClass(cn));

		cn = BytecodeUtility.getAcceptedClassNode(AbstractList.class);
		assertTrue(BytecodeUtility.isAbstractClass(cn));
	}

	/** Test. */
	@SuppressWarnings("unchecked")
	@Test
	public void testCountMethodInstructions() throws ClassNotFoundException, IOException {
		final Class<?> cls = SampleClass.class;

		Map<MethodIdentifier, Integer> expected = new HashMap<>();
		expected.put(MethodIdentifier.create(cls, "empty", "()V"), 1);
		expected.put(MethodIdentifier.create(cls, "empty", "(I)V"), 1);
		expected.put(MethodIdentifier.create(cls, "get0", "()I"), 2);
		expected.put(MethodIdentifier.create(cls, "getX", "()I"), 3);
		expected.put(MethodIdentifier.create(cls, "setX", "(I)V"), 4);
		expected.put(MethodIdentifier.create(cls, "throwException", "()V"), 4);

		ClassNode cn = BytecodeUtility.getAcceptedClassNode(cls);

		int countChecks = 0;

		for (MethodNode methodNode : (List<MethodNode>) cn.methods) {
			MethodIdentifier currentIdentifier = MethodIdentifier.create(cls, methodNode);

			Integer currentExpectedCountValue = expected.get(currentIdentifier);

			if (currentExpectedCountValue != null) {
				assertEquals((int) currentExpectedCountValue, BytecodeUtility.countMethodInstructions(methodNode));

				countChecks++;
			}
		}

		assertEquals(expected.size(), countChecks);
	}

	/** Test. */
	@Test
	public void testMethodFlags() throws IOException {
		ClassNode cn = BytecodeUtility.getAcceptedClassNode(SampleClass.class);

		@SuppressWarnings("unchecked")
		List<MethodNode> methods = cn.methods;

		MethodNode constructorMethodNode = methods.stream().filter(method -> "<init>".equals(method.name)).findAny()
				.get();
		assertTrue(BytecodeUtility.isConstructor(constructorMethodNode));

		MethodNode publicGetterMethodNode = methods.stream().filter(method -> "getX".equals(method.name)).findAny()
				.get();
		assertTrue(BytecodeUtility.isPublicMethod(publicGetterMethodNode));
		assertFalse(BytecodeUtility.isConstructor(publicGetterMethodNode));
	}

	/** Test. */
	@Test
	public void testHasParameterlessConstructor() throws IOException {
		ClassNode cn;

		cn = BytecodeUtility.getAcceptedClassNode(SampleClass.class);
		assertTrue(BytecodeUtility.hasPublicParameterlessConstructor(cn));

		cn = BytecodeUtility.getAcceptedClassNode(SampleClass2.class);
		assertFalse(BytecodeUtility.hasPublicParameterlessConstructor(cn));

		cn = BytecodeUtility.getAcceptedClassNode(SampleClass3.class);
		assertTrue(BytecodeUtility.hasPublicParameterlessConstructor(cn));

		cn = BytecodeUtility.getAcceptedClassNode(SampleClass4.class);
		assertFalse(BytecodeUtility.hasPublicParameterlessConstructor(cn));

		cn = BytecodeUtility.getAcceptedClassNode(SampleClass5.class);
		assertTrue(BytecodeUtility.hasPublicParameterlessConstructor(cn));

		cn = BytecodeUtility.getAcceptedClassNode(SampleClass5.Sample5Inner1.class);
		assertTrue(BytecodeUtility.hasPublicParameterlessConstructor(cn));

		cn = BytecodeUtility.getAcceptedClassNode(SampleClass5.Sample5Inner2.class);
		assertTrue(BytecodeUtility.hasPublicParameterlessConstructor(cn));
	}
}
