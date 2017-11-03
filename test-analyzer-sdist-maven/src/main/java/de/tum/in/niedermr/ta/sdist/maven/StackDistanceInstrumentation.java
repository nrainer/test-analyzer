package de.tum.in.niedermr.ta.sdist.maven;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.factory.MainArtifactVisitorFactory;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactModificationVisitor;
import de.tum.in.niedermr.ta.core.code.tests.detector.BiasedTestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation.AnalysisInstrumentationOperation;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.recording.v3.StackLogRecorderV3;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsKey;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsReader;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;

public class StackDistanceInstrumentation {

	private static final int ARGS_COUNT = 2;
	public static final ProgramArgsKey ARGS_ARTIFACT_INPUT_PATH = new ProgramArgsKey(StackDistanceInstrumentation.class,
			0);
	public static final ProgramArgsKey ARGS_ARTIFACT_OUTPUT_PATH = new ProgramArgsKey(
			StackDistanceInstrumentation.class, 1);

	public static void main(String[] args) throws IteratorException {
		ProgramArgsReader argsReader = new ProgramArgsReader(StackDistanceInstrumentation.class, args);

		IArtifactExceptionHandler exceptionHandler = MainArtifactVisitorFactory.INSTANCE
				.createArtifactExceptionHandler(false);

		String inputArtifactPath = argsReader.getArgument(ARGS_ARTIFACT_INPUT_PATH);
		String outputArtifactPath = argsReader.getArgument(ARGS_ARTIFACT_OUTPUT_PATH);

		IArtifactModificationVisitor modificationIterator = MainArtifactVisitorFactory.INSTANCE
				.createModificationVisitor(inputArtifactPath, outputArtifactPath, exceptionHandler);

		// the source code folder contains only source classes in the Maven scenario
		ITestClassDetector testClassDetector = new BiasedTestClassDetector(ClassType.NO_TEST_CLASS);

		AnalysisInstrumentationOperation operation = new AnalysisInstrumentationOperation(testClassDetector,
				StackLogRecorderV3.class);
		modificationIterator.execute(operation);
	}

	/** Create a writer for the program arguments. */
	public static ProgramArgsWriter createProgramArgsWriter() {
		return new ProgramArgsWriter(StackDistanceInstrumentation.class, ARGS_COUNT);
	}
}
