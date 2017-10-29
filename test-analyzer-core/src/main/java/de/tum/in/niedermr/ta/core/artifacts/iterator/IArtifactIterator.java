package de.tum.in.niedermr.ta.core.artifacts.iterator;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactVisitorForIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

public interface IArtifactIterator {

	<T extends ICodeOperation> void iterate(IArtifactVisitorForIterator<T> visitor, T operation)
			throws IteratorException;

	IArtifactExceptionHandler getExceptionHandler();
}
