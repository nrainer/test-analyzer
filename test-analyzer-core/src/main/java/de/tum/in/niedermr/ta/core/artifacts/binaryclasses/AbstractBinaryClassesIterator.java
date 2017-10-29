package de.tum.in.niedermr.ta.core.artifacts.binaryclasses;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.iterator.AbstractArtifactIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

/** Abstract iterator for binary classes. */
public abstract class AbstractBinaryClassesIterator<OP extends ICodeOperation> extends AbstractArtifactIterator<OP> {
	/** Constructor. */
	public AbstractBinaryClassesIterator(String artifactPath, IArtifactExceptionHandler exceptionHandler) {
		super(artifactPath, exceptionHandler);
	}

	/** {@inheritDoc} */
	@Override
	protected void processArtifactContent(OP artifactOperation) throws IOException, IteratorException {
		Path artifactPath = Paths.get(getPathToResource());

		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(artifactPath)) {

			for (Path currentEntryPath : directoryStream) {
				handleEntryInArtifact(artifactOperation, artifactPath, currentEntryPath);
			}
		}
	}

	private void handleEntryInArtifact(OP artifactOperation, Path artifactPath, Path currentEntryPath)
			throws IOException, IteratorException {
		if (Files.isDirectory(currentEntryPath)) {
			return;
		}

		FileInputStream inputStream = new FileInputStream(currentEntryPath.toFile());
		String entryPath = currentEntryPath.relativize(artifactPath).toString();

		if (currentEntryPath.endsWith(FileSystemConstants.FILE_EXTENSION_CLASS)) {
			processClassEntry(artifactOperation, inputStream, entryPath);
		} else {
			processResourceEntry(artifactOperation, inputStream, entryPath);
		}
	}
}
