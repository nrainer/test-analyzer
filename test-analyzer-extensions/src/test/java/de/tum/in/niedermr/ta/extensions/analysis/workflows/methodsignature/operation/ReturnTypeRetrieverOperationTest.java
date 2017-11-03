package de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.tests.detector.junit.JUnitTestClassDetector;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

/** Test {@link ReturnTypeRetrieverOperation}. */
public class ReturnTypeRetrieverOperationTest {

	/** Test. */
	@Test
	public void testReturnTypeRetriever() throws Exception {
		ReturnTypeRetrieverOperation operation = new ReturnTypeRetrieverOperation(
				new JUnitTestClassDetector(true, new String[0], new String[0], getClass().getClassLoader()));

		ClassReader cr = new ClassReader(SampleClass.class.getName());
		operation.analyze(cr, JavaUtility.toClassPathWithoutEnding(SampleClass.class));

		Map<MethodIdentifier, String> result = operation.getMethodReturnTypes();
		assertFalse(result.containsKey(MethodIdentifier.create(SampleClass.class, "voidMethod", "()V")));
		assertEquals(Collection.class.getName(),
				result.get(MethodIdentifier.create(SampleClass.class, "getCollection", "()Ljava.util.Collection;")));
		assertEquals(Serializable.class.getName(),
				result.get(MethodIdentifier.create(SampleClass.class, "getSerializable", "()Ljava.io.Serializable;")));
		assertEquals(Integer.class.getName(),
				result.get(MethodIdentifier.create(SampleClass.class, "getInteger", "()Ljava.lang.Integer;")));
		assertFalse(result.containsKey(MethodIdentifier.create(SampleClass.class, "getInt", "()I")));
	}
}
