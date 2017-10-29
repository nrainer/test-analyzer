package de.tum.in.niedermr.ta.core.artifacts.jars;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import de.tum.in.niedermr.ta.core.artifacts.exceptions.DefaultIteratorExceptionHandler;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.artifacts.iterator.IArtifactIterator;
import de.tum.in.niedermr.ta.core.artifacts.visitor.AbstractArtifactVisitor;
import de.tum.in.niedermr.ta.core.artifacts.visitor.ArtifactModificationVisitor;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactAnalysisVisitor;
import de.tum.in.niedermr.ta.core.artifacts.visitor.IArtifactModificationVisitor;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeAnalyzeOperation;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;
import de.tum.in.niedermr.ta.core.common.TestUtility;

/** Test {@link JarAnalyzeIterator} and {@link ArtifactModificationVisitor}. */
public class JarIteratorTest {

	private static final String TEST_FOLDER = TestUtility.getTestFolder(JarIteratorTest.class);
	private static final String TEST_INPUT_JAR = TEST_FOLDER + "simple-project-lite.jar";
	private static final String TEST_RESOURCE_JAR = TEST_FOLDER + "jar-with-resource.jar";
	private static final String TEST_TEMP_JAR_1 = TEST_FOLDER + "temp_1.jar";
	private static final String TEST_TEMP_JAR_2 = TEST_FOLDER + "temp_2.jar";
	private static final String CLASSPATH_SIMPLE_CALCULATION = "de/tum/in/ma/simpleproject/lite/CalculationLite.class";
	private static final String CLASSPATH_UNIT_TEST = "de/tum/in/ma/simpleproject/lite/CalculationLiteTests.class";

	private static final JarVisitorFactory FACTORY = new JarVisitorFactory();

	/** Test. */
	@Test
	public void testInvocationSequence() throws IteratorException {
		IArtifactIterator artifactIterator = FACTORY.createArtifactIterator(TEST_INPUT_JAR,
				new DefaultIteratorExceptionHandler());

		SequenceRecorderVisitor it = new SequenceRecorderVisitor(artifactIterator);
		it.execute(new ICodeOperation() {
			/** {@inheritDoc} */
			@Override
			public void reset() {
				// NOP
			}
		});

		assertTrue(it.getLog().startsWith(SequenceRecorderVisitor.BEFORE_ALL));
		assertTrue(it.getLog().contains(SequenceRecorderVisitor.HANDLE_ENTRY + SequenceRecorderVisitor.HANDLE_ENTRY));
		assertTrue(it.getLog().endsWith(SequenceRecorderVisitor.AFTER_ALL));
	}

	/** Test. */
	@Test
	public void testAnalyzeIterator() throws IteratorException {
		IArtifactAnalysisVisitor visitor = FACTORY.createAnalyzeVisitor(TEST_INPUT_JAR, false);

		ContentRecorderOperation operation = new ContentRecorderOperation();
		visitor.execute(operation);

		assertEquals(2, operation.m_iteratedClasses.size());
		assertTrue(operation.m_iteratedClasses.contains(CLASSPATH_SIMPLE_CALCULATION));
		assertTrue(operation.m_iteratedClasses.contains(CLASSPATH_UNIT_TEST));
	}

	/** Test. */
	@Test
	public void testModificationIterator() throws IteratorException {
		File file = new File(TEST_TEMP_JAR_1);

		if (file.exists()) {
			file.delete();
		}

		IArtifactModificationVisitor modificationVisitor = FACTORY.createModificationVisitor(TEST_INPUT_JAR,
				TEST_TEMP_JAR_1, false);
		modificationVisitor.execute(new EmptyModificationOperation());

		assertTrue(file.exists());

		IArtifactAnalysisVisitor analysisVisitor = FACTORY.createAnalyzeVisitor(TEST_TEMP_JAR_1, false);
		ContentRecorderOperation checkOperation = new ContentRecorderOperation();
		analysisVisitor.execute(checkOperation);

		assertEquals(2, checkOperation.m_iteratedClasses.size());

		file.delete();
	}

	/** Test. */
	@Test
	public void testResourcesAreCopied() throws IteratorException {
		File file = new File(TEST_TEMP_JAR_2);

		if (file.exists()) {
			file.delete();
		}

		IArtifactModificationVisitor modificationVisitor = FACTORY.createModificationVisitor(TEST_RESOURCE_JAR,
				TEST_TEMP_JAR_2, false);
		modificationVisitor.execute(new EmptyModificationOperation());

		assertTrue(file.exists());

		IArtifactIterator artifactIterator = FACTORY.createArtifactIterator(TEST_TEMP_JAR_2,
				new DefaultIteratorExceptionHandler());
		SequenceRecorderVisitor analyzeIterator = new SequenceRecorderVisitor(artifactIterator);
		analyzeIterator.execute(new EmptyAnalyzeOperation());

		assertTrue(analyzeIterator.getLog().contains(SequenceRecorderVisitor.HANDLE_RESOURCE));

		file.delete();
	}

	class SequenceRecorderVisitor extends AbstractArtifactVisitor<ICodeOperation> {
		private static final String BEFORE_ALL = "beforeAll";
		private static final String HANDLE_ENTRY = "handleEntry";
		private static final String HANDLE_RESOURCE = "handleResource";
		private static final String AFTER_ALL = "afterAll";

		private final StringBuilder m_logger = new StringBuilder();

		public SequenceRecorderVisitor(IArtifactIterator iterator) {
			super(iterator);
		}

		/** {@inheritDoc} */
		@Override
		public void visitBeforeAll() {
			m_logger.append(BEFORE_ALL);
		}

		/** {@inheritDoc} */
		@Override
		public void visitClassEntry(ICodeOperation jarOperation, InputStream inputStream, String originalClassPath) {
			m_logger.append(HANDLE_ENTRY);
		}

		/** {@inheritDoc} */
		@Override
		public void visitResourceEntry(ICodeOperation jarOperation, InputStream inStream, String resourceEntryList) {
			m_logger.append(HANDLE_RESOURCE);
		}

		/** {@inheritDoc} */
		@Override
		public void visitAfterAll() {
			m_logger.append(AFTER_ALL);
		}

		public String getLog() {
			return m_logger.toString();
		}
	}

	class ContentRecorderOperation implements ICodeAnalyzeOperation {
		private final Set<String> m_iteratedClasses;

		public ContentRecorderOperation() {
			this.m_iteratedClasses = new HashSet<>();
		}

		/** {@inheritDoc} */
		@Override
		public void analyze(ClassReader cr, String originalClassPath) throws CodeOperationException {
			m_iteratedClasses.add(originalClassPath);
		}

		/** {@inheritDoc} */
		@Override
		public void reset() {
			// NOP
		}
	}

	class EmptyAnalyzeOperation implements ICodeAnalyzeOperation {
		/** {@inheritDoc} */
		@Override
		public void analyze(ClassReader cr, String originalClassPath) throws CodeOperationException {
			// NOP
		}

		/** {@inheritDoc} */
		@Override
		public void reset() {
			// NOP
		}
	}

	class EmptyModificationOperation implements ICodeModificationOperation {
		/** {@inheritDoc} */
		@Override
		public void modify(ClassReader cr, ClassWriter cw) throws CodeOperationException {
			// NOP
		}

		/** {@inheritDoc} */
		@Override
		public void reset() {
			// NOP
		}
	}
}
