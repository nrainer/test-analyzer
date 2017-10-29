package de.tum.in.niedermr.ta.core.artifacts.visitor;

import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;

public interface IArtifactModificationVisitor extends IArtifactVisitor<ICodeModificationOperation> {

	IArtifactOutputWriter getArtifactOutputWriter();
}
