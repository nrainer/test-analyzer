package de.tum.in.niedermr.ta.core.analysis.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.analysis.filter.advanced.HashCodeMethodFilter;
import de.tum.in.niedermr.ta.core.analysis.filter.advanced.SetterGetterFilter;
import de.tum.in.niedermr.ta.core.analysis.filter.advanced.SimpleCodeComplexityFilter;
import de.tum.in.niedermr.ta.core.analysis.filter.core.ConstructorFilter;
import de.tum.in.niedermr.ta.core.analysis.filter.core.MethodNameFilter;
import de.tum.in.niedermr.ta.core.analysis.filter.core.NonEmptyMethodFilter;
import de.tum.in.niedermr.ta.core.code.constants.BytecodeConstants;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.sample.SampleClass;
import de.tum.in.niedermr.ta.sample.SampleClassExtended;

public class FilterTest {
	@Test
	public void testSetterGetterFilter1() throws Exception {
		IMethodFilter filter = new SetterGetterFilter();
		String className = SampleClass.class.getName();

		assertFalse(analyze(filter, className, "setX", "(I)V"));
		assertFalse(analyze(filter, className, "getY", "()D"));
		assertFalse(analyze(filter, className, "setY", "(D)V"));
		assertFalse(analyze(filter, className, "getZ", "()Ljava/lang/Object;"));
		assertFalse(analyze(filter, className, "setZ", "(Ljava/lang/Object;)V"));
		assertFalse(analyze(filter, className, "get0", "()I"));
		assertTrue(analyze(filter, className, "setXWithCheck", "(I)V"));
		assertTrue(analyze(filter, className, "getXAndY", "()D"));
	}

	@Test
	public void testSetterGetterFilter2() throws Exception {
		IMethodFilter filter = new SetterGetterFilter();
		String className = SampleClassExtended.class.getName();

		assertFalse(analyze(filter, className, "getX", "()I"));
		assertFalse(analyze(filter, className, "getDefinedConstY", "()I"));
		assertFalse(analyze(filter, className, "getBooleanConst", "()Z"));
		assertFalse(analyze(filter, className, "getIntConst1", "()I"));
		assertFalse(analyze(filter, className, "getIntConst2", "()I"));
		assertFalse(analyze(filter, className, "getShortConst1", "()S"));
		assertFalse(analyze(filter, className, "getShortConst2", "()S"));
		assertFalse(analyze(filter, className, "getLongConst1", "()J"));
		assertFalse(analyze(filter, className, "getLongConst2", "()J"));
		assertFalse(analyze(filter, className, "getDoubleConst", "()D"));
		assertFalse(analyze(filter, className, "getCharConst1", "()C"));
		assertFalse(analyze(filter, className, "getCharConst2", "()C"));
		assertFalse(analyze(filter, className, "getStringConst", "()Ljava/lang/String;"));

		assertTrue(analyze(filter, className, "getCalculation", "()J"));
	}

	@Test
	public void testNonEmptyFilter() throws Exception {
		final String className = SampleClass.class.getName();

		IMethodFilter filter = new NonEmptyMethodFilter();

		MethodNode methodNode;

		methodNode = new MethodNode();
		methodNode.instructions = new InsnList();
		methodNode.instructions.add(new InsnNode(Opcodes.NOP));
		methodNode.maxLocals = 0;
		methodNode.maxStack = 0;

		assertFalse(analyze(filter, className, "empty", "()V", methodNode));

		methodNode = new MethodNode();
		methodNode.instructions = new InsnList();
		methodNode.instructions.add(new InsnNode(Opcodes.ICONST_1));
		methodNode.instructions.add(new InsnNode(Opcodes.IRETURN));
		methodNode.maxLocals = 0;
		methodNode.maxStack = 1;

		assertTrue(analyze(filter, className, "returnTrue", "()Z", methodNode));
	}

	@Test
	public void testSimpleCodeComplexityFilter() throws Exception {
		final String className = SampleClass.class.getName();

		IMethodFilter filter1 = new SimpleCodeComplexityFilter(4);
		IMethodFilter filter2 = new SimpleCodeComplexityFilter(5);
		IMethodFilter filter3 = new SimpleCodeComplexityFilter(6);

		MethodNode methodNode = new MethodNode();
		methodNode.maxLocals = 3;
		methodNode.maxStack = 2;

		assertTrue(analyze(filter1, className, "setX", "(I)V", methodNode));
		assertFalse(analyze(filter2, className, "setX", "(I)V", methodNode));
		assertFalse(analyze(filter3, className, "setX", "(I)V", methodNode));
	}

	@Test
	public void testHashcodeMethodFilter() throws Exception {
		final String className = SampleClass.class.getName();

		IMethodFilter filter = new HashCodeMethodFilter();
		assertFalse(analyze(filter, className, "hashCode", "()I"));
		assertTrue(analyze(filter, className, "toString", "()Ljava/lang/String;"));
	}

	@Test
	public void testConstructorFilter() throws Exception {
		final String className = SampleClass.class.getName();

		IMethodFilter filter = new ConstructorFilter();
		assertFalse(analyze(filter, className, BytecodeConstants.NAME_CONSTRUCTOR, "()V"));
	}

	@Test
	public void testMethodNameFilter() throws Exception {
		final String className = SampleClass.class.getName();

		IMethodFilter filter = new MethodNameFilter(MethodIdentifier.create(className, "getX", "()I"),
				MethodIdentifier.create(className, "setXWithCheck", "(I)V"));

		assertTrue(analyze(filter, className, "getX", "()I"));
		assertTrue(analyze(filter, className, "setXWithCheck", "(I)V"));
		assertFalse(analyze(filter, className, "setX", "(I)V"));
	}

	private boolean analyze(IMethodFilter filter, String className, String methodName, String desc) throws Exception {
		return analyze(filter, className, methodName, desc, new MethodNode());
	}

	private boolean analyze(IMethodFilter filter, String className, String methodName, String desc, MethodNode node)
			throws Exception {
		return filter.apply(MethodIdentifier.create(className, methodName, desc), node).isAccepted();
	}
}
