package de.tum.in.niedermr.ta.core.artifacts.iterator;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

/** Iterator over elements in an artifact. */
public interface IArtifactIterator<OP extends ICodeOperation> {

	/**
	 * Execute an operation on all elements in the artifact.
	 * 
	 * @param operation
	 *            to be executed for all elements
	 */
	void execute(OP operation) throws IteratorException;
}
