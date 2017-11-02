package de.tum.in.niedermr.ta.core.artifacts.binaryclasses;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.conqat.lib.commons.filesystem.FileSystemUtils;

import de.tum.in.niedermr.ta.core.artifacts.io.AbstractArtifactOutputWriter;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.util.FileUtility;

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
		String elementPath = FileUtility.ensurePathEndsWithPathSeparator(getArtifactPath(),
				FileSystemConstants.PATH_SEPARATOR) + entryName;
		FileSystemUtils.ensureParentDirectoryExists(new File(elementPath));

		FileOutputStream outputStream = new FileOutputStream(elementPath);
		outputStream.write(data);
		outputStream.close();
	}
}
