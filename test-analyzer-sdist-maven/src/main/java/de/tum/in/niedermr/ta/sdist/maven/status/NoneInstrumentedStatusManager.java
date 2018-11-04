package de.tum.in.niedermr.ta.sdist.maven.status;

import java.io.IOException;

public class NoneInstrumentedStatusManager implements IInstrumentationStatusManager {

	/** {@inheritDoc} */
	@Override
	public boolean checkIsInstrumented(String codeDirectory) throws IOException {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void markAsInstrumented(String codeDirectory) throws IOException {
		// NOP
	}
}
