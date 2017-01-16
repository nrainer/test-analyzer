package de.tum.in.niedermr.ta.runner.analysis.workflow.steps.testworkflow;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Places a file named {@link #m_fileName} in the temp folder of the execution directory and checks every
 * {@value #m_timeIntervalBetweenChecksInMs} ms whether the file still exists. If the file has been deleted,
 * {@link #execAbort()} will be invoked. <br/>
 * <br/>
 * This is a daemon thread by default.
 *
 */
abstract class AbortCheckerThread extends Thread {

	/** Logger. */
	private static final Logger LOGGER = LogManager.getLogger(AbortCheckerThread.class);

	private File m_isRunningFile;
	private String m_fileName;
	private long m_timeIntervalBetweenChecksInMs;

	public AbortCheckerThread(String fileName, long timeIntervalBetweenChecksInSec) {
		m_fileName = fileName;
		m_timeIntervalBetweenChecksInMs = timeIntervalBetweenChecksInSec * 1000;
		this.setDaemon(true);
	}

	/** {@inheritDoc} */
	@Override
	public void run() {
		try {
			setUp();

			while (true) {
				sleepUntilNextCheck();

				if (isToBeAborted()) {
					LOGGER.info("Abort signal received!");
					execAbort();
					LOGGER.info("Abort signal forwarded.");
					return;
				}
			}
		} catch (Throwable t) {
			LOGGER.error(AbortCheckerThread.class.getSimpleName() + " is inactive because of thrown exception!", t);
		}
	}

	private void setUp() throws IOException {
		m_isRunningFile = new File(m_fileName);
		m_isRunningFile.createNewFile();
	}

	private void sleepUntilNextCheck() throws InterruptedException {
		Thread.sleep(m_timeIntervalBetweenChecksInMs);
	}

	private boolean isToBeAborted() {
		return !(m_isRunningFile.exists());
	}

	protected abstract void execAbort();
}