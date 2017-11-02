package de.tum.in.niedermr.ta.sdist.maven;

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

import de.tum.in.niedermr.ta.core.artifacts.exceptions.IArtifactExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.factory.MainArtifactVisitorFactory;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactModificationVisitor;
import de.tum.in.niedermr.ta.core.code.tests.detector.BiasedTestClassDetector;
import de.tum.in.niedermr.ta.core.code.tests.detector.ClassType;
import de.tum.in.niedermr.ta.core.code.tests.detector.ITestClassDetector;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.instrumentation.AnalysisInstrumentationOperation;
import de.tum.in.niedermr.ta.extensions.analysis.workflows.stackdistance.recording.v3.StackLogRecorderV3;

@Mojo(name = "sdist", defaultPhase = LifecyclePhase.PROCESS_TEST_CLASSES, requiresDependencyResolution = ResolutionScope.TEST)
public class StackDistanceMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	private MavenProject project;

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		String builtSourceCodeDirectory = project.getBuild().getOutputDirectory();

		try {
			List<String> classpathElements = project.getTestClasspathElements();
			instrumentSourceCode(builtSourceCodeDirectory, classpathElements);
		} catch (DependencyResolutionRequiredException | IteratorException e) {
			throw new MojoExecutionException("Exception", e);
		}
	}

	protected void instrumentSourceCode(String builtSourceCodeDirectory, List<String> classpathElements)
			throws IteratorException {
		String inputArtifactPath = builtSourceCodeDirectory;
		String outputArtifactPath = builtSourceCodeDirectory;
		IArtifactExceptionHandler exceptionHandler = MainArtifactVisitorFactory.INSTANCE
				.createArtifactExceptionHandler(true);

		IArtifactModificationVisitor modificationIterator = MainArtifactVisitorFactory.INSTANCE
				.createModificationVisitor(inputArtifactPath, outputArtifactPath, exceptionHandler);

		// the source code folder contains only source classes
		ITestClassDetector testClassDetector = new BiasedTestClassDetector(ClassType.NO_TEST_CLASS);

		AnalysisInstrumentationOperation operation = new AnalysisInstrumentationOperation(testClassDetector,
				StackLogRecorderV3.class);

		modificationIterator.execute(operation);
	}
}
