package de.tum.in.niedermr.ta.core.artifacts.jars;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.iterator.AbstractArtifactIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;

/** Abstract iterator for jar files. */
public abstract class AbstractJarIterator<OP extends ICodeOperation> extends AbstractArtifactIterator<OP> {

	/** Constructor. */
	public AbstractJarIterator(String inputJarPath, IArtifactExceptionHandler exceptionHandler) {
		super(inputJarPath, exceptionHandler);
	}

	/** {@inheritDoc} */
	@Override
	protected void processArtifactContent(OP artifactOperation) throws IOException, IteratorException {
		JarFileContent classContainer = JarFileContent.fromJarFile(getPathToResource());

		processClassEntryList(artifactOperation, classContainer);
		processResourceEntryList(artifactOperation, classContainer);
	}

	private void processClassEntryList(OP artifactOperation, JarFileContent classContainer)
			throws IteratorException, IOException {
		for (JarEntry entry : classContainer.getClassEntryList()) {
			InputStream inStream = classContainer.getInputStream(entry);
			String originalClassPath = entry.getName();
			processClassEntry(artifactOperation, inStream, originalClassPath);
		}
	}

	private void processResourceEntryList(OP artifactOperation, JarFileContent classContainer)
			throws IteratorException, IOException {
		for (JarEntry entry : classContainer.getResourceEntryList()) {
			InputStream inputStream = classContainer.getInputStream(entry);
			processResourceEntry(artifactOperation, inputStream, entry.getName());
		}
	}
}
