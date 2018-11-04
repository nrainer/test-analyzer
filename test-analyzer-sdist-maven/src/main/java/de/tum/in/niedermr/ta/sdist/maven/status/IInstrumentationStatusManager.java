package de.tum.in.niedermr.ta.sdist.maven.status;

import java.io.IOException;

public interface IInstrumentationStatusManager {

	boolean checkIsInstrumented(String codeDirectory);

	void markAsInstrumented(String codeDirectory) throws IOException;
}
