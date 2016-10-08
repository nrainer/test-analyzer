package de.tum.in.niedermr.ta.core.analysis.result.receiver;

/** Factory to create instances of {@link IResultReceiver}. */
public class ResultReceiverFactory {

	/**
	 * Create an instance of the appropriate file result receiver.
	 * 
	 * @return either {@link FileResultReceiver} (with overwrite = true) or
	 *         {@link MultiFileResultReceiver}
	 */
	public static IResultReceiver createFileResultReceiverWithDefaultSettings(boolean useMultiFile, String fileName) {
		if (useMultiFile) {
			return new MultiFileResultReceiver(fileName);
		}

		return new FileResultReceiver(fileName, true);
	}

	/** Create an instance of the {@link InMemoryResultReceiver}. */
	public static InMemoryResultReceiver createInMemoryResultReceiver() {
		return new InMemoryResultReceiver();
	}
}
