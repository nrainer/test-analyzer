package de.tum.in.niedermr.ta.core.artifacts.jars;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.DefaultIteratorExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.FaultTolerantIteratorExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactAnalysisIterator;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactModificationIterator;

/** Factory to create instances of iterators. */
public class JarIteratorFactory {

	/**
	 * Create a read-only iterator to iterate over the classes of the artifact.
	 */
	public static IArtifactAnalysisIterator createAnalyzeIterator(String inputJarPath, boolean faultTolerant) {
		return createAnalyzeIterator(inputJarPath, createArtifactExceptionHandler(faultTolerant));
	}

	/**
	 * Create a read-only iterator to iterate over the classes of the artifact.
	 */
	public static IArtifactAnalysisIterator createAnalyzeIterator(String inputJarPath,
			IArtifactExceptionHandler exceptionHandler) {
		return new JarAnalyzeIterator(inputJarPath, exceptionHandler);
	}

	/**
	 * Create a read-write iterator to iterate over the classes of the artifact.
	 */
	public static IArtifactModificationIterator createModificationIterator(String inputJarPath, String outputJarPath,
			boolean faultTolerant) {
		return createModificationIterator(inputJarPath, outputJarPath, createArtifactExceptionHandler(faultTolerant));
	}

	/**
	 * Create a read-write iterator to iterate over the classes of the artifact.
	 */
	public static IArtifactModificationIterator createModificationIterator(String inputJarPath, String outputJarPath,
			IArtifactExceptionHandler exceptionHandler) {
		return new JarModificationIterator(inputJarPath, outputJarPath, exceptionHandler);
	}

	public static IArtifactOutputWriter createArtifactOutputWriter(String jarFile) {
		return new JarFileWriter(jarFile);
	}

	/** Create an appropriate exception handler. */
	public static IArtifactExceptionHandler createArtifactExceptionHandler(boolean faultTolerant) {
		if (faultTolerant) {
			return new FaultTolerantIteratorExceptionHandler();
		}

		return new DefaultIteratorExceptionHandler();
	}
}
