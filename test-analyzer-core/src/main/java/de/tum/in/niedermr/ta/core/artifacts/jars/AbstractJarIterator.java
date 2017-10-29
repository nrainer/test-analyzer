package de.tum.in.niedermr.ta.core.artifacts.jars;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;

import org.objectweb.asm.ClassReader;

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

	private void processClassEntryList(OP jarOperation, JarFileContent classContainer)
			throws IteratorException, IOException {
		for (JarEntry entry : classContainer.getClassEntryList()) {
			InputStream inStream = classContainer.getInputStream(entry);
			ClassReader classInputReader = new ClassReader(inStream);
			String originalClassPath = entry.getName();

			try {
				handleEntry(jarOperation, classInputReader, originalClassPath);
			} catch (Throwable t) {
				getExceptionHandler().onExceptionInHandleClass(t, this, classInputReader, originalClassPath);
			} finally {
				inStream.close();
			}
		}
	}

	private void processResourceEntryList(OP jarOperation, JarFileContent classContainer)
			throws IteratorException, IOException {
		for (JarEntry entry : classContainer.getResourceEntryList()) {
			InputStream inputStream = classContainer.getInputStream(entry);

			try {
				handleResource(jarOperation, entry, inputStream);
			} catch (Throwable t) {
				getExceptionHandler().onExceptionInHandleResource(t, this, inputStream, entry.getName());
			} finally {
				inputStream.close();
			}
		}
	}
}
