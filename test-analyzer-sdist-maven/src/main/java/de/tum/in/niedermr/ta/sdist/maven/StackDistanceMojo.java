package de.tum.in.niedermr.ta.sdist.maven;

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

@Mojo(name = "sdist", defaultPhase = LifecyclePhase.PROCESS_TEST_CLASSES, requiresDependencyResolution = ResolutionScope.TEST)
public class StackDistanceMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	/** {@inheritDoc} */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		String builtSourceCodeDirectory = project.getBuild().getOutputDirectory();
		String inputArtifactPath = builtSourceCodeDirectory;
		String outputArtifactPath = builtSourceCodeDirectory;

		try {
			getLog().info("Starting to instrument non-test classes for stack distance computation");
			instrumentSourceCodeInNewProcess(inputArtifactPath, outputArtifactPath);
			getLog().info("Completed instrumenting non-test classes for stack distance computation");
		} catch (DependencyResolutionRequiredException e) {
			throw new MojoExecutionException("IteratorException", e);
		}
	}

	@SuppressWarnings("unchecked")
	protected void instrumentSourceCodeInNewProcess(String inputArtifactPath, String outputArtifactPath)
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
}
