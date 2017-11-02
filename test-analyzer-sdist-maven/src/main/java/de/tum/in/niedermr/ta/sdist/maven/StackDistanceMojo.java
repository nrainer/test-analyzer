package de.tum.in.niedermr.ta.sdist.maven;

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
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		String builtSourceCodeDirectory = project.getBuild().getOutputDirectory();
		String inputArtifactPath = builtSourceCodeDirectory;
		String outputArtifactPath = builtSourceCodeDirectory;

		try {
			instrumentSourceCode(inputArtifactPath, outputArtifactPath);
		} catch (IteratorException e) {
			throw new MojoExecutionException("IteratorException", e);
		}
	}

	protected void instrumentSourceCode(String inputArtifactPath, String outputArtifactPath) throws IteratorException {
		// Note that the classes to be mutated are not loaded. Consequently, default test class detectors are not
		// working.

		IArtifactExceptionHandler exceptionHandler = MainArtifactVisitorFactory.INSTANCE
				.createArtifactExceptionHandler(false);

		IArtifactModificationVisitor modificationIterator = MainArtifactVisitorFactory.INSTANCE
				.createModificationVisitor(inputArtifactPath, outputArtifactPath, exceptionHandler);

		// the source code folder contains only source classes
		ITestClassDetector testClassDetector = new BiasedTestClassDetector(ClassType.NO_TEST_CLASS);

		AnalysisInstrumentationOperation operation = new AnalysisInstrumentationOperation(testClassDetector,
				StackLogRecorderV3.class);

		modificationIterator.execute(operation);
	}
}
