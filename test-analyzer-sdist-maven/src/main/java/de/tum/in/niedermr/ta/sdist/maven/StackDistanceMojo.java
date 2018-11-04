package de.tum.in.niedermr.ta.sdist.maven;

import java.io.File;
import java.io.IOException;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import de.tum.in.niedermr.ta.core.common.constants.FileSystemConstants;
import de.tum.in.niedermr.ta.core.common.util.ClasspathUtility;
import de.tum.in.niedermr.ta.core.common.util.StringUtility;
import de.tum.in.niedermr.ta.runner.execution.ProcessExecution;
import de.tum.in.niedermr.ta.runner.execution.args.ProgramArgsWriter;
import de.tum.in.niedermr.ta.runner.execution.id.ExecutionIdFactory;
import de.tum.in.niedermr.ta.sdist.maven.status.AdvancedInstrumentationStatusManager;
import de.tum.in.niedermr.ta.sdist.maven.status.IInstrumentationStatusManager;
import de.tum.in.niedermr.ta.sdist.maven.status.NoneInstrumentedStatusManager;
import de.tum.in.niedermr.ta.sdist.maven.status.SimpleInstrumentationStatusManager;

/**
 * StackDistance Mojo to instrument the classes under test. <br/>
 * Use <code>PROCESS_CLASSES</code> as lifecycle phase because no test code is instrumented and so that (sub-) projects
 * without test code are also instrumented.
 */
@Mojo(name = "sdist", defaultPhase = LifecyclePhase.PROCESS_CLASSES, requiresDependencyResolution = ResolutionScope.COMPILE)
public class StackDistanceMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	/**
	 * Check if the instrumentation has not been done yet to avoid a further instrumentation (which will cause an
	 * error). Modes are: none, simple, advanced
	 */
	@Parameter(property = "precheckAlreadyInstrumentedMode", defaultValue = "advanced")
	private String precheckAlreadyInstrumentedMode = "advanced";

	/** {@inheritDoc} */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		if (!isShouldRun()) {
			getLog().info("Skipping current project without build folder: " + getProjectDisplayName());
			return;
		}

		getLog().info("Processing project: " + getProjectDisplayName());

		getLog().info("Starting to instrument non-test classes for stack distance computation");
		try {
			instrumentCodeDirectory(project.getBuild().getOutputDirectory());
		} catch (DependencyResolutionRequiredException | IOException e) {
			throw new MojoExecutionException("Exception occurred", e);
		}
		getLog().info("Completed instrumenting non-test classes for stack distance computation");
	}

	/** Check if the project has build artifacts to be instrumented. */
	protected boolean isShouldRun() {
		// project has build artifacts and compiled application classes exist
		return new File(project.getBuild().getDirectory()).exists()
				&& new File(project.getBuild().getOutputDirectory()).exists();
	}

	private void instrumentCodeDirectory(String codeDirectory)
			throws DependencyResolutionRequiredException, IOException {
		IInstrumentationStatusManager instrumentationStatus = createInstrumentationStatusManager();

		getLog().info("Checking instrumentation status using: " + instrumentationStatus.getClass().getSimpleName());

		if (instrumentationStatus.checkIsInstrumented(codeDirectory)) {
			getLog().warn("Skipping instrumentation for " + codeDirectory + ", seems to be already instrumented.");
			return;
		}

		getLog().info("Instrumenting: " + codeDirectory);
		String inputArtifactPath = codeDirectory;
		String outputArtifactPath = codeDirectory;
		instrumentSourceCodeInNewProcess(inputArtifactPath, outputArtifactPath);
		instrumentationStatus.markAsInstrumented(codeDirectory);
	}

	protected IInstrumentationStatusManager createInstrumentationStatusManager() {
		if (precheckAlreadyInstrumentedMode.equalsIgnoreCase("advanced")) {
			return new AdvancedInstrumentationStatusManager(getLog());
		}

		if (precheckAlreadyInstrumentedMode.equalsIgnoreCase("simple")) {
			return new SimpleInstrumentationStatusManager(getLog());
		}

		return new NoneInstrumentedStatusManager();
	}

	@SuppressWarnings("unchecked")
	private void instrumentSourceCodeInNewProcess(String inputArtifactPath, String outputArtifactPath)
			throws DependencyResolutionRequiredException {
		String executionPath = project.getBasedir().getAbsolutePath();
		String classpath = ClasspathUtility.getCurrentClasspath()
				+ StringUtility.join(project.getTestClasspathElements(), FileSystemConstants.CP_SEP);

		ProgramArgsWriter argsWriter = StackDistanceInstrumentation.createProgramArgsWriter();
		argsWriter.setValue(StackDistanceInstrumentation.ARGS_ARTIFACT_INPUT_PATH, inputArtifactPath);
		argsWriter.setValue(StackDistanceInstrumentation.ARGS_ARTIFACT_OUTPUT_PATH, outputArtifactPath);

		ProcessExecution processExecution = new ProcessExecution(executionPath, executionPath, executionPath);
		processExecution.execute(ExecutionIdFactory.createNewShortExecutionId(), ProcessExecution.NO_TIMEOUT,
				StackDistanceInstrumentation.class, classpath, argsWriter);
	}

	private String getProjectDisplayName() {
		return project.getArtifactId() + " (" + project.getName() + ")";
	}
}
