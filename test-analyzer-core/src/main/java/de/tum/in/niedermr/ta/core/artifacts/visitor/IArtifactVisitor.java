package de.tum.in.niedermr.ta.core.artifacts.visitor;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

/** Visitor for the elements in an artifact. */
public interface IArtifactVisitor<OP extends ICodeOperation> {

	/**
	 * Execute an operation on all elements in the artifact.
	 *
	 * @param operation
	 *            to be executed for all elements
	 */
	void execute(OP operation) throws IteratorException;
}
