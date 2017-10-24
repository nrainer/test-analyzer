package de.tum.in.niedermr.ta.core.artifacts.iterator;

import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;

public interface IArtifactModificationIterator extends IArtifactIterator<ICodeModificationOperation> {

	IArtifactOutputWriter getArtifactOutputWriter();
}
