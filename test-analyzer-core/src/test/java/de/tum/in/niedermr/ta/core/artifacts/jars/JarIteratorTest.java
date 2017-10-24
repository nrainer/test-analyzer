package de.tum.in.niedermr.ta.core.artifacts.jars;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;

import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import de.tum.in.niedermr.ta.core.analysis.content.ClassFileData;
import de.tum.in.niedermr.ta.core.artifacts.exceptions.IteratorException;
import de.tum.in.niedermr.ta.core.code.operation.CodeOperationException;
import de.tum.in.niedermr.ta.core.code.operation.ICodeAnalyzeOperation;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.TestUtility;

/** Test {@link JarAnalyzeIterator} and {@link JarModificationIterator}. */
public class JarIteratorTest {

	private static final String TEST_FOLDER = TestUtility.getTestFolder(JarIteratorTest.class);
	private static final String TEST_INPUT_JAR = TEST_FOLDER + "simple-project-lite.jar";
	private static final String TEST_RESOURCE_JAR = TEST_FOLDER + "jar-with-resource.jar";
	private static final String TEST_TEMP_JAR_1 = TEST_FOLDER + "temp_1.jar";
	private static final String TEST_TEMP_JAR_2 = TEST_FOLDER + "temp_2.jar";
	private static final String CLASSPATH_SIMPLE_CALCULATION = "de/tum/in/ma/simpleproject/lite/CalculationLite.class";
	private static final String CLASSPATH_UNIT_TEST = "de/tum/in/ma/simpleproject/lite/CalculationLiteTests.class";

	/** Test. */
	@Test
	public void testInvocationSequence() throws IteratorException {
		SequenceRecorderIterator it = new SequenceRecorderIterator(TEST_INPUT_JAR);
		it.execute(new ICodeOperation() {
			// NOP
		});

		assertTrue(it.getLog().startsWith(SequenceRecorderIterator.BEFORE_ALL));
		assertTrue(it.getLog().contains(SequenceRecorderIterator.HANDLE_ENTRY + SequenceRecorderIterator.HANDLE_ENTRY));
		assertTrue(it.getLog().endsWith(SequenceRecorderIterator.AFTER_ALL));
	}

	/** Test. */
	@Test
	public void testAnalyzeIterator() throws IteratorException {
		JarAnalyzeIterator it = new JarAnalyzeIterator(TEST_INPUT_JAR);
		ContentRecorderOperation operation = new ContentRecorderOperation();
		it.execute(operation);

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

		final Class<?> classToAdd = NewClass.class;

		JarModificationIterator modificationIterator = new JarModificationIterator(TEST_INPUT_JAR, TEST_TEMP_JAR_1) {
			@Override
			protected List<ClassFileData> getFurtherClassesToBeAdded() {
				List<ClassFileData> list = new LinkedList<>();

				try {
					list.add(new ClassFileData(JavaUtility.toClassPathWithEnding(classToAdd.getName()),
							new ClassReader(classToAdd.getName()).b));
				} catch (IOException ex) {
					// NOP
				}

				return list;
			}
		};

		modificationIterator.execute(new EmptyModificationOperation());

		assertTrue(file.exists());

		JarAnalyzeIterator analyzeIterator = new JarAnalyzeIterator(TEST_TEMP_JAR_1);
		ContentRecorderOperation checkOperation = new ContentRecorderOperation();
		analyzeIterator.execute(checkOperation);

		assertEquals(3, checkOperation.m_iteratedClasses.size());
		assertTrue(checkOperation.m_iteratedClasses.contains(JavaUtility.toClassPathWithEnding(classToAdd.getName())));

		file.delete();
	}

	/** Test. */
	@Test
	public void testResourcesAreCopied() throws IteratorException {
		File file = new File(TEST_TEMP_JAR_2);

		if (file.exists()) {
			file.delete();
		}

		JarModificationIterator modificationIterator = new JarModificationIterator(TEST_RESOURCE_JAR, TEST_TEMP_JAR_2);

		modificationIterator.execute(new EmptyModificationOperation());

		assertTrue(file.exists());

		SequenceRecorderIterator analyzeIterator = new SequenceRecorderIterator(TEST_TEMP_JAR_2);
		analyzeIterator.execute(new EmptyAnalyzeOperation());

		assertTrue(analyzeIterator.getLog().contains(SequenceRecorderIterator.HANDLE_RESOURCE));

		file.delete();
	}

	class SequenceRecorderIterator extends AbstractJarIterator<ICodeOperation> {
		private static final String BEFORE_ALL = "beforeAll";
		private static final String HANDLE_ENTRY = "handleEntry";
		private static final String HANDLE_RESOURCE = "handleResource";
		private static final String AFTER_ALL = "afterAll";

		private final StringBuilder m_logger = new StringBuilder();

		public SequenceRecorderIterator(String inputJarPath) {
			super(inputJarPath);
		}

		/** {@inheritDoc} */
		@Override
		protected void beforeAll() {
			m_logger.append(BEFORE_ALL);
		}

		/** {@inheritDoc} */
		@Override
		protected void handleEntry(ICodeOperation jarOperation, ClassReader cr, String originalClassPath) {
			m_logger.append(HANDLE_ENTRY);
		}

		/** {@inheritDoc} */
		@Override
		protected void handleResource(ICodeOperation jarOperation, JarEntry resourceEntryList, InputStream inStream) {
			m_logger.append(HANDLE_RESOURCE);
		}

		/** {@inheritDoc} */
		@Override
		protected void afterAll() {
			m_logger.append(AFTER_ALL);
		}

		public String getLog() {
			return m_logger.toString();
		}

		/** {@inheritDoc} */
		@Override
		protected void onExceptionInHandleEntry(Throwable t, String className) throws IteratorException {
			// NOP
		}

		/** {@inheritDoc} */
		@Override
		protected void onExceptionInHandleResource(Throwable t, String resourcePath) throws IteratorException {
			// NOP
		}

		/** {@inheritDoc} */
		@Override
		protected void onExceptionInJarProcessing(Throwable throwable, ICodeOperation jarOperation)
				throws IteratorException {
			// NOP
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
		public void clearResult() {
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
		public void clearResult() {
			// NOP
		}
	}

	class EmptyModificationOperation implements ICodeModificationOperation {
		/** {@inheritDoc} */
		@Override
		public void modify(ClassReader cr, ClassWriter cw) throws CodeOperationException {
			// NOP
		}
	}
}
