package de.tum.in.niedermr.ta.core.code.operation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.visitor.NoModificationClassVisitor;

/**
 * Base class for a code modification operation that is aware of test classes.
 */
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
		} else if (classType.isSourceClass()) {
			modifySourceClass(cr, cw);
		} else {
			modifyIgnoredTestClass(cr, cw, classType);
		}
	}

	/**
	 * Modify a source class.
	 * 
	 * @param classReader
	 *            class reader
	 * @param classWriter
	 *            class writer
	 */
	protected void modifySourceClass(ClassReader classReader, ClassWriter classWriter) {
		classReader.accept(new NoModificationClassVisitor(Opcodes.ASM5, classWriter), 0);
	}

	/**
	 * Modify a test class.
	 * 
	 * @param classReader
	 *            class reader
	 * @param classWriter
	 *            class writer
	 * @param classType
	 *            type of the class
	 */
	protected void modifyTestClass(ClassReader classReader, ClassWriter classWriter, ClassType classType) {
		classReader.accept(new NoModificationClassVisitor(Opcodes.ASM5, classWriter), 0);
	}

	/**
	 * Modify an ignored test class.
	 * 
	 * @param classReader
	 *            class reader
	 * @param classWriter
	 *            class writer
	 * @param classType
	 *            type of the class
	 */
	protected void modifyIgnoredTestClass(ClassReader classReader, ClassWriter classWriter, ClassType classType) {
		classReader.accept(new NoModificationClassVisitor(Opcodes.ASM5, classWriter), 0);
	}
}
