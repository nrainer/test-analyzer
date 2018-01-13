package de.tum.in.niedermr.ta.sdist.maven;

import java.util.ArrayList;
import java.util.List;

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

	/** Additional directories with compiled source source to be instrumented. */
	@Parameter(property = "additionalApplicationClasspathElements")
	private ArrayList<String> additionalApplicationClasspathElements;

	/** {@inheritDoc} */
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		List<String> compiledCodeDirectoriesToInstrument = new ArrayList<>();
		compiledCodeDirectoriesToInstrument.add(project.getBuild().getOutputDirectory());

		if (additionalApplicationClasspathElements != null) {
			compiledCodeDirectoriesToInstrument.addAll(compiledCodeDirectoriesToInstrument);
		}

		getLog().info("Starting to instrument non-test classes for stack distance computation");

		try {
			for (String codeDirectory : compiledCodeDirectoriesToInstrument) {
				getLog().info("Instrumenting: " + codeDirectory);
				String inputArtifactPath = codeDirectory;
				String outputArtifactPath = codeDirectory;
				instrumentSourceCodeInNewProcess(inputArtifactPath, outputArtifactPath);
			}
		} catch (DependencyResolutionRequiredException e) {
			throw new MojoExecutionException("IteratorException", e);
		}

		getLog().info("Completed instrumenting non-test classes for stack distance computation");
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
