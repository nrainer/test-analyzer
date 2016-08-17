package de.tum.in.niedermr.ta.core.code.operation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;

/** Base class for a code modification operation that is aware of test classes. */
public abstract class AbstractTestAwareCodeModificationOperation extends AbstractTestAwareCodeOperation
		implements ICodeModificationOperation {

	/** Constructor. */
	public AbstractTestAwareCodeModificationOperation(ITestClassDetector testClassDetector) {
		super(testClassDetector);
	}

	/** {@inheritDoc} */
	@Override
	public void modify(ClassReader cr, ClassWriter cw) {
		ClassType classType = analyzeClassType(cr);

		if (classType.isTestClass()) {
			modifyTestClass(cr, cw, classType);
		} else {
			modifySourceClass(cr, cw);
		}
	}

	protected abstract void modifySourceClass(ClassReader cr, ClassWriter cw);

	protected abstract void modifyTestClass(ClassReader cr, ClassWriter cw, ClassType classType);
}
