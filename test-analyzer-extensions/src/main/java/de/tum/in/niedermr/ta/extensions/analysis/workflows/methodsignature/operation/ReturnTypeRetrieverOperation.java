package de.tum.in.niedermr.ta.extensions.analysis.workflows.methodsignature.operation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.operation.AbstractTestAwareCodeAnalyzeOperation;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.core.code.util.BytecodeUtility;
import de.tum.in.niedermr.ta.core.code.util.Identification;

/**
 * Operation to retrieve the declared return types of methods.<br/>
 * Does not include constructors, abstract methods and methods with void return
 * type!
 */
public class ReturnTypeRetrieverOperation extends AbstractTestAwareCodeAnalyzeOperation {

	/** Methods and their return type. */
	private final Map<MethodIdentifier, String> m_methodReturnTypes;

	/** Constructor. */
	public ReturnTypeRetrieverOperation(ITestClassDetector testClassDetector) {
		super(testClassDetector);
		m_methodReturnTypes = new HashMap<>();
	}

	/** {@inheritDoc} */
	@Override
	protected void analyzeSourceClass(ClassNode cn, String originalClassPath) {
		analyzeReturnTypes(cn);
	}

	/** Analyze the return types of the methods of the class. */
	@SuppressWarnings("unchecked")
	private void analyzeReturnTypes(ClassNode classNode) {
		for (MethodNode methodNode : (List<MethodNode>) classNode.methods) {
			Type returnType = Type.getReturnType(methodNode.desc);

			if (!isRelevantMethod(methodNode, returnType)) {
				continue;
			}

			MethodIdentifier methodIdentifier = MethodIdentifier.create(classNode, methodNode);
			m_methodReturnTypes.put(methodIdentifier, returnType.getClassName());
		}
	}

	/** Check if the method is relevant for the return type analysis. */
	protected boolean isRelevantMethod(MethodNode methodNode, Type returnType) {
		if (BytecodeUtility.isConstructor(methodNode)) {
			return false;
		}
		if (BytecodeUtility.isAbstractMethod(methodNode)) {
			return false;
		}
		if (Identification.isVoid(returnType) || Identification.isPrimitiveOrString(returnType)) {
			return false;
		}

		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected void analyzeTestClass(ClassNode cn, String originalClassPath, ClassType testClassType) {
		// NOP
	}

	/** @see #m_methodReturnTypes */
	public Map<MethodIdentifier, String> getMethodReturnTypes() {
		return m_methodReturnTypes;
	}
}
