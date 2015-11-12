package de.tum.in.niedermr.ta.core.analysis.jars.writer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import de.tum.in.niedermr.ta.core.analysis.jars.content.JarFileElementRawData;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class JarFileWriter {
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

	public void close() throws IOException {
		if (m_outputStream != null) {
			m_outputStream.close();
		}
	}

	public void writeClassIntoJar(JarFileElementRawData classFileData) throws IOException {
		writeIntoJar(JavaUtility.ensureClassFileEnding(classFileData.getEntryName()), classFileData.getRawData());
	}

	public void writeResourceIntoJar(JarFileElementRawData resourceFileData) throws IOException {
		writeIntoJar(resourceFileData.getEntryName(), resourceFileData.getRawData());
	}

	public void writeClassesIntoJar(List<JarFileElementRawData> classFileList) throws IOException {
		for (JarFileElementRawData classData : classFileList) {
			writeClassIntoJar(classData);
		}
	}

	private void writeIntoJar(String entryName, byte[] data) throws IOException {
		open();

		JarEntry entry = new JarEntry(entryName);
		m_outputStream.putNextEntry(entry);
		m_outputStream.write(data);
	}
}
