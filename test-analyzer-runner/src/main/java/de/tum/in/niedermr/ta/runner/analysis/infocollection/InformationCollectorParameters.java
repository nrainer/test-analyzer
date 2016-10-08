package de.tum.in.niedermr.ta.runner.analysis.infocollection;

import de.tum.in.niedermr.ta.core.execution.id.IFullExecutionId;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsKey;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsReader;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;

/** Parameters for InformationCollectors. */
public class InformationCollectorParameters {

	/** Number of args. */
	private static final int ARGS_COUNT = 9;
	public static final ProgramArgsKey ARGS_EXECUTION_ID = new ProgramArgsKey(InformationCollectorParameters.class, 0);
	public static final ProgramArgsKey ARGS_FILE_WITH_TESTS_TO_RUN = new ProgramArgsKey(
			InformationCollectorParameters.class, 1);
	public static final ProgramArgsKey ARGS_FILE_WITH_RESULTS = new ProgramArgsKey(InformationCollectorParameters.class,
			2);
	public static final ProgramArgsKey ARGS_TEST_RUNNER_CLASS = new ProgramArgsKey(InformationCollectorParameters.class,
			3);
	public static final ProgramArgsKey ARGS_OPERATE_FAULT_TOLERANT = new ProgramArgsKey(
			InformationCollectorParameters.class, 4);
	public static final ProgramArgsKey ARGS_TEST_CLASS_INCLUDES = new ProgramArgsKey(
			InformationCollectorParameters.class, 5);
	public static final ProgramArgsKey ARGS_TEST_CLASS_EXCLUDES = new ProgramArgsKey(
			InformationCollectorParameters.class, 6);
	public static final ProgramArgsKey ARGS_RESULT_PRESENTATION = new ProgramArgsKey(
			InformationCollectorParameters.class, 7);
	public static final ProgramArgsKey ARGS_USE_MULTI_FILE_OUTPUT = new ProgramArgsKey(
			InformationCollectorParameters.class, 8);

	/** Create a writer for the program arguments. */
	public static ProgramArgsWriter createProgramArgsWriter() {
		return new ProgramArgsWriter(InformationCollectorParameters.class, ARGS_COUNT);
	}

	/** Create a reader for the program arguments. */
	public static ProgramArgsReader createProgramArgsReader(String[] args) {
		return new ProgramArgsReader(InformationCollectorParameters.class, args);
	}

	/** Retrieve the execution id from the arguments. */
	public static IFullExecutionId getExecutionId(String[] args) {
		return ExecutionIdFactory.parseFullExecutionId(createProgramArgsReader(args).getArgument(ARGS_EXECUTION_ID));
	}
}
