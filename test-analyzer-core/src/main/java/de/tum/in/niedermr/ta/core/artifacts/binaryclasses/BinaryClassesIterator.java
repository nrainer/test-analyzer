package de.tum.in.niedermr.ta.core.artifacts.binaryclasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.conqat.lib.commons.filesystem.FileSystemUtils;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.iterator.AbstractArtifactIterator;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactVisitorForIterator;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;
import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;

/** Abstract iterator for binary classes. */
class BinaryClassesIterator extends AbstractArtifactIterator {
	/** Constructor. */
	public BinaryClassesIterator(String artifactPath, IArtifactExceptionHandler exceptionHandler) {
		super(artifactPath, exceptionHandler);
	}

	/** {@inheritDoc} */
	@Override
	protected <OP extends ICodeOperation> void processArtifactContent(IArtifactVisitorForIterator<OP> visitor,
			OP artifactOperation) throws IOException, IteratorException {
		Path artifactPath = Paths.get(getPathToResource());

		List<File> files = FileSystemUtils.listFilesRecursively(artifactPath.toFile());

		for (File currentFile : files) {
			if (currentFile.isDirectory()) {
				continue;
			}

			Path currentFilePath = Paths.get(currentFile.getPath());
			handleEntryInArtifact(visitor, artifactOperation, artifactPath, currentFilePath);
		}
	}

	private <OP extends ICodeOperation> void handleEntryInArtifact(IArtifactVisitorForIterator<OP> visitor,
			OP artifactOperation, Path artifactPath, Path originalEntryPath) throws IOException, IteratorException {
		if (Files.isDirectory(originalEntryPath)) {
			return;
		}

		FileInputStream inputStream = new FileInputStream(originalEntryPath.toFile());
		String relativeEntryPath = artifactPath.relativize(originalEntryPath).toString();

		if (relativeEntryPath.endsWith(FileSystemConstants.FILE_EXTENSION_CLASS)) {
			visitor.visitClassEntry(artifactOperation, inputStream, relativeEntryPath);
		} else {
			visitor.visitResourceEntry(artifactOperation, inputStream, relativeEntryPath);
		}
	}
}
