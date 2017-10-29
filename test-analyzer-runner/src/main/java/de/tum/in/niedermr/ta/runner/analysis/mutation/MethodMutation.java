package de.tum.in.niedermr.ta.runner.analysis.mutation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.analysis.filter.MethodFilterList;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.IReturnValueGenerator;
import de.tum.in.niedermr.ta.core.artifacts.content.ClassFileData;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.artifacts.iterator.MainArtifactIteratorFactory;
import de.tum.in.niedermr.ta.core.code.identifier.MethodIdentifier;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

/**
 * (METKIL)
 *
 */
public class MethodMutation {
	/**
	 * Creates a new jar file which contains the class with the mutated method.
	 * 
	 * @return true, if the method was to be mutated and if the mutate operation
	 *         was successful
	 */
	public static boolean createJarWithMutatedMethod(MethodIdentifier methodIdentifier, String outputJarPath,
			IReturnValueGenerator retValGen, IMethodFilter[] additionalMethodFilters) throws Exception {
		String className = methodIdentifier.getOnlyClassName();

		ClassReader cr = new ClassReader(className);
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);

		MethodFilterList methodFilterCollection = MethodFilterList.createWithDefaultFilters();
		methodFilterCollection.addNameFilter(methodIdentifier);
		methodFilterCollection.addValueGenerationSupportedFilter(retValGen);
		methodFilterCollection.addFilters(additionalMethodFilters);

		MutateMethodsOperation operation = new MutateMethodsOperation(retValGen, methodFilterCollection);
		operation.modify(cr, cw);

		boolean mutationExecuted = !operation.getMutatedMethods().isEmpty();

		if (mutationExecuted) {
			IArtifactOutputWriter writer = MainArtifactIteratorFactory.INSTANCE
					.createArtifactOutputWriter(outputJarPath);
			writer.writeClass(new ClassFileData(JavaUtility.toClassPathWithEnding(className), cw.toByteArray()));
			writer.ensureAllStreamsClosed();
		}

		return mutationExecuted;
	}
}
