package de.tum.in.niedermr.ta.core.analysis.jars.iteration;

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

import de.tum.in.niedermr.ta.core.analysis.jars.content.JarFileElementRawData;
import de.tum.in.niedermr.ta.core.code.operation.ICodeAnalyzeOperation;
import de.tum.in.niedermr.ta.core.code.operation.ICodeModificationOperation;
import de.tum.in.niedermr.ta.core.code.operation.ICodeOperation;
import de.tum.in.niedermr.ta.core.code.util.JavaUtility;
import de.tum.in.niedermr.ta.core.common.TestUtility;

public class JarIteratorTest {
	private static final String TEST_FOLDER = TestUtility.getTestFolder(JarIteratorTest.class);
	private static final String TEST_INPUT_JAR = TEST_FOLDER + "simple-project-lite.jar";
	private static final String TEST_RESOURCE_JAR = TEST_FOLDER + "jar-with-resource.jar";
	private static final String TEST_TEMP_JAR_1 = TEST_FOLDER + "temp_1.jar";
	private static final String TEST_TEMP_JAR_2 = TEST_FOLDER + "temp_2.jar";
	private static final String CLASSPATH_SIMPLE_CALCULATION = "de/tum/in/ma/project/example/SimpleCalculation.class";
	private static final String CLASSPATH_UNIT_TEST = "de/tum/in/ma/project/example/UnitTest.class";

	@Test
	public void testInvocationSequence() throws Throwable {
		SequenceRecorderIterator it = new SequenceRecorderIterator(TEST_INPUT_JAR);
		it.execute(new ICodeOperation() {
			// NOP
		});

		assertTrue(it.getLog().startsWith(SequenceRecorderIterator.BEFORE_ALL));
		assertTrue(it.getLog().contains(SequenceRecorderIterator.HANDLE_ENTRY + SequenceRecorderIterator.HANDLE_ENTRY));
		assertTrue(it.getLog().endsWith(SequenceRecorderIterator.AFTER_ALL));
	}

	@Test
	public void testAnalyzeIterator() throws Throwable {
		JarAnalyzeIterator it = new JarAnalyzeIterator(TEST_INPUT_JAR);
		ContentRecoderOperation operation = new ContentRecoderOperation();
		it.execute(operation);

		assertEquals(2, operation.m_iteratedClasses.size());
		assertTrue(operation.m_iteratedClasses.contains(CLASSPATH_SIMPLE_CALCULATION));
		assertTrue(operation.m_iteratedClasses.contains(CLASSPATH_UNIT_TEST));
	}

	@Test
	public void testModificationIterator() throws Throwable {
		File file = new File(TEST_TEMP_JAR_1);

		if (file.exists()) {
			file.delete();
		}

		final Class<?> classToAdd = NewClass.class;

		JarModificationIterator modificationIterator = new JarModificationIterator(TEST_INPUT_JAR, TEST_TEMP_JAR_1) {
			@Override
			protected List<JarFileElementRawData> getFurtherClassesToBeAdded() {
				List<JarFileElementRawData> list = new LinkedList<>();

				try {
					list.add(new JarFileElementRawData(JavaUtility.toClassPathWithEnding(classToAdd.getName()),
							new ClassReader(classToAdd.getName()).b));
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				return list;
			}
		};

		modificationIterator.execute(new EmptyModificationOperation());

		assertTrue(file.exists());

		JarAnalyzeIterator analyzeIterator = new JarAnalyzeIterator(TEST_TEMP_JAR_1);
		ContentRecoderOperation checkOperation = new ContentRecoderOperation();
		analyzeIterator.execute(checkOperation);

		assertEquals(3, checkOperation.m_iteratedClasses.size());
		assertTrue(checkOperation.m_iteratedClasses.contains(JavaUtility.toClassPathWithEnding(classToAdd.getName())));

		file.delete();
	}

	@Test
	public void testResourcesAreCopied() throws Throwable {
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

		@Override
		protected void beforeAll() {
			m_logger.append(BEFORE_ALL);
		}

		@Override
		protected void handleEntry(ICodeOperation jarOperation, ClassReader cr, String originalClassPath) {
			m_logger.append(HANDLE_ENTRY);
		}

		@Override
		protected void handleResource(ICodeOperation jarOperation, JarEntry resourceEntryList, InputStream inStream) {
			m_logger.append(HANDLE_RESOURCE);
		}

		@Override
		protected void afterAll() {
			m_logger.append(AFTER_ALL);
		}

		public String getLog() {
			return m_logger.toString();
		}

		@Override
		protected void onExceptionInHandleEntry(Throwable t, String className) throws Exception {
			// NOP
		}

		@Override
		protected void onExceptionInHandleResource(Throwable t, String resourcePath) throws Exception {
			// NOP
		}
	}

	class ContentRecoderOperation implements ICodeAnalyzeOperation {
		private final Set<String> m_iteratedClasses;

		public ContentRecoderOperation() {
			this.m_iteratedClasses = new HashSet<>();
		}

		@Override
		public void analyze(ClassReader cr, String originalClassPath) throws Exception {
			m_iteratedClasses.add(originalClassPath);
		}
	}

	class EmptyAnalyzeOperation implements ICodeAnalyzeOperation {
		@Override
		public void analyze(ClassReader cr, String originalClassPath) throws Exception {
			// NOP
		}
	}

	class EmptyModificationOperation implements ICodeModificationOperation {
		@Override
		public void modify(ClassReader cr, ClassWriter cw) throws Exception {
			// NOP
		}
	}
}
