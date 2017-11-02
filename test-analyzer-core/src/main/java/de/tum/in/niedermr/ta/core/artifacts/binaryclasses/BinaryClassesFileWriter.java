package de.tum.in.niedermr.ta.core.artifacts.binaryclasses;

import java.io.FileOutputStream;
import java.io.IOException;

import de.tum.in.niedermr.ta.core.artifacts.io.AbstractArtifactOutputWriter;

class BinaryClassesFileWriter extends AbstractArtifactOutputWriter {
	/** Constructor. */
	public BinaryClassesFileWriter(String artifactPath) {
		super(artifactPath);
	}

	/** {@inheritDoc} */
	@Override
	public void ensureAllStreamsClosed() throws IOException {
		// NOP
	}

	/** {@inheritDoc} */
	@Override
	protected void writeElement(String entryName, byte[] data) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(getArtifactPath());
		outputStream.write(data);
		outputStream.close();
	}
}
