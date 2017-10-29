package de.tum.in.niedermr.ta.core.artifacts.jars;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import de.tum.in.niedermr.ta.core.artifacts.content.ClassFileData;
import de.tum.in.niedermr.ta.core.artifacts.io.IArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

class JarFileWriter implements IArtifactOutputWriter {
	private final String m_jarFile;
	private JarOutputStream m_outputStream;

	public JarFileWriter(String jarFile) {
		this.m_jarFile = jarFile;
	}

	private void open() throws IOException {
		if (m_outputStream == null) {
			this.m_outputStream = new JarOutputStream(new FileOutputStream(m_jarFile));
		}
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws IOException {
		if (m_outputStream != null) {
			m_outputStream.close();
		}
	}

	/** {@inheritDoc} */
	@Override
	public void writeClass(ClassFileData classFileData) throws IOException {
		writeElement(JavaUtility.ensureClassFileEnding(classFileData.getEntryName()), classFileData.getRawData());
	}

	/** {@inheritDoc} */
	@Override
	public void writeResource(ClassFileData resourceFileData) throws IOException {
		writeElement(resourceFileData.getEntryName(), resourceFileData.getRawData());
	}

	/** {@inheritDoc} */
	@Override
	public void writeClasses(List<ClassFileData> classFileList) throws IOException {
		for (ClassFileData classData : classFileList) {
			writeClass(classData);
		}
	}

	private void writeElement(String entryName, byte[] data) throws IOException {
		open();

		JarEntry entry = new JarEntry(entryName);
		m_outputStream.putNextEntry(entry);
		m_outputStream.write(data);
	}
}
