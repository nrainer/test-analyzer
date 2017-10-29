package de.tum.in.niedermr.ta.runner.analysis.instrumentation;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;

import de.tum.in.niedermr.ta.core.artifacts.content.ClassFileData;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.FaultTolerantIteratorExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactModificationVisitor;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactVisitor;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;

public class FaultTolerantInstrumentationIteratorExceptionHandler extends FaultTolerantIteratorExceptionHandler {

	/** Logger. */
	private static final Logger LOGGER = LogManager
			.getLogger(FaultTolerantInstrumentationIteratorExceptionHandler.class);

	/** {@inheritDoc} */
	@Override
	public void onExceptionInHandleClass(Throwable throwable, IArtifactVisitor<?> visitor, ClassReader classInputReader,
			String originalClassPath) {
		LOGGER.warn("Skipping bytecode instrumentation of " + JavaUtility.toClassName(classInputReader.getClassName())
				+ "! " + "Fault tolerant mode permits to continue after " + throwable.getClass().getName()
				+ " with message '" + throwable.getMessage() + "'.");

		if (!(visitor instanceof IArtifactModificationVisitor)) {
			return;
		}

		IArtifactModificationVisitor modificationIterator = (IArtifactModificationVisitor) visitor;

		try {
			modificationIterator.getArtifactOutputWriter()
					.writeClass(new ClassFileData(originalClassPath, classInputReader.b));
		} catch (IOException e) {
			LOGGER.error("Writing class into jar failed: " + originalClassPath);
		}
	}
}
