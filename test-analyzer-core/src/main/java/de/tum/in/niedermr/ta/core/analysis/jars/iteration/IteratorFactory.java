package de.tum.in.niedermr.ta.core.analysis.jars.iteration;

/** Factory to create instances of iterators. */
public class IteratorFactory {

	/** Create a read-only iterator to iterate over the classes of a jar file. */
	public static JarAnalyzeIterator createJarAnalyzeIterator(String inputJarPath, boolean faultTolerant) {
		if (faultTolerant) {
			return new FaultTolerantJarAnalyzeIterator(inputJarPath);
		}
		return new JarAnalyzeIterator(inputJarPath);
	}

	/** Create a read-write iterator to iterate over the classes of a jar file. */
	public static JarModificationIterator createJarModificationIterator(String inputJarPath, String outputJarPath,
			boolean faultTolerant) {
		if (faultTolerant) {
			return new FaultTolerantJarModificationIterator(inputJarPath, outputJarPath);
		}
		return new JarModificationIterator(inputJarPath, outputJarPath);
	}
}
