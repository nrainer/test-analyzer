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

public class BytecodeUtilityTest {
	@Test
	public void testGetAcceptedClassNode1() throws IOException {
		Class<?> cls = String.class;

		assertEquals(JavaUtility.toClassPathWithoutEnding(cls.getName()),
				BytecodeUtility.getAcceptedClassNode(cls).name);
	}

	@Test
	public void testGetAcceptedClassNode2() throws IOException {
		String className = String.class.getName();

		assertEquals(JavaUtility.toClassPathWithoutEnding(className),
				BytecodeUtility.getAcceptedClassNode(className).name);
	}

	@Test
	public void testIsAbstractClass() throws ClassNotFoundException, IOException {
		ClassNode cn;

		cn = BytecodeUtility.getAcceptedClassNode(String.class);
		assertFalse(BytecodeUtility.isAbstractClass(cn));

		cn = BytecodeUtility.getAcceptedClassNode(AbstractList.class);
		assertTrue(BytecodeUtility.isAbstractClass(cn));
	}

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
}
