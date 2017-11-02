package de.tum.in.niedermr.ta.core.artifacts.io;

import java.io.IOException;
import java.util.List;

import de.tum.in.niedermr.ta.core.artifacts.content.ClassFileData;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public abstract class AbstractArtifactOutputWriter implements IArtifactOutputWriter {
	private final String m_artifactPath;

	/** Constructor. */
	public AbstractArtifactOutputWriter(String artifactPath) {
		m_artifactPath = artifactPath;
	}

	public String getArtifactPath() {
		return m_artifactPath;
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

	protected abstract void writeElement(String entryName, byte[] data) throws IOException;
}
