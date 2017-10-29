package de.tum.in.niedermr.ta.runner.analysis.instrumentation;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.artifacts.content.ClassFileData;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.FaultTolerantIteratorExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactModificationIterator;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class FaultTolerantInstrumentationIteratorExceptionHandler extends FaultTolerantIteratorExceptionHandler {

	/** Logger. */
	private static final Logger LOGGER = LogManager
			.getLogger(FaultTolerantInstrumentationIteratorExceptionHandler.class);

	@Override
	public void onExceptionInHandleClass(Throwable throwable, IArtifactIterator<?> iterator,
			ClassReader classInputReader, String originalClassPath) {
		LOGGER.warn("Skipping bytecode instrumentation of " + JavaUtility.toClassName(classInputReader.getClassName())
				+ "! " + "Fault tolerant mode permits to continue after " + throwable.getClass().getName()
				+ " with message '" + throwable.getMessage() + "'.");

		if (!(iterator instanceof IArtifactModificationIterator)) {
			return;
		}

		IArtifactModificationIterator modificationIterator = (IArtifactModificationIterator) iterator;

		try {
			modificationIterator.getArtifactOutputWriter()
					.writeClass(new ClassFileData(originalClassPath, classInputReader.b));
		} catch (IOException e) {
			LOGGER.error("Writing class into jar failed: " + originalClassPath);
		}
	}
}
