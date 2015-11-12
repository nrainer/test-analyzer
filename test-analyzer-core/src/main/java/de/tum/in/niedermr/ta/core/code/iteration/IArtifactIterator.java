package de.tum.in.niedermr.ta.core.code.iteration;

import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

public interface IArtifactIterator<OP extends ICodeOperation> {

	void execute(OP jarOperation) throws Exception;
}
