package de.tum.in.niedermr.ta.core.artifacts.jars;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import de.tum.in.niedermr.ta.core.artifacts.io.AbstractArtifactOutputWriter;

class JarFileWriter extends AbstractArtifactOutputWriter {
	private JarOutputStream m_outputStream;

	/** Constructor. */
	public JarFileWriter(String jarFile) {
		super(jarFile);
	}

	/** {@inheritDoc} */
	@Override
	public void ensureAllStreamsClosed() throws IOException {
		if (m_outputStream != null) {
			m_outputStream.close();
		}
	}

	/** {@inheritDoc} */
	@Override
	protected void writeElement(String entryName, byte[] data) throws IOException {
		if (m_outputStream == null) {
			m_outputStream = new JarOutputStream(new FileOutputStream(getArtifactPath()));
		}

		JarEntry entry = new JarEntry(entryName);
		m_outputStream.putNextEntry(entry);
		m_outputStream.write(data);
	}
}
