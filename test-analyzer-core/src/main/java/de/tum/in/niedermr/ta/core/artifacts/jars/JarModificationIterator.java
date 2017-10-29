package de.tum.in.niedermr.ta.core.artifacts.jars;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import de.tum.in.niedermr.ta.core.artifacts.content.ClassFileData;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactModificationIterator;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;

/** Iterator for modifications in Jar files. */
class JarModificationIterator extends AbstractJarIterator<ICodeModificationOperation>
		implements IArtifactModificationIterator {
	private final IArtifactOutputWriter m_jarFileWriter;

	/** Constructor. */
	protected JarModificationIterator(String inputJarPath, String outputJarPath,
			IArtifactExceptionHandler exceptionHandler) {
		super(inputJarPath, exceptionHandler);
		this.m_jarFileWriter = new JarFileWriter(outputJarPath);
	}

	/** {@inheritDoc} */
	@Override
	public IArtifactOutputWriter getArtifactOutputWriter() {
		return m_jarFileWriter;
	}

	/** {@inheritDoc} */
	@Override
	protected void handleEntry(ICodeModificationOperation jarOperation, ClassReader cr, String originalClassPath)
			throws IteratorException, CodeOperationException, IOException {
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);

		jarOperation.modify(cr, cw);

		byte[] transformedClass = cw.toByteArray();
		m_jarFileWriter.writeClass(new ClassFileData(originalClassPath, transformedClass));
	}

	/** {@inheritDoc} */
	@Override
	protected void handleResource(ICodeModificationOperation jarOperation, InputStream inStream, String entryName)
			throws IteratorException, CodeOperationException, IOException {
		m_jarFileWriter.writeResource(new ClassFileData(entryName, IOUtils.toByteArray(inStream)));
	}

	/** {@inheritDoc} */
	@Override
	protected void afterAll() throws IteratorException, IOException {
		if (getFurtherClassesToBeAdded() != null) {
			m_jarFileWriter.writeClasses(getFurtherClassesToBeAdded());
		}

		m_jarFileWriter.close();
	}

	protected List<ClassFileData> getFurtherClassesToBeAdded() {
		return new ArrayList<>();
	}
}
