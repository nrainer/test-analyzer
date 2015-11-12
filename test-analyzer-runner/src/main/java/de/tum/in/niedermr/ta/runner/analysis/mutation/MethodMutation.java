package de.tum.in.niedermr.ta.runner.analysis.mutation;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import de.tum.in.niedermr.ta.core.analysis.filter.IMethodFilter;
import de.tum.in.niedermr.ta.core.analysis.filter.MethodFilterCollection;
import de.tum.in.niedermr.ta.core.analysis.jars.content.JarFileElementRawData;
import de.tum.in.niedermr.ta.core.analysis.jars.writer.JarFileWriter;
import de.tum.in.niedermr.ta.core.analysis.mutation.returnvalues.IReturnValueGenerator;
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
	 * @return true, if the method was to be mutated and if the mutate operation was successful
	 */
	public static boolean createJarWithMutatedMethod(MethodIdentifier methodIdentifier, String outputJarPath, IReturnValueGenerator retValGen,
			IMethodFilter[] additionalMethodFilters) throws Exception {
		String className = methodIdentifier.getOnlyClassName();

		ClassReader cr = new ClassReader(className);
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);

		MethodFilterCollection methodFilterCollection = MethodFilterCollection.createCollectionWithDefaultFilters();
		methodFilterCollection.addNameFilter(methodIdentifier);
		methodFilterCollection.addValueGenerationSupportedFilter(retValGen);
		methodFilterCollection.addFilterCollection(additionalMethodFilters);

		MutateMethodsOperation operation = new MutateMethodsOperation(retValGen, methodFilterCollection);
		operation.modify(cr, cw);

		boolean mutationExecuted = !operation.getMutatedMethods().isEmpty();

		if (mutationExecuted) {
			JarFileWriter writer = new JarFileWriter(outputJarPath);
			writer.writeClassIntoJar(new JarFileElementRawData(JavaUtility.toClassPathWithEnding(className), cw.toByteArray()));
			writer.close();
		}

		return mutationExecuted;
	}
}
