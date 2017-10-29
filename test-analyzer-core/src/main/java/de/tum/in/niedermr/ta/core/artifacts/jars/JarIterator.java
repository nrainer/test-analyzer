package de.tum.in.niedermr.ta.core.artifacts.jars;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.iterator.AbstractArtifactIterator;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactVisitorForIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

/** Iterator for jar files. */
class JarIterator extends AbstractArtifactIterator {

	/** Constructor. */
	public JarIterator(String inputJarPath, IArtifactExceptionHandler exceptionHandler) {
		super(inputJarPath, exceptionHandler);
	}

	/** {@inheritDoc} */
	@Override
	protected <OP extends ICodeOperation> void processArtifactContent(IArtifactVisitorForIterator<OP> visitor,
			OP artifactOperation) throws IOException, IteratorException {
		JarFileContent classContainer = JarFileContent.fromJarFile(getPathToResource());

		processClassEntryList(visitor, artifactOperation, classContainer);
		processResourceEntryList(visitor, artifactOperation, classContainer);
	}

	private <OP extends ICodeOperation> void processClassEntryList(IArtifactVisitorForIterator<OP> visitor,
			OP artifactOperation, JarFileContent classContainer) throws IteratorException, IOException {
		for (JarEntry entry : classContainer.getClassEntryList()) {
			InputStream inStream = classContainer.getInputStream(entry);
			String originalClassPath = entry.getName();
			visitor.visitClassEntry(artifactOperation, inStream, originalClassPath);
		}
	}

	private <OP extends ICodeOperation> void processResourceEntryList(IArtifactVisitorForIterator<OP> visitor,
			OP artifactOperation, JarFileContent classContainer) throws IteratorException, IOException {
		for (JarEntry entry : classContainer.getResourceEntryList()) {
			InputStream inputStream = classContainer.getInputStream(entry);
			visitor.visitResourceEntry(artifactOperation, inputStream, entry.getName());
		}
	}
}
