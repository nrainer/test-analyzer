package de.tum.in.niedermr.ta.core.analysis.result.receiver;

/** Factory to create instances of {@link IResultReceiver}. */
public class ResultReceiverFactory {

	/**
	 * Create an instance of the appropriate file result receiver.
	 * 
	 * @return either {@link FileResultReceiver} (with overwrite = true) or {@link MultiFileResultReceiver}
	 */
	public static IResultReceiver createFileResultReceiverWithDefaultSettings(boolean useMultiFile, String fileName) {
		return createFileResultReceiverWithDefaultSettings(useMultiFile, fileName, false);
	}

	public static IResultReceiver createFileResultReceiverWithDefaultSettings(boolean useMultiFile, String fileName,
			boolean flushOnPartiallyComplete) {
		if (useMultiFile) {
			return new MultiFileResultReceiver(fileName, flushOnPartiallyComplete);
		}

		return new FileResultReceiver(fileName, true, flushOnPartiallyComplete);
	}

	/** Create an instance of the {@link InMemoryResultReceiver}. */
	public static InMemoryResultReceiver createInMemoryResultReceiver() {
		return new InMemoryResultReceiver();
	}
}
