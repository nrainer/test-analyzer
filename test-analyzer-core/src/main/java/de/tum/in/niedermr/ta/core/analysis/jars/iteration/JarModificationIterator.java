package de.tum.in.niedermr.ta.core.analysis.jars.iteration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;

import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import de.tum.in.niedermr.ta.core.analysis.jars.content.JarFileElementRawData;
import de.tum.in.niedermr.ta.core.analysis.jars.writer.JarFileWriter;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;

/** Iterator for modifications in Jar files. */
public class JarModificationIterator extends AbstractJarIterator<ICodeModificationOperation> {
	private final JarFileWriter m_jarFileWriter;

	protected JarModificationIterator(String inputJarPath, String outputJarPath) {
		super(inputJarPath);
		this.m_jarFileWriter = new JarFileWriter(outputJarPath);
	}

	protected JarFileWriter getJarFileWriter() {
		return m_jarFileWriter;
	}

	/** {@inheritDoc} */
	@Override
	protected void beforeAll() throws Exception {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	protected void handleEntry(ICodeModificationOperation jarOperation, ClassReader cr, String originalClassPath)
			throws Exception {
		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);

		jarOperation.modify(cr, cw);

		byte[] transformedClass = cw.toByteArray();
		m_jarFileWriter.writeClassIntoJar(new JarFileElementRawData(originalClassPath, transformedClass));
	}

	/** {@inheritDoc} */
	@Override
	protected void handleResource(ICodeModificationOperation jarOperation, JarEntry resourceEntry, InputStream inStream)
			throws Exception {
		m_jarFileWriter.writeResourceIntoJar(
				new JarFileElementRawData(resourceEntry.getName(), IOUtils.toByteArray(inStream)));
	}

	/** {@inheritDoc} */
	@Override
	protected void afterAll() throws IOException {
		if (getFurtherClassesToBeAdded() != null) {
			m_jarFileWriter.writeClassesIntoJar(getFurtherClassesToBeAdded());
		}

		m_jarFileWriter.close();
	}

	protected List<JarFileElementRawData> getFurtherClassesToBeAdded() {
		return new ArrayList<>();
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInHandleEntry(Throwable throwable, String className) throws Exception {
		throw new Exception(throwable);
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInHandleResource(Throwable throwable, String resourcePath) throws Exception {
		throw new Exception(throwable);
	}

	/** {@inheritDoc} */
	@Override
	protected void onExceptionInJarProcessing(Throwable throwable, ICodeModificationOperation jarOperation)
			throws Exception {
		throw new Exception(throwable);

	}
}
