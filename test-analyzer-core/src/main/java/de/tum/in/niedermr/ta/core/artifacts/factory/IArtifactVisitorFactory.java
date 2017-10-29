package de.tum.in.niedermr.ta.core.artifacts.factory;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactAnalysisVisitor;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactModificationVisitor;

public interface IArtifactVisitorFactory {

	/**
	 * Create a read-only visitor for the classes of the artifact.
	 */
	IArtifactAnalysisVisitor createAnalyzeVisitor(String artifactContainerInputPath, boolean faultTolerant);

	/**
	 * Create a read-only visitor for the classes of the artifact.
	 */
	IArtifactAnalysisVisitor createAnalyzeVisitor(String artifactContainerInputPath,
			IArtifactExceptionHandler exceptionHandler);

	/**
	 * Create a read-write visitor for the classes of the artifact.
	 */
	IArtifactModificationVisitor createModificationVisitor(String artifactContainerInputPath,
			String artifactContainerOutputPath, boolean faultTolerant);

	/**
	 * Create a read-write visitor for the classes of the artifact.
	 */
	IArtifactModificationVisitor createModificationVisitor(String artifactContainerInputPath,
			String artifactContainerOutputPath, IArtifactExceptionHandler exceptionHandler);

	/** Create an output writer. */
	IArtifactOutputWriter createArtifactOutputWriter(String artifactContainerPath);

	/** Create an appropriate exception handler. */
	IArtifactExceptionHandler createArtifactExceptionHandler(boolean faultTolerant);

	IArtifactIterator createArtifactIterator(String artifactContainerInputPath,
			IArtifactExceptionHandler exceptionHandler);
}
