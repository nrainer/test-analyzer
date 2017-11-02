package de.tum.in.niedermr.ta.core.artifacts.visitor;

import java.io.IOException;
import java.io.InputStream;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

public interface IArtifactVisitorForIterator<OP extends ICodeOperation> extends IArtifactVisitor<OP> {

	void visitBeforeAll() throws IteratorException, IOException;

	void visitAfterAll() throws IteratorException, IOException;

	void visitClassEntry(OP artifactOperation, InputStream inStream, String originalClassPath)
			throws IOException, IteratorException;

	void visitResourceEntry(OP artifactOperation, InputStream inputStream, String entryName)
			throws IteratorException, IOException;
}