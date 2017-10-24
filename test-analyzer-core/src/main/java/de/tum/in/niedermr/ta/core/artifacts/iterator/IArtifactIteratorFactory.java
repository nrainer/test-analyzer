package de.tum.in.niedermr.ta.core.artifacts.iterator;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;

public interface IArtifactIteratorFactory {

	/**
	 * Create a read-only iterator to iterate over the classes of the artifact.
	 */
	public IArtifactAnalysisIterator createAnalyzeIterator(String artifactContainerInputPath, boolean faultTolerant);

	/**
	 * Create a read-only iterator to iterate over the classes of the artifact.
	 */
	public IArtifactAnalysisIterator createAnalyzeIterator(String artifactContainerInputPath,
			IArtifactExceptionHandler exceptionHandler);

	/**
	 * Create a read-write iterator to iterate over the classes of the artifact.
	 */
	public IArtifactModificationIterator createModificationIterator(String artifactContainerInputPath,
			String artifactContainerOutputPath, boolean faultTolerant);

	/**
	 * Create a read-write iterator to iterate over the classes of the artifact.
	 */
	public IArtifactModificationIterator createModificationIterator(String artifactContainerInputPath,
			String artifactContainerOutputPath, IArtifactExceptionHandler exceptionHandler);

	/** Create an output writer. */
	public IArtifactOutputWriter createArtifactOutputWriter(String artifactContainerPath);

	/** Create an appropriate exception handler. */
	public IArtifactExceptionHandler createArtifactExceptionHandler(boolean faultTolerant);
}
